package com.ht.employeeonboarding.service;

import com.ht.employeeonboarding.entity.*;
import com.ht.employeeonboarding.repository.RefreshTokenRepository;
import com.ht.employeeonboarding.repository.UserRepository;
import com.ht.employeeonboarding.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class JwtService implements UserDetailsService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenService refreshTokenService;
    
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    public UserDetails loadUserByUsername(String useremail) throws UsernameNotFoundException {
        User user = userRepository.findByUserEmail(useremail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + useremail));

        return new org.springframework.security.core.userdetails.User(
                user.getUserEmail(),
                user.getUserPassword(),
                getAuthorities(user)
        );
    }

    @Transactional
    public JwtResponse createJwtToken(JwtRequest jwtRequest, AuthenticationManager authenticationManager) throws Exception {
        String userName = jwtRequest.getUserEmail();
        String userPassword = jwtRequest.getUserPassword();

        authenticate(userName, userPassword, authenticationManager);

        UserDetails userDetails = loadUserByUsername(userName);
        String accessToken = jwtUtil.generateToken(userDetails);

        User user = userRepository.findByUserEmail(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + userName));

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return JwtResponse.builder()
                .userEmail(user.getUserEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .role(user.getRole().getRoleName())
                .build();
    }

    @Transactional
    public JwtResponse refreshToken(RefreshTokenRequest request) {
        return refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    UserDetails userDetails = loadUserByUsername(user.getUserEmail());
                    String newAccessToken = jwtUtil.generateToken(userDetails);

                    return JwtResponse.builder()
                            .userEmail(user.getUserEmail())
                            .accessToken(newAccessToken)
                            .refreshToken(request.getRefreshToken())
                            .role(user.getRole().getRoleName())
                            .build();
                }).orElseThrow(() -> new RuntimeException("Refresh token not found in database"));
    }

    private Set<SimpleGrantedAuthority> getAuthorities(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName()));
        return authorities;
    }

    private void authenticate(String username, String password, AuthenticationManager authenticationManager) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @Transactional
    public void logoutUser(String refreshToken) {
        Optional<RefreshToken> storedToken = refreshTokenRepository.findByToken(refreshToken);
        if (storedToken.isPresent()) {
            RefreshToken token = storedToken.get();
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        } else {
            throw new RuntimeException("Refresh token not found");
        }
    }
}
