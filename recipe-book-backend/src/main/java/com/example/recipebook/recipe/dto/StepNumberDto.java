package com.example.recipebook.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StepNumberDto {
    private String stepDescription;
    private int stepNumber;
}
