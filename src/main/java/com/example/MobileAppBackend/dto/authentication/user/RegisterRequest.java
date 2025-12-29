package com.example.MobileAppBackend.dto.authentication.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Slf4j
public class RegisterRequest {

    private String email;
    private String password;
    private String username;

}
