package org.whatsfordinner.whatsfordinner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.whatsfordinner.whatsfordinner.model.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
