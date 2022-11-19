package com.example.recipebook.recipe_ingredient;

import com.example.recipebook.ingredient.Ingredient;
import com.example.recipebook.recipe.Recipe;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RecipeIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipe_id")
    @NotNull
    @JsonBackReference
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ingredient_id")
    @NotNull
    private Ingredient ingredient;

    @Column(nullable = false)
    @NotBlank
    private String quantity;

    public RecipeIngredient(Recipe recipe, Ingredient ingredient, String quantity) {
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.quantity = quantity;
    }
}
