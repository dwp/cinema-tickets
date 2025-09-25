package uk.gov.dwp.uc.pairtest.external;

import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.stereotype.Service;

/**
 * Jesus Example external client simulating integration with a downstream seat reservation service.
 * <p>
 * Demonstrates conditional bean registration and (optionally) the use of Resilience4j CircuitBreaker.
 * Only active if <b>feature.circuitbreaker.enabled=true</b> and Resilience4j circuitbreaker annotation is present on the classpath.
 * </p>
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-09-20
 *
 * Copyright: (c) 2025, UK Government
 */
@Service
@ConditionalOnProperty(name = "feature.circuitbreaker.enabled", havingValue = "true")
@ConditionalOnClass(name = "io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker")
public class ExternalSeatGatewayClient {

    //@CircuitBreaker(name = "defaultBreaker", fallbackMethod = "fallback")
    public String callExternal() {
        // Simulate failure for demo
        throw new RuntimeException("Simulated downstream failure");
    }

    public String fallback(Throwable t) {
        return "fallback-ok";
    }
}
