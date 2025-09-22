package dev.guru.UserService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequest {
    @Email
    private String email;
    @NotBlank(message = "username cannot be empty")
    private String username;
    @Size(min = 8, max = 16, message = "password length should be 8-16 characters")
    private String password;

    public UserRequest() {
    }

    public UserRequest(Builder builder) {
        this.email = builder.email;
        this.username = builder.username;
        this.password = builder.password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }


    public static class Builder {
        private String email;
        private String username;
        private String password;

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public UserRequest build() {
            return new UserRequest(this);
        }
    }
}
