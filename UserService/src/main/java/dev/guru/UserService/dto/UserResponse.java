package dev.guru.UserService.dto;

import org.springframework.stereotype.Component;

@Component
public class UserResponse {
    private boolean result;
    private String message;

    public UserResponse(){

    }

    public UserResponse(Builder builder) {
        this.result = builder.result;
        this.message = builder.message;
    }

    public boolean isResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public static class Builder{
        private boolean result;
        private String message;

        public Builder (){
        }

        public Builder withResult(boolean result) {
            this.result = result;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public UserResponse build(){
            return new UserResponse(this);
        }
    }
}
