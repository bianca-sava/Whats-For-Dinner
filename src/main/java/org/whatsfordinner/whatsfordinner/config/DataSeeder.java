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

        Ingredient egg = seedIngredient("egg", Ingredient.Category.OTHER, Ingredient.Unit.PIECES, false);
        Ingredient milk = seedIngredient("milk", Ingredient.Category.DAIRY, Ingredient.Unit.ML, false);
        Ingredient flour = seedIngredient("flour", Ingredient.Category.GRAIN, Ingredient.Unit.GRAMS, false);
        Ingredient butter = seedIngredient("butter", Ingredient.Category.DAIRY, Ingredient.Unit.GRAMS, false);
        Ingredient pasta = seedIngredient("pasta", Ingredient.Category.GRAIN, Ingredient.Unit.GRAMS, false);
        Ingredient tomatoSauce = seedIngredient("tomato sauce", Ingredient.Category.VEGETABLE, Ingredient.Unit.ML, false);
        Ingredient cheese = seedIngredient("cheese", Ingredient.Category.DAIRY, Ingredient.Unit.GRAMS, false);

        Ingredient pepper = seedIngredient("pepper", Ingredient.Category.SPICE, Ingredient.Unit.TASTE, true);
        Ingredient salt = seedIngredient("salt", Ingredient.Category.SPICE, Ingredient.Unit.TASTE, true);
        Ingredient oil = seedIngredient("oil", Ingredient.Category.OTHER, Ingredient.Unit.ML, true);
        Ingredient sugar = seedIngredient("sugar", Ingredient.Category.OTHER, Ingredient.Unit.GRAMS, true);

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

        seedAllergy("gluten");
        seedAllergy("lactose");
        seedAllergy("nuts");
        seedAllergy("peanuts");
        seedAllergy("eggs");
        seedAllergy("fish");
        seedAllergy("shellfish");
        seedAllergy("soy");
    }

    private Ingredient seedIngredient(String name, Ingredient.Category category, Ingredient.Unit unit, boolean isPantry) {
        return ingredientRepository.findByName(name)
                .orElseGet(() -> ingredientRepository.save(
                        Ingredient.builder()
                                .name(name)
                                .category(category)
                                .defaultUnit(unit)
                                .isPantryItem(isPantry)
                                .build()
                ));
    }

    private void seedAllergy(String name) {
        allergyRepository.findByName(name)
                .orElseGet(() -> allergyRepository.save(
                        Allergy.builder().name(name).build()
                ));
    }
}