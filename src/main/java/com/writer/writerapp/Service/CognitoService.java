package com.writer.writerapp.Service;


import com.writer.writerapp.Models.ResponseVO.AuthenticationResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CognitoService {
    @Value("${aws.cognito.clientId}")
    private String clientId;
    @Value("${aws.cognito.clientSecret}")
    private String clientSecret;
    private final Region region = Region.US_EAST_1;

    private CognitoIdentityProviderClient getClient() {
        return CognitoIdentityProviderClient.builder()
                .region(region)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }

    private String calculateSecretHash(String username) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(clientSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKey);
            mac.update(username.getBytes(StandardCharsets.UTF_8));
            mac.update(clientId.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(mac.doFinal());
        } catch (Exception e) {
            throw new RuntimeException("Error while calculating secret hash", e);
        }
    }

    public SignUpResponse signUp(String username, String password, String email, String name) {
        CognitoIdentityProviderClient client = getClient();

        AttributeType emailAttr = AttributeType.builder().name("email").value(email).build();
        AttributeType nameAttr = AttributeType.builder().name("name").value(name).build();

        SignUpRequest request = SignUpRequest.builder()
                .clientId(clientId)
                .secretHash(calculateSecretHash(username))
                .username(username)
                .password(password)
                .userAttributes(emailAttr, nameAttr)
                .build();

        return client.signUp(request);
    }

    public InitiateAuthResponse login(String username, String password) {
        CognitoIdentityProviderClient client = getClient();

        Map<String, String> authParams = new HashMap<>();
        authParams.put("USERNAME", username);
        authParams.put("PASSWORD", password);
        authParams.put("SECRET_HASH", calculateSecretHash(username));

        InitiateAuthRequest request = InitiateAuthRequest.builder()
                .authFlow(AuthFlowType.USER_PASSWORD_AUTH)
                .clientId(clientId)
                .authParameters(authParams)
                .build();

        return client.initiateAuth(request);
    }
    public AuthenticationResponse mapToAuthenticationResponse(AuthenticationResultType authResult) {
        AuthenticationResponse response = new AuthenticationResponse();
        response.setAccessToken(authResult.accessToken());
        response.setIdToken(authResult.idToken());
        response.setRefreshToken(authResult.refreshToken());
        response.setExpiresIn(authResult.expiresIn());
        response.setTokenType(authResult.tokenType());
        return response;
    }

    public void confirmSignUp(String username, String confirmationCode) {
        CognitoIdentityProviderClient client = getClient();

        ConfirmSignUpRequest request = ConfirmSignUpRequest.builder()
                .clientId(clientId)
                .secretHash(calculateSecretHash(username))
                .username(username)
                .confirmationCode(confirmationCode)
                .build();

        client.confirmSignUp(request);
    }

    public void resendConfirmationCode(String username) {
        CognitoIdentityProviderClient client = getClient();

        ResendConfirmationCodeRequest request = ResendConfirmationCodeRequest.builder()
                .clientId(clientId)
                .secretHash(calculateSecretHash(username))
                .username(username)
                .build();

        client.resendConfirmationCode(request);
    }

    public void forgotPassword(String username) {
        CognitoIdentityProviderClient client = getClient();

        ForgotPasswordRequest request = ForgotPasswordRequest.builder()
                .clientId(clientId)
                .secretHash(calculateSecretHash(username))
                .username(username)
                .build();

        client.forgotPassword(request);
    }

    public void confirmForgotPassword(String username, String newPassword, String confirmationCode) {
        CognitoIdentityProviderClient client = getClient();

        ConfirmForgotPasswordRequest request = ConfirmForgotPasswordRequest.builder()
                .clientId(clientId)
                .secretHash(calculateSecretHash(username))
                .username(username)
                .password(newPassword)
                .confirmationCode(confirmationCode)
                .build();

        client.confirmForgotPassword(request);
    }
}