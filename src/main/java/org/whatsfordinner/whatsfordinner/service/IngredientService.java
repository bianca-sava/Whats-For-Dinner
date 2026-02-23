package org.whatsfordinner.whatsfordinner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.whatsfordinner.whatsfordinner.model.Ingredient;
import org.whatsfordinner.whatsfordinner.repository.IngredientRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public Ingredient getIngredientById(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));
    }

    public Ingredient createIngredient(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    public Ingredient updateIngredient(Long id, Ingredient ingredient) {
        Ingredient existing = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        existing.setName(ingredient.getName());
        existing.setCategory(ingredient.getCategory());
        existing.setDefaultUnit(ingredient.getDefaultUnit());

        return ingredientRepository.save(existing);
    }

    public void deleteIngredient(Long id) {
        if (!ingredientRepository.existsById(id)) {
            throw new RuntimeException("Ingredient not found");
        }
        ingredientRepository.deleteById(id);
    }

}