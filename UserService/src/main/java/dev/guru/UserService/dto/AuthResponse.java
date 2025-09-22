package dev.guru.UserService.dto;

public class AuthResponse
{
    private boolean authenticated;
    private String message;
    private String token;

    public AuthResponse(){

    }

    public AuthResponse(Builder builder){
        this.authenticated = builder.authenticated;
        this.message = builder.message;
        this.token = builder.token;
    }

    public static class Builder
    {
        private boolean authenticated;
        private String message;
        private String token;

        public Builder withAuthenticated(boolean authenticated)
        {
            this.authenticated = authenticated;
            return this;
        }

        public Builder withMessage(String message)
        {
            this.message = message;
            return this;
        }

        public Builder withToken(String token)
        {
            this.token = token;
            return this;
        }

        public AuthResponse build()
        {
            return new AuthResponse(this);
        }
    }

    public boolean isAuthenticated()
    {
        return authenticated;
    }
    public String getMessage()
    {
        return message;
    }
    public String getToken()
    {
        return token;
    }
}
