package com.example.MobileAppBackend.dto.create;

import com.example.MobileAppBackend.dto.model.IngredientDto;
import com.example.MobileAppBackend.dto.model.RatingDto;
import com.example.MobileAppBackend.dto.model.StepDto;
import com.example.MobileAppBackend.model.Ingredient;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
@Slf4j
public class CreatePostRequest {


    @NotBlank(message = "Post must have an author id.")
    private String authorId;

    @NotBlank(message="Text cannot be empty.")
    @Size(min = 5, max = 100, message = "Post text must be between 5 and 50 characters long.")
    private String text;

    @NotBlank(message = "Recipe must have a title.")
    @Size(min = 5, max = 100, message = "Recipe title must be between 5 and 100 characters long.")
    private String title;

    @NotNull(message = "Recipe must have a list of ingredients.")
    private List<IngredientDto> ingredients;

    @NotNull(message = "Recipe must have a list of steps.")
    private List<StepDto> steps;

    @NotBlank(message = "Description must not be empty.")
    private String description;

    @NotNull(message = "Recipe must have tags defined.")
    private List<String> tags;

    @NotBlank(message = "Recipe must have a define cuisine.")
    private String cuisine;

    @NotNull(message = "Recipe must have a allergies defined.")
    private List<String> allergies;

    @NotBlank(message = "Recipe must have a set difficulty rating.")
    private String difficulty;

    @NotNull(message = "Recipe must have preparation time set.")
    private int prep_time;

    @NotNull(message = "Recipe must have defined calories.")
    private int calories;


}
