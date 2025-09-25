package uk.gov.dwp.uc.pairtest.config.features;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;

/**
 * Jesus Configuration class for default (open) security settings when OAuth2 is disabled.
 * <p>
 * <ul>
 *   <li>CSRF protection is disabled for easier testing and stateless APIs.</li>
 *   <li>CORS is enabled for all origins, methods, and headers, to facilitate cross-domain requests during development or in microservices.</li>
 *   <li>All HTTP endpoints are permitted by default (no authentication enforced).</li>
 *   <li>Conditionally loaded only if <b>feature.oauth2.enabled=false</b> (or missing).</li>
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
@ConditionalOnProperty(name = "feature.oauth2.enabled", havingValue = "false", matchIfMissing = true)

public class DefaultSecurityConfig {

    /**
     * Configures application security for the case when OAuth2 is disabled.
     * Disables CSRF, enables CORS, and allows all requests.
     * 
     * @param http Spring's HttpSecurity DSL
     * @return the configured SecurityFilterChain
     * @throws Exception if config fails
     */
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .cors(cors -> {})
            .authorizeHttpRequests(authz -> authz.anyRequest().permitAll());
        return http.build();
    }
    // corsConfigurationSource bean is defined elsewhere to avoid bean conflicts
}
