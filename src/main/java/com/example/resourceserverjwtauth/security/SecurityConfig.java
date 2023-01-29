package com.example.resourceserverjwtauth.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

/**
 * @author Sandaru Anjana <sandaruanjana@outlook.com>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    public static final String AUTHORITIES_CLAIM_NAME = "roles";

    @Value("${jwt.secret}")
    private String jwtSecret;

    /**
     * The authenticationManager function is a Spring Security bean that will be used to authenticate users.
     * It uses the AuthenticationConfiguration class to configure the authentication manager with a user store and an authenticator.
     * The AuthenticationConfiguration class is defined in application-security-config.xml, which was created by Spring Boot when we ran &quot;spring init web myapp&quot;.
     *
     * @param authConfig Get the authenticationmanagerbuilder
     * @return An authenticationmanager instance
     */
    @Bean
    @Primary
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .cors().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/auth/login/**").permitAll()
                .requestMatchers("/api/v1/auth/signup/**").permitAll()
                .requestMatchers("/api/v1/auth/test/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt().jwtAuthenticationConverter(jwtAuthenticationConverter())
                .and().and().build();
    }

    /**
     * The jwtAuthenticationConverter function is used to convert the JWT token into an Authentication object.
     * The JwtAuthenticationConverter class is a Spring Security Converter that converts a String value into an Authentication object.
     * This function uses the jwtGrantedAuthoritiesConverter function to convert the JWT token into an Authentication object.
     * This function replace default authorities claim name "SCOPE" with "roles".
     *
     * @return JwtAuthenticationConverter
     */
    protected JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("");
        authoritiesConverter.setAuthoritiesClaimName(AUTHORITIES_CLAIM_NAME);

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }

    /**
     * The jwtEncoder function is a Spring Bean that returns an instance of the
     * NimbusJwtEncoder class. The NimbusJwtEncoder class is a JWT encoder implementation
     * provided by the Stormpath Java SDK. This encoder uses the HS256 algorithm to encode
     * and decode JWTs, which stands for HMAC using SHA-256 as its hashing algorithm. The
     * HS256 algorithm provides security against tampering because it uses a shared secret
     * between your application and Stormpath to sign each token, which means that anyone who
     * gets hold of this secret can create their own tokens -
     *
     * @return NimbusJwtEncoder
     */
    @Bean
    @Primary
    JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtSecret.getBytes()));
    }


    /**
     * The jwtDecoder function is a Spring Bean that returns an instance of the NimbusJwtDecoder class.
     * The NimbusJwtDecoder class is a JWT decoder that uses the RSA algorithm to decode JWTs.
     * The jwtSecret variable holds the secret key used by this decoder, which was generated using openssl and converted into bytes using Base64 encoding.
     *
     * @return NimbusJwtDecoder
     */
    @Bean
    @Primary
    public JwtDecoder jwtDecoder() {

        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator();
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(audienceValidator);

        byte[] bytes = jwtSecret.getBytes();
        SecretKeySpec originalKey = new SecretKeySpec(bytes, 0, bytes.length, "RSA");
        NimbusJwtDecoder build = NimbusJwtDecoder.withSecretKey(originalKey).macAlgorithm(MacAlgorithm.HS512).build();
        build.setJwtValidator(withAudience);
        return build;
    }


//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("http://localhost:8081"));
//        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
//        configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**",configuration);
//        return source;
//    }
}
