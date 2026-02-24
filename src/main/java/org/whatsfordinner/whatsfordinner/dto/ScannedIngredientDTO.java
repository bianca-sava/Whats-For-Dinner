package org.whatsfordinner.whatsfordinner.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScannedIngredientDTO {
    private Long ingredientId;
    private String receiptName;
    private String mappedName;
}