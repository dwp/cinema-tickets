/**
 * Jesus FeatureTogglesEndpoint.java
 *
 * Custom Spring Boot actuator endpoint to view and mutate runtime feature toggles dynamically.
 * Enables in-memory enabling/disabling of features without restart. Properties are NOT persisted across restarts.
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-08-24
 *
 * Copyright: (c) 2025, UK Government
 */
package uk.gov.dwp.uc.pairtest.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;

/**
 * Exposes a /actuator/feature-toggles endpoint for on-the-fly feature switch control.
 * Supported toggles include all properties with feature.* prefix; toggles override static configs at runtime.
 * All changes are non-persistent and affect only the running JVM instance.
 * 
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-09-20
 *
 * Copyright: (c) 2025, UK Government
 */
@Component
@Endpoint(id = "feature-toggles")
@RequiredArgsConstructor
public class FeatureTogglesEndpoint {

    private static final String RUNTIME_SOURCE = "runtimeFeatureToggles";
    private final ConfigurableEnvironment environment;

    /**
     * Returns a snapshot of all feature.* properties (runtime + static) and current time.
     * @return map of toggles and system timestamp
     */
    @ReadOperation
    public Map<String, Object> read() {
        Map<String, Object> out = new LinkedHashMap<>();
        Properties merged = collectAllFeatureProps();
        for (String name : merged.stringPropertyNames()) {
            out.put(name, merged.getProperty(name));
        }
        out.put("timestamp", Instant.now().toString());
        return out;
    }

    /**
     * Sets/overrides a single feature toggle (feature.* property) at runtime.
     * @param key   feature key (e.g., feature.something.enabled)
     * @param value new value (true/false or any string)
     * @return status/result map
     */
    @WriteOperation
    public Map<String, Object> write(String key, String value) {
        if (key == null || key.isBlank()) {
            return Map.of("error", "key must be provided");
        }
        MutablePropertySources sources = environment.getPropertySources();
        PropertiesPropertySource runtime = (PropertiesPropertySource) sources.get(RUNTIME_SOURCE);
        if (runtime == null) {
            runtime = new PropertiesPropertySource(RUNTIME_SOURCE, new Properties());
            sources.addFirst(runtime);
        }

        Object src = runtime.getSource();
        Properties props;
        if (src instanceof Properties) {
            props = (Properties) src;
        } else if (src instanceof Map) {
            // Copy map values into a new Properties obj
            props = new Properties();
            ((Map<?, ?>) src).forEach((k, v) -> props.put(k, v));
        } else {
            throw new IllegalStateException("Unexpected property source type: " + src.getClass());
        }

        props.put(key, value);

        return Map.of(
                "key", key,
                "value", value,
                "timestamp", Instant.now().toString(),
                "note", "This change is in-memory and non-persistent; it overrides static properties while app is running."
        );
    }

    /**
     * Scans all available Environment property sources and collects feature.* keys.
     * @return merged feature properties from all sources
     */
    private Properties collectAllFeatureProps() {
        Properties collected = new Properties();
        environment.getPropertySources().forEach(ps -> {
            Object src = ps.getSource();
            if (src instanceof Map<?, ?> map) {
                map.forEach((k, v) -> {
                    if (k instanceof String name && name.startsWith("feature.")) {
                        collected.setProperty(name, String.valueOf(v));
                    }
                });
            }
        });
        return collected;
    }
}
