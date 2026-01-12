package com.example.MobileAppBackend.config;


import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

@Service
public class ApiKeyService {

    public String generateApiKey(){
        return UUID.randomUUID().toString();
    }

    public String generateApiSecret(String apiKey, String password){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String combined = password + ":" + apiKey;
            byte[] hash = digest.digest(combined.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating API secret", e);
        }
    }

}
