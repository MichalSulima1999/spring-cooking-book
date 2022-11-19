package com.example.recipebook.recipe.dto;

import com.example.recipebook.step.Step;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StepNumberDto {
    private Step step;
    private int stepNumber;
}
