package com.example.MobileAppBackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="users")
@Slf4j
@ToString
public class User {

    @Id
    private String id;

    private String username;
    private String email;
    private String password;
    private String avatar;
    private List<String> preferred_tags = new ArrayList<>();;
    private List<String> preferred_cuisine = new ArrayList<>();;
    private List<String> allergies = new ArrayList<>();
    private Set<String> favorites = new HashSet<>();

    private UserType userType;

    //Developer
    private String apiKey;
    private LocalDateTime apiKeyCreatedAt;
    private boolean apiKeyActive;



}
