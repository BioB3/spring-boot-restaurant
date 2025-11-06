package th.ac.ku.restaurant.controller;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(
    MethodArgumentNotValidException ex
  ) {
    Map<String, String> errors = new HashMap<>();
    ex
      .getBindingResult()
      .getFieldErrors()
      .forEach(error ->
        errors.put(error.getField(), error.getDefaultMessage())
      );

    logger.error("JSON Input Error: {}", errors);

    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<String> handleEntityNotFoundException(
    EntityNotFoundException ex
  ) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(EntityExistsException.class)
  public ResponseEntity<String> handleEntityExistsException(
    EntityExistsException ex
  ) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(SecurityException.class)
  public void handleSecurityException(SecurityException ex) {
    logger.error("Invalid JWT signature: " + ex.getMessage());
  }

  @ExceptionHandler(MalformedJwtException.class)
  public void handleMalformedJwtException(MalformedJwtException ex) {
    logger.error("Invalid JWT token: " + ex.getMessage());
  }

  @ExceptionHandler(ExpiredJwtException.class)
  public void handleExpiredJwtException(ExpiredJwtException ex) {
    logger.error("JWT token is expired: " + ex.getMessage());
  }

  @ExceptionHandler(UnsupportedJwtException.class)
  public void handleUnsupportedJwtException(UnsupportedJwtException ex) {
    logger.error("JWT token is unsupported: " + ex.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public void handleIllegalArgumentException(IllegalArgumentException ex) {
    logger.error("JWT claims string is empty: " + ex.getMessage());
  }

  @ExceptionHandler(ServletException.class)
  public void handleServletException(ServletException ex) {
    logger.error("Cannot set user authentication: " + ex);
  }

  @ExceptionHandler(IOException.class)
  public void handleIOException(IOException ex) {
    logger.error("Cannot set user authentication: " + ex);
  }
}
