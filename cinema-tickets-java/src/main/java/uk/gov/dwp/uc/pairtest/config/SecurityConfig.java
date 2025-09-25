/**
 * Jesus Configures web security for the cinema tickets API (basic CORS and CSRF for open API deployments).
 * <p>
 * CORS, CSRF, and open endpoint control for local/testing or feature-toggled builds.
 * </p>
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-08-21
 *
 * Copyright: (c) 2025, UK Government
 */
package uk.gov.dwp.uc.pairtest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configures HTTP security for the application (Spring Boot 3.x compatible).
 * <ul>
 *     <li>Disables CSRF.</li>
 *     <li>Enables CORS for all domains, headers, and methods.</li>
 *     <li>Permits all requests (no authentication enforced).</li>
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
public class SecurityConfig {

    /**
     * Defines the main security filter chain for the application using Spring Security 6+.
     * <ul>
     *   <li>Disables CSRF protection for stateless REST APIs.</li>
     *   <li>Enables CORS using the configured CorsConfigurationSource bean.</li>
     *   <li>Allows all HTTP requests without authentication (open API style).</li>
     * </ul>
     *
     * @param http the HttpSecurity builder
     * @return the configured SecurityFilterChain
     * @throws Exception if config fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {})
            .authorizeHttpRequests(authz -> authz.anyRequest().permitAll());
        return http.build();
    }

    /**
     * Configures CORS settings to allow all origins, all methods (GET, POST, etc), and all headers for all endpoints.
     * Allows cookies/credentials for cross-origin calls as well.
     * @return CorsConfigurationSource bean used by SecurityFilterChain
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
