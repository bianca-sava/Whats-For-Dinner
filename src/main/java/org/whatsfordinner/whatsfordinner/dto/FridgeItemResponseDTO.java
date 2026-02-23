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
public class FridgeItemResponseDTO {
    private Long id;
    private String ingredientName;
    private Ingredient.Category category;
    private Ingredient.Unit unit;
    private Double quantity;
}