package com.example.recipebook.step;

import com.example.recipebook.recipe_step.RecipeStep;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Step {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "step", cascade = CascadeType.ALL)
    private Set<RecipeStep> recipeIngredients = new HashSet<>();
}
