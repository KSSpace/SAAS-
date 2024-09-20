package com.saas.user.utils;

public enum OtpPurpose {
    REGISTRATION("Registration"),
    PASSWORD_RECOVERY("Password Recovery");

    private final String description;

    OtpPurpose(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}