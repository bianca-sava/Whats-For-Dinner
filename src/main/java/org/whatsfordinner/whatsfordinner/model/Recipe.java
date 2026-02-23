package org.whatsfordinner.whatsfordinner.model;

import lombok.*;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "recipes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(columnDefinition = "TEXT")
    private String instructions;

    private Integer prepTime;

    private Integer servings;

    @Enumerated(EnumType.STRING)
    private MealType mealType;

    @Enumerated(EnumType.STRING)
    private DietType dietType;

    private String imageUrl;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<RecipeIngredient> recipeIngredients;

    public enum MealType {
        BREAKFAST, LUNCH, DINNER, SNACK
    }

    public enum DietType {
        NORMAL, VEGETARIAN, VEGAN
    }
}
