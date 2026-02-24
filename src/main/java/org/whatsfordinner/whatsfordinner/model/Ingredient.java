package org.whatsfordinner.whatsfordinner.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ingredients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Unit defaultUnit;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isPantryItem = false;

    public enum Category {
        DAIRY, MEAT, VEGETABLE, FRUIT, GRAIN, OTHER, SPICE
    }

    public enum Unit {
        GRAMS, ML, PIECES, TEASPOON, TABLESPOON, TASTE
    }
}