package uk.gov.dwp.uc.pairtest.exception;

public class InvalidPurchaseException extends RuntimeException {

  public InvalidPurchaseException(String message) {
    super(message);
  }
}
