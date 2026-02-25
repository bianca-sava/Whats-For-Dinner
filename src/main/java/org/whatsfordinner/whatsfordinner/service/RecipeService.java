package org.whatsfordinner.whatsfordinner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.whatsfordinner.whatsfordinner.dto.RecipeIngredientDTO;
import org.whatsfordinner.whatsfordinner.dto.RecipeResponseDTO;
import org.whatsfordinner.whatsfordinner.dto.RecipeSearchRequestDTO;
import org.whatsfordinner.whatsfordinner.model.Recipe;
import org.whatsfordinner.whatsfordinner.model.User;
import org.whatsfordinner.whatsfordinner.model.UserPreferences;
import org.whatsfordinner.whatsfordinner.repository.RecipeRepository;
import org.whatsfordinner.whatsfordinner.repository.UserAllergyRepository;
import org.whatsfordinner.whatsfordinner.repository.UserFridgeRepository;
import org.whatsfordinner.whatsfordinner.repository.UserPreferencesRepository;
import org.whatsfordinner.whatsfordinner.repository.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserFridgeRepository userFridgeRepository;
    private final UserRepository userRepository;
    private final UserPreferencesRepository userPreferencesRepository;
    private final UserAllergyRepository userAllergyRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<RecipeResponseDTO> searchRecipes(RecipeSearchRequestDTO request) {
        User user = getCurrentUser();

        Set<Long> fridgeIngredientIds = userFridgeRepository.findByUser(user)
                .stream()
                .map(userFridge -> userFridge.getIngredient().getId())
                .collect(Collectors.toSet());

        Set<Long> userAllergyIds = userAllergyRepository.findByUser(user)
                .stream()
                .map(ua -> ua.getAllergy().getId())
                .collect(Collectors.toSet());

        int maxMissing = request.getMaxMissingIngredients() != null
                ? request.getMaxMissingIngredients()
                : 0;

        Recipe.DietType effectiveDietType = request.getDietType();
        if (effectiveDietType == null) {
            UserPreferences prefs = userPreferencesRepository.findByUser(user).orElse(null);
            if (prefs != null) {
                if (Boolean.TRUE.equals(prefs.getIsVegan())) {
                    effectiveDietType = Recipe.DietType.VEGAN;
                } else if (Boolean.TRUE.equals(prefs.getIsVegetarian())) {
                    effectiveDietType = Recipe.DietType.VEGETARIAN;
                }
            }
        }

        final Recipe.DietType finalDietType =
                effectiveDietType == Recipe.DietType.NORMAL ? null : effectiveDietType;

        return recipeRepository.findAll()
                .stream()
                .filter(recipe -> matchesMealType(recipe, request.getMealType()))
                .filter(recipe -> matchesDietType(recipe, finalDietType))
                .filter(recipe -> countMissingIngredients(recipe, fridgeIngredientIds) <= maxMissing)
                .map(recipe -> mapToDTO(recipe, fridgeIngredientIds, userAllergyIds))
                .collect(Collectors.toList());
    }

    private boolean matchesMealType(Recipe recipe, Recipe.MealType mealType) {
        if (mealType == null) return true;
        return recipe.getMealType() == mealType;
    }

    private boolean matchesDietType(Recipe recipe, Recipe.DietType dietType) {
        if (dietType == null) return true;
        return switch (dietType) {
            case VEGAN -> recipe.getDietType() == Recipe.DietType.VEGAN;
            case VEGETARIAN -> recipe.getDietType() == Recipe.DietType.VEGETARIAN
                    || recipe.getDietType() == Recipe.DietType.VEGAN;
            case NORMAL -> true;
        };
    }

    private long countMissingIngredients(Recipe recipe, Set<Long> fridgeIngredientIds) {
        return recipe.getRecipeIngredients()
                .stream()
                .filter(ri -> !ri.getIsOptional())
                .filter(ri -> !ri.getIngredient().getIsPantryItem())
                .filter(ri -> !fridgeIngredientIds.contains(ri.getIngredient().getId()))
                .count();
    }

    private RecipeResponseDTO mapToDTO(Recipe recipe, Set<Long> fridgeIngredientIds, Set<Long> userAllergyIds) {
        List<String> missingIngredients = recipe.getRecipeIngredients()
                .stream()
                .filter(ri -> !ri.getIsOptional())
                .filter(ri -> !ri.getIngredient().getIsPantryItem())
                .filter(ri -> !fridgeIngredientIds.contains(ri.getIngredient().getId()))
                .map(ri -> ri.getIngredient().getName())
                .collect(Collectors.toList());

        List<String> allergenWarnings = userAllergyIds.isEmpty()
                ? List.of()
                : recipe.getRecipeIngredients()
                .stream()
                .map(ri -> ri.getIngredient().getAllergen())
                .filter(allergen -> allergen != null && userAllergyIds.contains(allergen.getId()))
                .map(allergen -> allergen.getName())
                .distinct()
                .collect(Collectors.toList());

        List<RecipeIngredientDTO> ingredients = recipe.getRecipeIngredients()
                .stream()
                .map(ri -> RecipeIngredientDTO.builder()
                        .ingredientName(ri.getIngredient().getName())
                        .quantity(ri.getQuantity())
                        .unit(ri.getIngredient().getDefaultUnit())
                        .isOptional(ri.getIsOptional())
                        .build())
                .collect(Collectors.toList());

        return RecipeResponseDTO.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .description(recipe.getDescription())
                .instructions(recipe.getInstructions())
                .prepTime(recipe.getPrepTime())
                .servings(recipe.getServings())
                .mealType(recipe.getMealType())
                .dietType(recipe.getDietType())
                .ingredients(ingredients)
                .missingIngredients(missingIngredients)
                .allergenWarnings(allergenWarnings)
                .build();
    }
}