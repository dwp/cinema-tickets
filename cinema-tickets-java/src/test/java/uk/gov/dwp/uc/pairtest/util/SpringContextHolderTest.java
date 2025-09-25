package uk.gov.dwp.uc.pairtest.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SpringContextHolderTest {

    @Test
    void testSetBean_and_GetBean_returnsSetBean() {
        String bean = "SomeTestBean";
        SpringContextHolder.setBean(String.class, bean);
        assertEquals(bean, SpringContextHolder.getBean(String.class));
    }

    @Test
    void testGetBean_returnsContextBeanIfNotOverridden() {
        // Clear overrides so it will fall through to context.getBean
        SpringContextHolder.clearTestBeans();
        assertThrows(Exception.class, () -> SpringContextHolder.getBean(Object.class));
    }

    @Test
    void testClearTestBeansRemovesOverrides() {
        String bean = "AnotherBean";
        SpringContextHolder.setBean(String.class, bean);
        SpringContextHolder.clearTestBeans();
        // Should now fall-through and, unless the context provides a bean, probably throws
        assertThrows(Exception.class, () -> SpringContextHolder.getBean(String.class));
    }
}