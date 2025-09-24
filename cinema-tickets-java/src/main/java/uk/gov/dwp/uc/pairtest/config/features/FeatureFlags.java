package uk.gov.dwp.uc.pairtest.config.features;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Jesus Binds feature toggles from application properties to strongly-typed configuration beans.
 * <p>
 * Each supported feature toggle (oauth2, configserver, circuitbreaker, ratelimiter, monitoring)
 * is mapped to a Toggle object for easy injection into conditional beans.
 *
 * Example (application.properties):
 * <pre>
 *   feature.oauth2.enabled=true
 *   feature.circuitbreaker.enabled=false
 * </pre>
 *
 * Use {@code @Autowired FeatureFlags} to inject and check feature states in other configs.
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-09-20
 *
 * Copyright: (c) 2025, UK Government
 */
@Configuration
@ConfigurationProperties(prefix = "feature")
public class FeatureFlags {
    private Toggle oauth2 = new Toggle();
    private Toggle configserver = new Toggle();
    private Toggle circuitbreaker = new Toggle();
    private Toggle ratelimiter = new Toggle();
    private Toggle monitoring = new Toggle();

    public Toggle getOauth2() { return oauth2; }
    public void setOauth2(Toggle oauth2) { this.oauth2 = oauth2; }

    public Toggle getConfigserver() { return configserver; }
    public void setConfigserver(Toggle configserver) { this.configserver = configserver; }

    public Toggle getCircuitbreaker() { return circuitbreaker; }
    public void setCircuitbreaker(Toggle circuitbreaker) { this.circuitbreaker = circuitbreaker; }

    public Toggle getRatelimiter() { return ratelimiter; }
    public void setRatelimiter(Toggle ratelimiter) { this.ratelimiter = ratelimiter; }

    public Toggle getMonitoring() { return monitoring; }
    public void setMonitoring(Toggle monitoring) { this.monitoring = monitoring; }

    public static class Toggle {
        private boolean enabled = false;
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
    }
}
