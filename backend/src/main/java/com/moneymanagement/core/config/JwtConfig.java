package com.moneymanagement.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;

import java.time.Duration;
import java.util.stream.Collectors;

@Configuration
public class JwtConfig {

    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri("https://www.googleapis.com/oauth2/v3/certs").build();

        // Create a custom JwtTimestampValidator with 1 day clock skew allowance to handle future iat claims
        JwtTimestampValidator timestampValidator = new JwtTimestampValidator(Duration.ofDays(1));

        // Create other default validators except JwtTimestampValidator
        OAuth2TokenValidator<Jwt> issuerValidator = new JwtIssuerValidator("https://accounts.google.com");

        // Combine the issuer validator with the custom timestamp validator
        OAuth2TokenValidator<Jwt> combinedValidator = new DelegatingOAuth2TokenValidator<>(
                issuerValidator,
                timestampValidator
        );

        // Wrap the combined validator with a custom validator that ignores "iat" claim errors
        OAuth2TokenValidator<Jwt> customValidator = new OAuth2TokenValidator<Jwt>() {
            @Override
            public OAuth2TokenValidatorResult validate(Jwt token) {
                OAuth2TokenValidatorResult result = combinedValidator.validate(token);
                if (result.hasErrors()) {
                    // Filter out errors related to "iat" claim
                    var filteredErrors = result.getErrors().stream()
                            .filter(error -> !error.getDescription().contains("iat"))
                            .collect(Collectors.toSet());
                    if (filteredErrors.isEmpty()) {
                        return OAuth2TokenValidatorResult.success();
                    } else {
                        return OAuth2TokenValidatorResult.failure(filteredErrors);
                    }
                }
                return result;
            }
        };

        jwtDecoder.setJwtValidator(customValidator);

        return jwtDecoder;
    }
}
