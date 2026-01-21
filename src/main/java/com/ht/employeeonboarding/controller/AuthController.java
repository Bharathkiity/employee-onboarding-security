package com.ht.employeeonboarding.controller;

import com.ht.employeeonboarding.entity.JwtRequest;
import com.ht.employeeonboarding.entity.JwtResponse;
import com.ht.employeeonboarding.entity.RefreshTokenRequest;
import com.ht.employeeonboarding.service.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest authenticationRequest) {
        try {
            JwtResponse jwtResponse = jwtService.createJwtToken(authenticationRequest, authenticationManager);
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<JwtResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        try {
            JwtResponse jwtResponse = jwtService.refreshToken(refreshTokenRequest);
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            // Returning JwtResponse with error message
            JwtResponse errorResponse = JwtResponse.builder()
                    .error("Refresh Token is invalid")
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }




    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        Map<String, String> response = new HashMap<>();
        try {
            jwtService.logoutUser(refreshTokenRequest.getRefreshToken());
            response.put("message", "Logout successful");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Logout failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
