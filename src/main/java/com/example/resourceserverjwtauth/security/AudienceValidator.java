package com.example.resourceserverjwtauth.security;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import static com.example.resourceserverjwtauth.security.SecurityConfig.AUTHORITIES_CLAIM_NAME;

/**
 * @author Sandaru Anjana <sandaruanjana@outlook.com>
 */
public class AudienceValidator implements OAuth2TokenValidator<Jwt> {

    OAuth2Error error = new OAuth2Error("invalid_token", "The required roles is missing", null);

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        if (token.getClaims().containsKey(AUTHORITIES_CLAIM_NAME)) {
            return OAuth2TokenValidatorResult.success();
        }
        return OAuth2TokenValidatorResult.failure(error);
    }
}
