package com.authentication.records.login;

import java.util.List;

public record LoginResponse(
        Long userId,
        String fullName,
        String message,
        String jwtToken

) {
}
