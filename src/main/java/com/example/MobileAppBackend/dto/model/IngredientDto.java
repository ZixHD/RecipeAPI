package com.example.MobileAppBackend.dto.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class IngredientDto {

    @NotBlank(message = "Ingredient must have a name.")
    @Size(min=3, max = 50, message = "Ingredient must be between 3 and 50 characters long.")
    private String name;

    @NotBlank(message = "Quantity must be set.")
    @Size(min=2, max = 50, message = "Quantity must be between 2 and 50 characters long.")
    private String quantity;

}
