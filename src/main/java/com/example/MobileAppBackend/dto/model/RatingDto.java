package com.example.MobileAppBackend.dto.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Slf4j
public class RatingDto {


    @NotBlank(message = "Rating must have user id.")
    private String userId;

    @NotNull(message = "Rating must have a score.")
    private int score;


}
