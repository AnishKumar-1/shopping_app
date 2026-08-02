package com.authentication.exception;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class CustomException {

    private Map<String, String> error;
    private LocalDateTime timestamp;

    private int status;
}
