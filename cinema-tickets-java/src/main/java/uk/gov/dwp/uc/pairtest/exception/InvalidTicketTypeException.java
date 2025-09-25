/**
 * Jesus Exception thrown when an invalid or unrecognized ticket type is supplied
 * in a ticket purchase request.
 *
 * Extends {@link InvalidPurchaseException} for consistent client error handling.
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-09-20
 *
 * Copyright: (c) 2025, UK Government
 */
package uk.gov.dwp.uc.pairtest.exception;

public class InvalidTicketTypeException extends InvalidPurchaseException {
    public InvalidTicketTypeException(String message) {
        super(message);
    }
}
