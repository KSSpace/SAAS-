package com.saas.user.dto;

public class LoginRequest {

    // @NotBlank(message = "Username or Email cannot be blank")
    private String usernameOrEmail;

    // @NotBlank(message = "Password cannot be blank")
    private String password;

    // No-arg constructor
    public LoginRequest() {
    }

    // All-args constructor
    public LoginRequest(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }

    // Getters and setters
    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Optional: Override toString method for logging purposes
    @Override
    public String toString() {
        return "LoginRequest{" +
                "usernameOrEmail='" + usernameOrEmail + '\'' +
                ", password='[PROTECTED]'" + // Never log passwords
                '}';
    }
}