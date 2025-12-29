package com.example.MobileAppBackend.repository;

import com.example.MobileAppBackend.model.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TagRepository extends MongoRepository<Tag, String> {

    boolean existsTagByName(String name);
}
