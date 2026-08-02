package com.authentication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomException> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        CustomException customException = new CustomException();
        customException.setError(errors);
        customException.setStatus(HttpStatus.BAD_REQUEST.value());
        customException.setTimestamp(LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(customException);
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CustomException> handleBadCredentials(
            BadCredentialsException ex) {

        return buildResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<CustomException> handleUserNotFound(
            UserNotFound ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomException> handleIllegalArgument(
            IllegalArgumentException ex) {

        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomException> handleException(Exception ex) {

        return buildResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    private ResponseEntity<CustomException> buildResponse(
            String message,
            HttpStatus status) {

        Map<String, String> errors = new HashMap<>();
        errors.put("message", message);

        CustomException customException = new CustomException();
        customException.setError(errors);
        customException.setStatus(status.value());
        customException.setTimestamp(LocalDateTime.now());

        return ResponseEntity
                .status(status)
                .body(customException);
    }
}