package com.authentication.dto.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest {
    @Email(message = "Please enter correct email id")
    @NotEmpty(message = "Please enter email id")
    private String email;
    @NotEmpty(message = "Please enter password")
    private String password;
}
