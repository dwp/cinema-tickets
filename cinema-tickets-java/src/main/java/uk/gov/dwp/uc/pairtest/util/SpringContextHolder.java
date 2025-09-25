/**
*  Jesus SpringContextHolder provides static access to Spring-managed beans anywhere in application code,
 * with support for test overrides for unit/integration testing scenarios.
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-08-22
 *
 * Copyright: (c) 2025, UK Government
 */
package uk.gov.dwp.uc.pairtest.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Holder to access Spring Beans anywhere in the code.
 * Utility to get Spring beans programmatically.
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-09-20
 *
 * Copyright: (c) 2025, UK Government
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext context;    
    private static final Map<Class<?>, Object> testBeans = new HashMap<>();

    /**
     * setter method for application context
     */
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        context = applicationContext;
    }

    /**
     * Get bean from Spring context or test override
     * 
     * @param <T>
     * @param clazz
     * @return requested bean if available.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        if (testBeans.containsKey(clazz)) {
            return (T) testBeans.get(clazz);
        }
        return context.getBean(clazz);
    }

    /**
     * Method to set the bean
     * 
     * @param <T>
     * @param clazz
     * @param bean
     */
    public static <T> void setBean(Class<T> clazz, T bean) {
        testBeans.put(clazz, bean);
    }
    
    /**
     * Clear test overrides
     */
    public static void clearTestBeans() {
        testBeans.clear();
    }
    
}
