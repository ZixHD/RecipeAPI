package com.example.MobileAppBackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "postRecipes")
@Slf4j
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostRecipe {

    @Id
    private String id;

    private String authorId;
    private List<Rating> ratings;

    // Recipe part
    private String title;
    private String description;
    private List<Ingredient> ingredients;
    private List<Step> steps;
    private List<String> tags;
    private String cuisine;
    private List<String> allergies;
    private String difficulty;
    private int prep_time;
    private int calories;

    private String text;
    private int views = 0;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
