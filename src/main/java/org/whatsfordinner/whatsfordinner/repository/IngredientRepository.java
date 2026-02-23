package org.whatsfordinner.whatsfordinner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.whatsfordinner.whatsfordinner.model.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
