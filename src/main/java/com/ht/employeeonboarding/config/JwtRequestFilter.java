package com.ht.employeeonboarding.config;

import com.ht.employeeonboarding.entity.JwtResponse;
import com.ht.employeeonboarding.entity.RefreshTokenRequest;
import com.ht.employeeonboarding.service.JwtService;
import com.ht.employeeonboarding.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);
    private final JwtUtil jwtUtil;
    private final JwtService jwtService;

    
    public JwtRequestFilter(JwtUtil jwtUtil, JwtService jwtService) {
        this.jwtUtil = jwtUtil;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Log all headers for debugging
        logger.info("🔍 Incoming Request Headers:");
        request.getHeaderNames().asIterator().forEachRemaining(header -> 
            logger.info("{}: {}", header, request.getHeader(header))
        );

        final String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        logger.info("🔍 JWT Request Filter Triggered");

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            logger.info("✅ JWT Token Found: {}", jwtToken);

            try {
                username = jwtUtil.getUsernameFromToken(jwtToken);
                logger.info("✅ Extracted Username: {}", username);
            } catch (ExpiredJwtException e) {
                handleExpiredToken(request, response, e);
                return;
            } catch (Exception e) {
                logger.error("❌ Error parsing JWT: {}", e.getMessage());
            }
        } else {
            logger.warn("❌ No JWT Token in Request Header or Invalid Format");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = jwtService.loadUserByUsername(username);
            if (jwtUtil.validateToken(jwtToken, userDetails)) {
                setAuthentication(request, userDetails);
                logger.info("✅ User Authenticated: {}", username);
            } else {
                logger.warn("❌ Invalid JWT Token for User: {}", username);
            }
        }

        chain.doFilter(request, response);
    }


    private void handleExpiredToken(HttpServletRequest request, HttpServletResponse response, ExpiredJwtException e) throws IOException {
        String isRefreshToken = request.getHeader("isRefreshToken");
        String requestURL = request.getRequestURL().toString();
        LocalDateTime jwtExpiryTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(e.getClaims().getExpiration().getTime()),
                ZoneId.systemDefault()
        );

        logger.warn("❌ JWT Token Expired for User: {}. Expiration Time: {}",
                e.getClaims().getSubject(),
                jwtExpiryTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        if ("true".equals(isRefreshToken) && requestURL.contains("/api/auth/refreshToken")) {
            logger.info("🔄 Refresh Token Activated for User: {} at {}", 
                    e.getClaims().getSubject(), 
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            allowForRefreshToken(request, e);
        } else {
            logger.info("🚀 Attempting Automatic Refresh for Expired Token...");

            // Automatically attempt to refresh token
            try {
                String refreshToken = request.getHeader("Refresh-Token");
                if (refreshToken != null) {
                    RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
                    refreshTokenRequest.setRefreshToken(refreshToken);

                    JwtResponse newTokens = jwtService.refreshToken(refreshTokenRequest);
                    response.setHeader("Authorization", "Bearer " + newTokens.getAccessToken());
                    response.setHeader("Refresh-Token", newTokens.getRefreshToken());
                    logger.info("✅ Token Automatically Refreshed for User: {}", newTokens.getUserEmail());
                } else {
                    logger.error("❌ Refresh Token not provided. Automatic refresh failed.");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"error\":\"JWT Token has expired and no Refresh Token provided\"}");
                }
            } catch (Exception ex) {
                logger.error("❌ Automatic Token Refresh Failed: {}", ex.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\":\"JWT Token expired and refresh failed\"}");
            }
        }
    }



    private void setAuthentication(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        logger.info("✅ Authentication Set for User: {}", userDetails.getUsername());
    }

    private void allowForRefreshToken(HttpServletRequest request, ExpiredJwtException e) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(null, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        request.setAttribute("claims", e.getClaims());
    }
}
