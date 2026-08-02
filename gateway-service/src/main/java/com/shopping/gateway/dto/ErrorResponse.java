package com.shopping.gateway.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {

    private boolean success;

    private int status;

    private String errorCode;

    private String message;

    private LocalDateTime timestamp;
}