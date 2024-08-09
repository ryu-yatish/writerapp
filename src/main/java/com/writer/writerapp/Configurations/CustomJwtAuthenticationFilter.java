package com.writer.writerapp.Configurations;
import com.nimbusds.jwt.JWT;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;

@Slf4j
@Component
@AllArgsConstructor
public class CustomJwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationFailureHandler failureHandler;
    private final JwtDecoder jwtDecoder;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = extractToken(request);
            try {
                if (token != null) {
                    Jwt decodedJwt = jwtDecoder.decode(token);
                }
            }catch (JwtValidationException jex){
                log.error(jex.toString());
                request.setAttribute("expired", "true");
                throw new JwtValidationException("expired token", Collections.singleton(new OAuth2Error("token_expired", "The token has expired", null)));
            }catch (BadJwtException bex){
                log.error(bex.toString());

                throw new JwtValidationException("bad token", Collections.singleton(new OAuth2Error("invalid_token", "The token is invalid", null)));
            }

            filterChain.doFilter(request, response);
        } catch (AuthenticationException ex) {
            SecurityContextHolder.clearContext();
            failureHandler.onAuthenticationFailure(request, response, ex);
        }
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


}