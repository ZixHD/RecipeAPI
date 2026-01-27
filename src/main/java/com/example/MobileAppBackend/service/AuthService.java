package com.example.MobileAppBackend.service;

import com.example.MobileAppBackend.config.ApiKeyService;
import com.example.MobileAppBackend.config.JwtService;
import com.example.MobileAppBackend.dto.authentication.client.ClientRegisterRequestDto;
import com.example.MobileAppBackend.dto.authentication.client.ClientRegisterResponseDto;
import com.example.MobileAppBackend.dto.authentication.user.LoginRequest;
import com.example.MobileAppBackend.dto.authentication.user.RegisterRequest;
import com.example.MobileAppBackend.model.User;
import com.example.MobileAppBackend.model.UserType;
import com.example.MobileAppBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ApiKeyService apiKeyService;
    private final PasswordEncoder passwordEncoder;

    protected boolean verifyPassword(String rawPassword, String hashedPassword) {
        if (rawPassword == null || hashedPassword == null) return false;
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }

    public void register(RegisterRequest registerRequest) throws IllegalArgumentException{
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            log.warn("Email already exists");
            throw new RuntimeException("Email already in use");
        }


        String hashedPassword = passwordEncoder.encode(registerRequest.getPassword());


        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(hashedPassword);
        user.setUsername(registerRequest.getUsername());
        user.setUserType(UserType.USER);

        userRepository.save(user);
        log.debug("Created user {}", user);
        log.info("User registered successfully");
    }

    public String login(LoginRequest loginRequest) throws IllegalArgumentException {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();


        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if(user != null && verifyPassword(password, user.getPassword())) {
            log.info("User logged in successfully");
            return jwtService.generateToken(user.getId(), user.getUsername());
        }
        log.warn("Invalid credentials");
        throw new IllegalArgumentException("Invalid email or password");

    }

    public ClientRegisterResponseDto clientRegister(ClientRegisterRequestDto clientRegisterRequestDto) {
        if(userRepository.existsUserByEmail(clientRegisterRequestDto.getEmail())) {
            log.warn("Email already exists");
            throw new RuntimeException("Email already registered");
        }
        String generatedApiKey = apiKeyService.generateApiKey();
        User developer = new User();
        developer.setEmail(clientRegisterRequestDto.getEmail());
        developer.setUsername(clientRegisterRequestDto.getUsername());
        developer.setPassword(clientRegisterRequestDto.getPassword());
        developer.setUserType(UserType.CLIENT);
        developer.setApiKey(generatedApiKey);
        developer.setApiKeyActive(true);
        String privateKey = ApiKeyService.generateClientPrivatePublicKey(developer, userRepository);
        User savedDeveloper = userRepository.save(developer);

        ClientRegisterResponseDto clientRegisterResponseDto = new ClientRegisterResponseDto(
                savedDeveloper.getApiKey(),
                privateKey,
                "Client account created successfully. Save your API key and secret - they won't be shown again!"
        );
        log.debug("New user registered: {}", developer);
        return clientRegisterResponseDto;
    }

}
