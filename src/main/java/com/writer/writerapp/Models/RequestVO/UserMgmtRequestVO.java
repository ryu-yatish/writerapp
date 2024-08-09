package com.writer.writerapp.Models.RequestVO;

import lombok.*;


public class UserMgmtRequestVO {
    // SignUpRequest.java
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUpRequest {
        private String username;
        private String password;
        private String email;
        private String name;

        // Getters and Setters
    }

    // LoginRequest.java
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {
        private String username;
        private String password;

        // Getters and Setters
    }

    // ConfirmSignUpRequest.java
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConfirmSignUpRequest {
        private String username;
        private String confirmationCode;

        // Getters and Setters
    }

    // ResendConfirmationRequest.java
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResendConfirmationRequest {
        private String username;

        // Getters and Setters
    }

    // ForgotPasswordRequest.java
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForgotPasswordRequest {
        private String username;

        // Getters and Setters
    }

    // ConfirmForgotPasswordRequest.java
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConfirmForgotPasswordRequest {
        private String username;
        private String newPassword;
        private String confirmationCode;

        // Getters and Setters
    }

}
