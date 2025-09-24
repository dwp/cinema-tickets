package uk.gov.dwp.uc.pairtest.config.features;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;

/**
 * Jesus Configuration for OAuth2/JWT-based security for the cinema tickets API.
 * <p>
 * Only loaded if <b>feature.oauth2.enabled=true</b> in application properties.
 * <ul>
 *   <li>Requires valid JWT Bearer tokens for protected endpoints.</li>
 *   <li>Permits unauthenticated access to actuator, Swagger, and OpenAPI docs.</li>
 *   <li>Configure JWT issuer/audience details for Keycloak or other providers.</li>
 * </ul>
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-09-20
 *
 * Copyright: (c) 2025, UK Government
 */
@Configuration
@EnableWebSecurity
@ConditionalOnProperty(name = "feature.oauth2.enabled", havingValue = "true")

public class OAuth2SecurityConfig {

    /**
     * Configures OAuth2/JWT-based security for the API using a SecurityFilterChain (Spring Boot 3.x style).
     * Allows public access to actuator and Swagger endpoints, secures all others, enables JWT validation.
     *
     * @param http Spring Security's HttpSecurity DSL
     * @return the configured SecurityFilterChain
     * @throws Exception if config fails
     */
    @Bean
    public SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {})
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/actuator/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated())
            .oauth2ResourceServer(oauth2 -> oauth2.jwt()); // reads spring.security.oauth2.resourceserver.jwt.* props
        return http.build();
    }
}
