package com.shopping.product.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleErrorResponse(MethodArgumentNotValidException ex,
                                                             HttpServletRequest request){

        List<String> errors=ex.getBindingResult().getFieldErrors().stream().map(
                error->error.getField() + ": " + error.getDefaultMessage()
        ).toList();
        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message("Invalid request data")
                .errorCode("VALIDATION_ERROR")
                .details(errors)
                .path(request.getRequestURL().toString())
                .timestamp(LocalDateTime.now()).build();
        return ResponseEntity.badRequest().body(response);
    }


    @ExceptionHandler(DataNotFound.class)
    public ResponseEntity<ErrorResponse> handleDataNotFound(
            DataNotFound ex, HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setError("Not Found");
        error.setMessage(ex.getMessage());
        error.setErrorCode("DATA_NOT_FOUND");
        error.setPath(request.getRequestURI());
        error.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEntry(
            DuplicateResourceException ex, HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse();
        error.setStatus(HttpStatus.CONFLICT.value());
        error.setError("Conflict");
        error.setMessage(ex.getMessage());
        error.setErrorCode("DUPLICATE_RESOURCE");
        error.setPath(request.getRequestURI());
        error.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }


}
