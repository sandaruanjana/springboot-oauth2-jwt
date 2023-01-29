package com.example.resourceserverjwtauth.utill;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

/**
 * @author Sandaru Anjana <sandaruanjana@outlook.com>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtils {
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public String generateToken(String username, String issuer, List<String> roles) {

        Instant now = Instant.now();
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .subject(username)
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.HOURS))
                .claim("roles", roles)
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS512).build(), jwtClaimsSet);
        return jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }

    public String generateRefreshToken(String username, String issuer, List<String> roles) {

        Instant now = Instant.now();
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .subject(username)
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plus(60, ChronoUnit.DAYS))
                .claim("roles", roles)
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS512).build(), jwtClaimsSet);
        return jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            return jwt.getExpiresAt().isAfter(Instant.now());
        } catch (Exception e) {
            log.error("Token is not valid", e);
            return false;
        }
    }

    public Map<String, Object> getTokenClaim(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getClaims();
    }

    public Long getTokenExpirationTime(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getExpiresAt().getEpochSecond();
    }

}
