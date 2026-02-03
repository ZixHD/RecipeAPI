package com.example.MobileAppBackend.repository;


import com.example.MobileAppBackend.model.PostRecipe;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRecipeRepository extends MongoRepository<PostRecipe, String> {

    PostRecipe findPostById(String id);

}
