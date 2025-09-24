package uk.gov.dwp.uc.pairtest.config.features;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * Jesus Configuration class for integrating with Spring Cloud Config Server (externalized configuration management).
 * <p>
 * This class is loaded only when the config server feature toggle is enabled.
 * <ul>
 *   <li>Use when you want to support dynamic property updates (via {@code @RefreshScope}).</li>
 *   <li>Requires <b>spring-cloud-starter-config</b> (client) on the classpath for actual remote config refresh.</li>
 *   <li>By default, this class does not define any beans; extend as necessary to register refreshable components.</li>
 * </ul>
 * </p>
 *
 * Example usage (when enabled):
 * <code>
 *   @RefreshScope
 *   @Service
 *   public class MyDynamicService { ... }
 * </code>
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-09-20
 *
 * Copyright: (c) 2025, UK Government
 */
@Configuration
@RefreshScope
@ConditionalOnProperty(name = "feature.configserver.enabled", havingValue = "true")
public class ConfigServerConfig {
    // Extend as needed for custom beans or refreshable property holders
}
