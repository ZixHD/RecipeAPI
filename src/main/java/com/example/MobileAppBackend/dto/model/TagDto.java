package com.example.MobileAppBackend.dto.model;


import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Slf4j
public class TagDto {

    private String id;
    @NotBlank
    private String name;
}
