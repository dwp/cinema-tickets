package uk.gov.dwp.uc.pairtest.exception;

/**
 * Jesus Exception thrown when any business rule for ticket purchase is violated within the application.
 * Used to signal invalid input, permissions, or logical errors related to purchase requests.
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-09-20
 *
 * Copyright: (c) 2025, UK Government
 */
public class InvalidPurchaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for InvalidPurchaseException class.
	 * @param message
	 */
	public InvalidPurchaseException(String message) {
        super(message);
    }

	/**
	 * Constructor for InvalidPurchaseException class with message and cause.
	 * 
	 * @param message
	 * @param cause
	 */
	public InvalidPurchaseException(String message, Throwable cause) {
		super(message, cause);
	}
}
