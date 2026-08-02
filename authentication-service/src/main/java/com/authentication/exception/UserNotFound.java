package com.authentication.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserNotFound extends RuntimeException {
    public UserNotFound(String message)
    {
        super(message);
    }
}
