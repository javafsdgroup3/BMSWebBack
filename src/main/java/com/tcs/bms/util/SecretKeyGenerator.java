package com.tcs.bms.util;

import java.security.SecureRandom;
import java.util.Base64;

public class SecretKeyGenerator {
    public static void main(String[] args) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        String secretKey = Base64.getEncoder().encodeToString(bytes);
        System.out.println("Secret Key: " + secretKey);
    }
}

