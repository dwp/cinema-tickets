package uk.gov.dwp.uc.pairtest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Jesus Global maintenance toggle to short-circuit all requests with 503.
 * Disabled by default (app.maintenance.enabled=false).
 * 
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-09-20
 *
 * Copyright: (c) 2025, UK Government
 */
@Configuration
public class MaintenanceConfig implements WebMvcConfigurer {

    @Value("${app.maintenance.enabled:false}")
    private boolean maintenanceEnabled;

    @Value("${app.maintenance.message:Service under maintenance}")
    private String maintenanceMessage;

    /**
     * Method to add Interceptors
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (maintenanceEnabled) {
            registry.addInterceptor(new org.springframework.web.servlet.HandlerInterceptor() {
                @Override
                public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
                    writeMaintenance(response);
                    return false;
                }
            });
        }
    }

    private void writeMaintenance(HttpServletResponse response) throws IOException {
        response.setStatus(503);
        response.getWriter().write(maintenanceMessage);
    }
}
