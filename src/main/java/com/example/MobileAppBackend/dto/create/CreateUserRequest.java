package com.example.MobileAppBackend.dto.create;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

@Data
@Slf4j
@ToString
public class CreateUserRequest {


    private String id;

    @NotBlank(message= "Username can't be empty")
    @Size(min=2, max=30, message="Username must be between 2 and 30 characters long")
    private String username;

    @NotBlank(message ="Email can't be empty")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message ="Password can't be empty")
    @Size(min=6, max=20, message = "Password must be between 6 and 20 characters long")
    private String password;

    private String avatar;
    private List<String> preferred_tags;
    private List<String> preferred_cuisine;
    private List<String> allergies;
    private List<Long> favorites;

}
