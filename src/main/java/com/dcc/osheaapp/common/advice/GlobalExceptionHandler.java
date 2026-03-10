package com.dcc.osheaapp.common.advice;

import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.common.model.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @Autowired private MessageSource messageSource;

  @ExceptionHandler(AccessDeniedException.class)
  protected ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException ex) {
    ex.printStackTrace();
    return new ResponseEntity<ApiResponse>(
        new ApiResponse(401, "UNAUTHORIZED", ex.getMessage(), null), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(ExpiredJwtException.class)
  protected ResponseEntity<ApiResponse> handleExpiredJwtException(ExpiredJwtException ex) {
    ex.printStackTrace();
    return new ResponseEntity<ApiResponse>(
        new ApiResponse(401, "EXPIRED_TOKEN", ex.getMessage(), null), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ApiResponse> handleGenericException(Exception ex) {
    ex.printStackTrace();
    return new ResponseEntity<ApiResponse>(
        new ApiResponse(500, "INTERNAL_SERVER_ERROR", "Something went wrong.", null),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  protected ResponseEntity<ApiResponse> handleIllegalArgumentException(
      IllegalArgumentException ex) {
    ex.printStackTrace();
    return new ResponseEntity<ApiResponse>(
        new ApiResponse(400, "BAD_REQUEST", ex.getMessage(), null), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(OjbException.class)
  protected ResponseEntity<ApiResponse> handleOjbException(
      OjbException ex, HttpServletRequest request) {
    ex.printStackTrace();
    String message =
        messageSource.getMessage(ex.getErrorCode().name(), ex.getParams(), request.getLocale());
    return new ResponseEntity<ApiResponse>(
        new ApiResponse(500, ex.getErrorCode().name(), message), HttpStatus.INTERNAL_SERVER_ERROR);
  }
  //    @ExceptionHandler(OjbException.class)
  //    protected ResponseEntity<ApiResponse> handleNoDataFoundException(OjbException ex,
  // HttpServletRequest request) {
  //        ex.printStackTrace();
  //        String message = messageSource.getMessage(ex.getErrorCode().name(), ex.getParams(),
  // request.getLocale());
  //        return new ResponseEntity<ApiResponse>(
  //                new ApiResponse(404, ex.getErrorCode().name(), message) , HttpStatus.NOT_FOUND);
  //    }
}
