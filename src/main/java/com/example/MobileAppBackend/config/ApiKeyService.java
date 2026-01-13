package com.example.MobileAppBackend.config;


import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

@Service
public class ApiKeyService {

    private static final SecureRandom  secureRandom = new SecureRandom();

    public String generateApiKey(){
        return UUID.randomUUID().toString();
    }

    public String generateApiSecret(){
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

}
