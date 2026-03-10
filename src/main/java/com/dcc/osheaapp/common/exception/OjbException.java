package com.dcc.osheaapp.common.exception;

public class OjbException extends RuntimeException {

  private final ErrorCode errorCode;
  private final Object[] params;

  public OjbException(ErrorCode errorCode, Object[] params) {
    this.errorCode = errorCode;
    this.params = params;
  }

  public OjbException(String message, ErrorCode errorCode, Object[] params) {
    super(message);
    this.errorCode = errorCode;
    this.params = params;
  }

  public OjbException(String message, Throwable cause, ErrorCode errorCode, Object[] params) {
    super(message, cause);
    this.errorCode = errorCode;
    this.params = params;
  }

  public OjbException(Throwable cause, ErrorCode errorCode, Object[] params) {
    super(cause);
    this.errorCode = errorCode;
    this.params = params;
  }

  public OjbException(
      String message,
      Throwable cause,
      boolean enableSuppression,
      boolean writableStackTrace,
      ErrorCode errorCode,
      Object[] params) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.errorCode = errorCode;
    this.params = params;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }

  public Object[] getParams() {
    return params;
  }
}
