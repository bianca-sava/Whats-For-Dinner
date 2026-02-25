package org.whatsfordinner.whatsfordinner.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.whatsfordinner.whatsfordinner.model.Allergy;
import org.whatsfordinner.whatsfordinner.model.Ingredient;
import org.whatsfordinner.whatsfordinner.model.Recipe;
import org.whatsfordinner.whatsfordinner.model.RecipeIngredient;
import org.whatsfordinner.whatsfordinner.repository.AllergyRepository;
import org.whatsfordinner.whatsfordinner.repository.IngredientRepository;
import org.whatsfordinner.whatsfordinner.repository.RecipeRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;
    private final AllergyRepository allergyRepository;

    @Override
    public void run(String... args) {

        Allergy lactose   = seedAllergy("lactose");
        Allergy eggs      = seedAllergy("eggs");
        Allergy gluten    = seedAllergy("gluten");
        Allergy nuts      = seedAllergy("nuts");
        Allergy peanuts   = seedAllergy("peanuts");
        Allergy fish      = seedAllergy("fish");
        Allergy shellfish = seedAllergy("shellfish");
        Allergy soy       = seedAllergy("soy");

        Ingredient egg         = seedIngredient("egg",          Ingredient.Category.OTHER,     Ingredient.Unit.PIECES, false, eggs);
        Ingredient milk        = seedIngredient("milk",         Ingredient.Category.DAIRY,     Ingredient.Unit.ML,     false, lactose);
        Ingredient flour       = seedIngredient("flour",        Ingredient.Category.GRAIN,     Ingredient.Unit.GRAMS,  false, gluten);
        Ingredient butter      = seedIngredient("butter",       Ingredient.Category.DAIRY,     Ingredient.Unit.GRAMS,  false, lactose);
        Ingredient pasta       = seedIngredient("pasta",        Ingredient.Category.GRAIN,     Ingredient.Unit.GRAMS,  false, gluten);
        Ingredient tomatoSauce = seedIngredient("tomato sauce", Ingredient.Category.VEGETABLE, Ingredient.Unit.ML,     false, null);
        Ingredient cheese      = seedIngredient("cheese",       Ingredient.Category.DAIRY,     Ingredient.Unit.GRAMS,  false, lactose);

        seedIngredient("pepper", Ingredient.Category.SPICE, Ingredient.Unit.TASTE, true, null);
        seedIngredient("salt",   Ingredient.Category.SPICE, Ingredient.Unit.TASTE, true, null);
        seedIngredient("oil",    Ingredient.Category.OTHER, Ingredient.Unit.ML,    true, null);
        seedIngredient("sugar",  Ingredient.Category.OTHER, Ingredient.Unit.GRAMS, true, null);

        if (recipeRepository.count() == 0) {
            Recipe pancakes = recipeRepository.save(Recipe.builder()
                    .name("Pancakes")
                    .description("Fluffy breakfast pancakes")
                    .instructions("Mix ingredients, cook on pan 2 min each side")
                    .prepTime(15)
                    .servings(2)
                    .mealType(Recipe.MealType.BREAKFAST)
                    .dietType(Recipe.DietType.VEGETARIAN)
                    .build());

            pancakes.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(pancakes).ingredient(egg).quantity(2.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(pancakes).ingredient(milk).quantity(200.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(pancakes).ingredient(flour).quantity(150.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(pancakes).ingredient(butter).quantity(30.0).isOptional(true).build()
            ));
            recipeRepository.save(pancakes);

            Recipe pastaRecipe = recipeRepository.save(Recipe.builder()
                    .name("Pasta with tomato sauce")
                    .description("Simple and quick pasta")
                    .instructions("Boil pasta, heat sauce, combine")
                    .prepTime(20)
                    .servings(2)
                    .mealType(Recipe.MealType.LUNCH)
                    .dietType(Recipe.DietType.VEGAN)
                    .build());

            pastaRecipe.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(pastaRecipe).ingredient(pasta).quantity(200.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(pastaRecipe).ingredient(tomatoSauce).quantity(150.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(pastaRecipe).ingredient(cheese).quantity(50.0).isOptional(true).build()
            ));
            recipeRepository.save(pastaRecipe);
        }
    }

    private Ingredient seedIngredient(String name, Ingredient.Category category, Ingredient.Unit unit,
                                      boolean isPantry, Allergy allergen) {
        return ingredientRepository.findByName(name)
                .orElseGet(() -> ingredientRepository.save(
                        Ingredient.builder()
                                .name(name)
                                .category(category)
                                .defaultUnit(unit)
                                .isPantryItem(isPantry)
                                .allergen(allergen)
                                .build()
                ));
    }

    private Allergy seedAllergy(String name) {
        return allergyRepository.findByName(name)
                .orElseGet(() -> allergyRepository.save(
                        Allergy.builder().name(name).build()
                ));
    }
}