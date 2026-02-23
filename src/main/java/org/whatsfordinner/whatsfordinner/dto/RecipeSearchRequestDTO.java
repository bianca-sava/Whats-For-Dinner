package org.whatsfordinner.whatsfordinner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.whatsfordinner.whatsfordinner.model.Recipe;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeSearchRequestDTO {
    private Recipe.MealType mealType;
    private Recipe.DietType dietType;
    private Integer maxMissingIngredients;
}