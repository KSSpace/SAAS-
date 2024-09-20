package com.saas.user.utils;

public class OtpUtils {

    public static String generateOtp() {
        return String.valueOf(100000 + (int) (Math.random() * 900000)); // Generate a 6-digit OTP
    }
}
