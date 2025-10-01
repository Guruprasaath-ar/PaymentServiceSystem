package dev.guru.WalletService.dto;

public class UserResponse {


    private long userId;
    private boolean result;
    private String message;

    public UserResponse(){

    }

    public UserResponse(Builder builder) {
        this.result = builder.result;
        this.message = builder.message;
        this.userId = builder.userId;
    }

    public boolean isResult() {
        return result;
    }

    public Long getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public static class Builder{
        private boolean result;
        private String message;
        private Long userId;

        public Builder (){
        }

        public Builder withUserId(Long userId){
            this.userId = userId;
            return this;
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
