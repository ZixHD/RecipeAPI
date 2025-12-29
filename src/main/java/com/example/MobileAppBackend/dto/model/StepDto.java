package com.example.MobileAppBackend.dto.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class StepDto {

    @NotNull(message = "Step number must be set.")
    private int stepNumber;

    @NotBlank(message = "Step must have an instruction.")
    @Size(min = 5, max = 100, message = "Instruction must be between 5 and 100 characters long.")
    private String instruction;

    @NotBlank(message = "Step must have media set.")
    private String media;
}
