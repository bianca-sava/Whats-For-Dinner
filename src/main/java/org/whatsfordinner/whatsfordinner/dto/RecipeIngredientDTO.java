package org.whatsfordinner.whatsfordinner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeIngredientDTO {
    private String ingredientName;
    private Double quantity;
    private Boolean isOptional;
}