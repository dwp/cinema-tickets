package uk.gov.dwp.uc.pairtest.exception;

public class InvalidPurchaseException extends RuntimeException {

  private final int statusCode;
  private final String exceptionMessage;

  public InvalidPurchaseException(int statusCode, String message) {
    this.statusCode = statusCode;
    this.exceptionMessage = message;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public String getExceptionMessage() {
    return exceptionMessage;
  }
}
