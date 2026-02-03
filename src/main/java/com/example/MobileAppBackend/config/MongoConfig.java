package com.example.MobileAppBackend.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
public class MongoConfig {

    @Bean
    public MongoClient mongoClient() {
        Dotenv dotenv = Dotenv.load();
        String username = dotenv.get("MONGO_USERNAME");
        String password = dotenv.get("MONGO_PASSWORD");
        String host = dotenv.get("MONGO_URI");

        String encodedPassword = URLEncoder.encode(password, StandardCharsets.UTF_8);

        String uri = String.format(
                "mongodb+srv://%s:%s@%s/?retryWrites=true&w=majority&appName=Cluster0",
                username,
                encodedPassword,
                host
        );
        System.out.println("uri: " + uri);
        log.info("Connecting to MongoDB Atlas...");

        return MongoClients.create(uri);
    }
}