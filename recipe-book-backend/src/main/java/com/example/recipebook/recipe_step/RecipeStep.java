package com.example.recipebook.recipe_step;

import com.example.recipebook.recipe.Recipe;
import com.example.recipebook.step.Step;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RecipeStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipe_id")
    @NotNull
    @JsonBackReference
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "step_id")
    @NotNull
    private Step step;

    @Column(nullable = false)
    @Min(1)
    private int stepNumber;

    public RecipeStep(Recipe recipe, Step step, int stepNumber) {
        this.recipe = recipe;
        this.step = step;
        this.stepNumber = stepNumber;
    }
}
