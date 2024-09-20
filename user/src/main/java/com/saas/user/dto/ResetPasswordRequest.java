package com.saas.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResetPasswordRequest {

    @JsonProperty("userEmail")
    private String userEmail;

    @JsonProperty("submittedOtp")
    private String submittedOtp;

    @JsonProperty("newPassword")
    private String newPassword;

    // Default constructor for JSON parsing
    public ResetPasswordRequest() {
    }

    // Constructor with all fields
    public ResetPasswordRequest(String userEmail, String submittedOtp, String newPassword) {
        this.userEmail = userEmail;
        this.submittedOtp = submittedOtp;
        this.newPassword = newPassword;
    }

    // Getters and setters
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getSubmittedOtp() {
        return submittedOtp;
    }

    public void setSubmittedOtp(String submittedOtp) {
        this.submittedOtp = submittedOtp;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    // toString method for logging purposes
    @Override
    public String toString() {
        return "ResetPasswordRequest{" +
                "userEmail='" + userEmail + '\'' +
                ", submittedOtp='" + submittedOtp + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}