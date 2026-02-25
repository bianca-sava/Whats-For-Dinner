package org.whatsfordinner.whatsfordinner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.whatsfordinner.whatsfordinner.model.Ingredient;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeIngredientDTO {
    private String ingredientName;
    private Double quantity;
    private Ingredient.Unit unit;
    private Boolean isOptional;
}