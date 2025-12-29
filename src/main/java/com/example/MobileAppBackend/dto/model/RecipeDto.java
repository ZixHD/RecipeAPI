package com.example.MobileAppBackend.dto.model;

import com.example.MobileAppBackend.model.Ingredient;
import com.example.MobileAppBackend.model.Step;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Slf4j
public class RecipeDto {

    private String title;
    private String authorId;
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
