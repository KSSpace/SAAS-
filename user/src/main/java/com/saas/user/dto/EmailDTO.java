package com.saas.user.dto;

public class EmailDTO {
    private String email;

    // Default constructor for JSON parsing
    public EmailDTO() {
    }

    // Constructor with parameter
    public EmailDTO(String email) {
        this.email = email;
    }

    // Getter and setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
