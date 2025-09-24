package uk.gov.dwp.uc.pairtest.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for InvalidTicketTypeException.
 * Ensures the constructor and inheritance are as expected for coverage.
 */

 /**
 * Jesus InvalidTicketTypeExceptionTest.java
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-08-21
 *
 * Copyright: (c) 2025, UK Government
 */
class InvalidTicketTypeExceptionTest {

    @Test
    void testConstructor() {
        InvalidTicketTypeException ex = new InvalidTicketTypeException("bad type");
        assertEquals("bad type", ex.getMessage());
        assertTrue(ex instanceof InvalidPurchaseException);
    }
}
