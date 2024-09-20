package com.saas.user.utils;

import java.security.SecureRandom;

public class NonceUtils {

    public static String generateNonce() {
        SecureRandom random = new SecureRandom();
        byte[] nonce = new byte[16];
        random.nextBytes(nonce);
        return SecurityUtils.bytesToHex(nonce);
    }
}
