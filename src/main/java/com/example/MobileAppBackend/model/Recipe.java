package com.example.MobileAppBackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Slf4j
@Data
@RequiredArgsConstructor
@ToString
public class Recipe {


    private String title;
    private String author_id;
    private String description;
    private List<Ingredient> ingredients;
    private List<Step> steps;
    private List<String> tags;
    private String cuisine;
    private List<String> allergies;
    private String difficulty;
    private int prep_time;
    private int calories;

}
