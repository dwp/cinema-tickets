package uk.gov.dwp.uc.pairtest.exception;

public class InvalidPurchaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * The default constructor.
     */
    public InvalidPurchaseException() {
    }

    /**
     * Constructor with a default error message.
     *
     * @param message The error message
     */
    public InvalidPurchaseException(final String message) {
        super(message);
    }

}
