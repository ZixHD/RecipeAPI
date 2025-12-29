package com.example.MobileAppBackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Rating {

    private String userId;
    private int score;
}
