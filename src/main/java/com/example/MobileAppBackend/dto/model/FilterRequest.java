package com.example.MobileAppBackend.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FilterRequest {

    //TODO: maybe move this to POSTS

    //preporuceno (default)
//    private boolean sortByNewest;
//    private boolean sortByOldest;
//    private boolean sortByPopularity;
//    private boolean sortByPrepTime;

    private Integer minCalories;
    private Integer maxCalories;
    //optional
    private Integer minPrepTime;
    private Integer maxPrepTime;

    private List<String> allergies;
    private List<String> tags;

    private String difficulty;
    private String cuisine;
}
