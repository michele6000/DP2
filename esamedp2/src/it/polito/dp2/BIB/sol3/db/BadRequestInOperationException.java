package it.polito.dp2.BIB.sol3.db;

public class BadRequestInOperationException extends Exception {
  /**
   *
   */
  private static final long serialVersionUID = 2753371731266430333L;

  public BadRequestInOperationException() {}

  public BadRequestInOperationException(String message) {
    super(message);
  }

  public BadRequestInOperationException(Throwable cause) {
    super(cause);
  }

  public BadRequestInOperationException(String message, Throwable cause) {
    super(message, cause);
  }

  public BadRequestInOperationException(
    String message,
    Throwable cause,
    boolean enableSuppression,
    boolean writableStackTrace
  ) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
