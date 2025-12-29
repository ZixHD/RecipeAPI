package com.example.MobileAppBackend.repository;

import com.example.MobileAppBackend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    List<User> findAll();

    Optional<User> findByUsername(String username);

    boolean existsUserByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByApiKeyAndApiKeyActiveTrue(String apiKey);
}
