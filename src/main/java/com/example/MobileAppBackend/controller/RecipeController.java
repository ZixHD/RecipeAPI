package com.example.MobileAppBackend.controller;

import com.example.MobileAppBackend.dto.model.FilterRequest;
import com.example.MobileAppBackend.dto.model.RecipeDto;
import com.example.MobileAppBackend.model.PostRecipe;
import com.example.MobileAppBackend.model.Recipe;
import com.example.MobileAppBackend.service.PostRecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final PostRecipeService postRecipeService;

    @GetMapping
    public ResponseEntity<?> getAllRecipes(@RequestParam(required = false) String exclude, @RequestParam(required = false) String include) {
        if (exclude != null && include != null) {
            return ResponseEntity
                    .badRequest()
                    .body("Only one parameter can be used: either 'include' or 'exclude', not both.");
        }

        if(exclude != null){
            return ResponseEntity.ok(postRecipeService.getSpecificRecipesExclude(exclude));
        } else if (include != null) {
            return ResponseEntity.ok(postRecipeService.getSpecificRecipesInclude(include));
        }
        return ResponseEntity.ok(postRecipeService.getAllRecipes());
    }


    //API
    @GetMapping("/filter")
    public ResponseEntity<List<RecipeDto>> filterRecipes(@RequestBody FilterRequest filterRequest){
        return ResponseEntity.ok(postRecipeService.filterRecipes(filterRequest));
    }





}
