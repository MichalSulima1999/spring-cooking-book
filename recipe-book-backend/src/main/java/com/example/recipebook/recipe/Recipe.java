package com.example.recipebook.recipe;

import com.example.recipebook.diet.Diet;
import com.example.recipebook.category.Category;
import com.example.recipebook.recipe_ingredient.RecipeIngredient;
import com.example.recipebook.step.Step;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 64, unique = true)
    @NotBlank
    private String name;

    @Column(nullable = false)
    private String description;

    private String image;

    @Column(nullable = false)
    private int cookingMinutes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private SkillLevel skillLevel;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "recipe_category_id", nullable = false)
    @NotNull
    private Category category;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "diet_id", nullable = false)
    @NotNull
    private Diet diet;

    @JsonManagedReference
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private Set<RecipeIngredient> recipeIngredients = new HashSet<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private Set<Step> steps = new HashSet<>();
}
