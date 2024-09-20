package com.saas.user.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;

public class EmailRecoveryRequest {

    private String email;

    // Default constructor for Jackson
    public EmailRecoveryRequest() {
    }

    // Constructor with @JsonCreator and @JsonProperty annotations
    @JsonCreator
    public EmailRecoveryRequest(@JsonProperty("email") String email) {
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


