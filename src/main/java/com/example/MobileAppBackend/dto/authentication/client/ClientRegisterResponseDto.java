package com.example.MobileAppBackend.dto.authentication.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ClientRegisterResponseDto {

    private String apiKey;
    private String apiSecret;
    private String message;
}
