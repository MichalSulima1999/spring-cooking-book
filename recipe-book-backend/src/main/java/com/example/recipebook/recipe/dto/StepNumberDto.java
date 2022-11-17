package com.example.recipebook.recipe.dto;

import com.example.recipebook.step.Step;
import lombok.Data;

@Data
public class StepNumberDto {
    private Step step;
    private int stepNumber;
}
