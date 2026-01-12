package com.shopping.inventory.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {

    private int status;
    private String error;
    private String message;
    private String errorCode;          // App-specific code
    private List<String> details;      // Validation errors
    private String path;
    private LocalDateTime timestamp;
}
