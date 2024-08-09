package com.writer.writerapp.controllers;

import com.writer.writerapp.Models.RequestVO.UserMgmtRequestVO;
import com.writer.writerapp.Models.ResponseVO.AuthenticationResponse;
import com.writer.writerapp.Service.CognitoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private CognitoService cognitoService;

    @GetMapping("/hello")
    public ResponseEntity<String> publicHello() {
        return ResponseEntity.ok("Hello, this is a public endpoint!");
    }
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody UserMgmtRequestVO.SignUpRequest request) {
        try {
            SignUpResponse response = cognitoService.signUp(request.getUsername(), request.getPassword(), request.getEmail(), request.getName());
            return ResponseEntity.status(HttpStatus.OK).body("OK");
        } catch (UsernameExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        } catch (CognitoIdentityProviderException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.awsErrorDetails().errorMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody UserMgmtRequestVO.LoginRequest request) {
        try {
            InitiateAuthResponse response = cognitoService.login(request.getUsername(), request.getPassword());
            AuthenticationResponse authResponse = cognitoService.mapToAuthenticationResponse(response.authenticationResult());
            return ResponseEntity.ok(authResponse);
        } catch (CognitoIdentityProviderException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmSignUp(@RequestBody UserMgmtRequestVO.ConfirmSignUpRequest request) {
        try {
            cognitoService.confirmSignUp(request.getUsername(), request.getConfirmationCode());
            return ResponseEntity.ok("User confirmed successfully");
        } catch (CognitoIdentityProviderException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.awsErrorDetails().errorMessage());
        }
    }

    @PostMapping("/resend-confirmation")
    public ResponseEntity<?> resendConfirmationCode(@RequestBody UserMgmtRequestVO.ResendConfirmationRequest request) {
        try {
            cognitoService.resendConfirmationCode(request.getUsername());
            return ResponseEntity.ok("Confirmation code resent successfully");
        } catch (CognitoIdentityProviderException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.awsErrorDetails().errorMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody UserMgmtRequestVO.ForgotPasswordRequest request) {
        try {
            cognitoService.forgotPassword(request.getUsername());
            return ResponseEntity.ok("Password reset code sent successfully");
        } catch (CognitoIdentityProviderException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.awsErrorDetails().errorMessage());
        }
    }

    @PostMapping("/confirm-forgot-password")
    public ResponseEntity<?> confirmForgotPassword(@RequestBody UserMgmtRequestVO.ConfirmForgotPasswordRequest request) {
        try {
            cognitoService.confirmForgotPassword(request.getUsername(), request.getNewPassword(), request.getConfirmationCode());
            return ResponseEntity.ok("Password reset successfully");
        } catch (CognitoIdentityProviderException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.awsErrorDetails().errorMessage());
        }
    }
}