package com.authentication.dto.register;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {

    @NotEmpty(message = "Please enter fullName")
    @Size(min = 2, max = 50, message = "Full name must be between 2 and 50 characters")
    private String fullName;
    @NotEmpty(message = "Please enter email")
    @Email(message = "Please enter correct email")
    private String email;
    @NotEmpty(message = "Please create password")
    @Size(min = 8, max = 32, message = "Password must be between 8 and 32 characters")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
            message = "Password must contain at least one digit, one lowercase, one uppercase, and one special character"
    )
    private String createPassword;

}
