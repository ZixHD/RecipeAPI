package com.example.MobileAppBackend.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MongoConfig {

    @Bean
    public MongoClient mongoClient() {
        Dotenv dotenv = Dotenv.load();
        String username = dotenv.get("MONGO_USERNAME");
        String password = dotenv.get("MONGO_PASSWORD");
        String host = dotenv.get("MONGO_URI");
        String database = dotenv.get("MONGO_DB");

        String options = "retryWrites=true&w=majority&appName=Cluster0";

        String uri = String.format(
                "mongodb+srv://%s:%s@%s/%s?%s",
                username,
                password,
                host,
                database,
                options
        );

        log.info("uri: {}", uri);
        return MongoClients.create(uri);
    }
}