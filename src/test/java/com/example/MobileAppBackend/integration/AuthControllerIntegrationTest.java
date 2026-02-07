package com.example.MobileAppBackend.integration;

import com.example.MobileAppBackend.dto.authentication.client.ClientRegisterRequestDto;
import com.example.MobileAppBackend.dto.authentication.user.LoginRequest;
import com.example.MobileAppBackend.dto.authentication.user.RegisterRequest;
import com.example.MobileAppBackend.model.User;
import com.example.MobileAppBackend.model.UserType;
import com.example.MobileAppBackend.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterUser_success() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("testuser@example.com");
        request.setUsername("testuser");
        request.setPassword("Password123");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());


        User savedUser = userRepository.findByEmail("testuser@example.com").orElse(null);
        assert savedUser != null;
        assert savedUser.getUserType() == UserType.USER;
    }

    @Test
    void testLoginUser_success() throws Exception {
        User user = new User();
        user.setEmail("loginuser@example.com");
        user.setUsername("loginuser");
        user.setUserType(UserType.USER);
        user.setPassword(passwordEncoder.encode("Password123"));
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("loginuser@example.com");
        loginRequest.setPassword("Password123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void testClientRegister_success() throws Exception {
        ClientRegisterRequestDto requestDto = new ClientRegisterRequestDto();
        requestDto.setEmail("client@example.com");
        requestDto.setUsername("clientuser");
        requestDto.setPassword("ClientPassword123");

        mockMvc.perform(post("/auth/client/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.apiKey").exists())
                .andExpect(jsonPath("$.privateKey").exists())
                .andExpect(jsonPath("$.message").value("Client account created successfully. Save your API key and secret - they won't be shown again!"));
    }

    @Test
    void testRegisterUser_duplicateEmail_shouldFail() throws Exception {
        User existingUser = new User();
        existingUser.setEmail("duplicate@example.com");
        existingUser.setUsername("existing");
        existingUser.setPassword("dummy");
        existingUser.setUserType(UserType.USER);
        userRepository.save(existingUser);

        RegisterRequest request = new RegisterRequest();
        request.setEmail("duplicate@example.com");
        request.setUsername("newuser");
        request.setPassword("Password123");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }
}
