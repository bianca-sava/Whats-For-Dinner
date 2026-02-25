package org.whatsfordinner.whatsfordinner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.whatsfordinner.whatsfordinner.model.Recipe;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String instructions;
    private Integer prepTime;
    private Integer servings;
    private Recipe.MealType mealType;
    private Recipe.DietType dietType;
    private List<RecipeIngredientDTO> ingredients;
    private String imageUrl;
    private List<String> missingIngredients;
    private List<String> allergenWarnings;
}