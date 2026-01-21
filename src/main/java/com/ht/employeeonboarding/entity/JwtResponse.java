package com.ht.employeeonboarding.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private Long userId;
    private String userEmail;
    private String accessToken;
    private String refreshToken;
    private String role;
    private Long employeeId;
    private boolean profileComplete;
    private String error;

    // Optional: If you want a custom builder (but Lombok @Builder already handles it)
    public static JwtResponseBuilder builder() {
        return new JwtResponseBuilder();
    }

    public static class JwtResponseBuilder {
        private Long userId;
        private String userEmail;
        private String accessToken;
        private String refreshToken;
        private String role;
        private Long employeeId;
        private boolean profileComplete;
        private String error;

        public JwtResponseBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public JwtResponseBuilder userEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        public JwtResponseBuilder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public JwtResponseBuilder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public JwtResponseBuilder role(String role) {
            this.role = role;
            return this;
        }

        public JwtResponseBuilder employeeId(Long employeeId) {
            this.employeeId = employeeId;
            return this;
        }

        public JwtResponseBuilder profileComplete(boolean profileComplete) {
            this.profileComplete = profileComplete;
            return this;
        }

        public JwtResponseBuilder error(String error) {
            this.error = error;
            return this;
        }

        public JwtResponse build() {
            return new JwtResponse(userId, userEmail, accessToken, refreshToken, role, employeeId, profileComplete, error);
        }
    }
}
