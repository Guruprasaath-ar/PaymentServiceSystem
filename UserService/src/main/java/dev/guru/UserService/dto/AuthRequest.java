package dev.guru.UserService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class AuthRequest {
    @Email
    private String email;
    @NotEmpty(message = "password cannot be empty")
    private String password;

    public AuthRequest(){

    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
