package com.example.MobileAppBackend.config;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ApiKeyService {

    public String generateApiKey(){
        return UUID.randomUUID().toString();
    }
}
