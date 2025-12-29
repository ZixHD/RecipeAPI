package com.example.MobileAppBackend.dto.authentication.client;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ClientRegisterRequestDto {

    private String email;
    private String username;
    private String password;

}
