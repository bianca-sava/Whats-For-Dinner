package org.whatsfordinner.whatsfordinner.dto;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.whatsfordinner.whatsfordinner.model.Ingredient;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientResponseDTO {
    private Long id;
    private String name;
    private Ingredient.Category category;
    private Ingredient.Unit defaultUnit;
}
