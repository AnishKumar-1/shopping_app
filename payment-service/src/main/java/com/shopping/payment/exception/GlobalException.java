package com.shopping.payment.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class GlobalException {

    ErrorResponse error;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpServletRequest request){
        List<String> argError= exception.getBindingResult().getFieldErrors()
                .stream().map(error->error.getField() + ": " + error.getDefaultMessage()).toList();
        error= ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message("Invalid request data")
                .errorCode("VALIDATION_ERROR")
                .details(argError)
                .path(request.getRequestURL().toString())
                .timestamp(LocalDateTime.now()).build();
        return ResponseEntity.badRequest().body(error);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadableEnuError(HttpMessageNotReadableException exception, HttpServletRequest request){
        error= ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message("Invalid request data")
                .errorCode("VALIDATION_ERROR")
                .details(List.of("Invalid payment method. Allowed values: UPI, CARD, NET_BANKING"))
                .path(request.getRequestURL().toString())
                .timestamp(LocalDateTime.now()).build();
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(InvalidOrderStateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOrderStateException(InvalidOrderStateException exception, HttpServletRequest request){
        error= ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .error("Bad Request")
                .message("Invalid request data")
                .errorCode("VALIDATION_ERROR")
                .details(Collections.singletonList(exception.getLocalizedMessage()))
                .path(request.getRequestURL().toString())
                .timestamp(LocalDateTime.now()).build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException exception, HttpServletRequest request){
        error= ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error("Bad Request")
                .message("Invalid request data")
                .errorCode("VALIDATION_ERROR")
                .details(Collections.singletonList(exception.getLocalizedMessage()))
                .path(request.getRequestURL().toString())
                .timestamp(LocalDateTime.now()).build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException exception, HttpServletRequest request){
        error= ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message("Invalid request data")
                .errorCode("VALIDATION_ERROR")
                .details(Collections.singletonList(exception.getLocalizedMessage()))
                .path(request.getRequestURL().toString())
                .timestamp(LocalDateTime.now()).build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


}
