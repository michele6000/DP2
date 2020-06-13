package it.polito.dp2.BIB.sol3.db;

public class ConflictInOperationException extends Exception {
  /**
   *
   */
  private static final long serialVersionUID = -4873551272256042781L;

  public ConflictInOperationException() {}

  public ConflictInOperationException(String message) {
    super(message);
  }

  public ConflictInOperationException(Throwable cause) {
    super(cause);
  }

  public ConflictInOperationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ConflictInOperationException(
    String message,
    Throwable cause,
    boolean enableSuppression,
    boolean writableStackTrace
  ) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
