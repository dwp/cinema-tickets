package uk.gov.dwp.uc.pairtest.config.features;

import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.Configuration;

/**
 * Jesus Configuration class for enabling circuit breaker support via Resilience4j in the cinema tickets application.
 * <p>
 * This class is conditionally loaded only when the circuit breaker feature toggle is set to true.
 * <ul>
 *   <li>Requires the presence of the Resilience4j CircuitBreaker annotation on the classpath.</li>
 *   <li>Acts as an extension point: add global circuit breaker beans or configuration properties here.</li>
 *   <li>Clients and external connectors should provide their own <b>@CircuitBreaker</b> annotations.</li>
 * </ul>
 * </p>
 *
 * Example usage (when enabled):
 * <code>
 *   @CircuitBreaker(name = "defaultBreaker", fallbackMethod = "fallbackMethod")
 *   public String callExternal() {...}
 * </code>
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-09-20
 *
 * Copyright: (c) 2025, UK Government
 */
@Configuration
@ConditionalOnProperty(name = "feature.circuitbreaker.enabled", havingValue = "true")
@ConditionalOnClass(name = "io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker")
public class CircuitBreakerConfig {
    // Extend here to add any shared circuit breaker beans/configuration
}
