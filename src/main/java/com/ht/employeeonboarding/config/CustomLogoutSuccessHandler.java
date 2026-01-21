package com.ht.employeeonboarding.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.ht.employeeonboarding.util.JwtUtil;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomLogoutSuccessHandler.class);
    private final JwtUtil jwtUtil;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public CustomLogoutSuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {

        String role = null;
        String token = request.getHeader("Authorization");
        LocalDateTime logoutTime = LocalDateTime.now();
        LocalDateTime jwtExpiryTime = null;

        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            try {
                Claims claims = jwtUtil.extractAllClaims(jwtToken);
                if (claims.get("roles") != null) {
                    role = claims.get("roles").toString();
                }

                // Convert JWT Expiration to DateTime format
                if (claims.getExpiration() != null) {
                    jwtExpiryTime = LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(claims.getExpiration().getTime()),
                            ZoneId.systemDefault()
                    );
                }

                logger.info("✅ JWT Claims Extracted: Role: {}, Logout Time: {}, JWT Expiry Time: {}",
                        role != null ? role : "NOT SPECIFIED",
                        logoutTime.format(DATE_FORMATTER),
                        (jwtExpiryTime != null ? jwtExpiryTime.format(DATE_FORMATTER) : "NOT SPECIFIED"));

            } catch (Exception e) {
                logger.warn("❌ Failed to extract claims from JWT during logout: {}", e.getMessage());
            }
        }

        logger.info("✅ User logged out at {}. JWT Expiry: {}",
                    logoutTime.format(DATE_FORMATTER),
                    (jwtExpiryTime != null ? jwtExpiryTime.format(DATE_FORMATTER) : "NOT SPECIFIED"));

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("{\"message\": \"Logout successful\"}");
        response.getWriter().flush();
    }

	
}
