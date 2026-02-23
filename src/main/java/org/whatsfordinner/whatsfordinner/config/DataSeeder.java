package org.whatsfordinner.whatsfordinner.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.whatsfordinner.whatsfordinner.model.Ingredient;
import org.whatsfordinner.whatsfordinner.model.Recipe;
import org.whatsfordinner.whatsfordinner.model.RecipeIngredient;
import org.whatsfordinner.whatsfordinner.repository.IngredientRepository;
import org.whatsfordinner.whatsfordinner.repository.RecipeRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;

    @Override
    public void run(String... args) {
        if (ingredientRepository.count() > 0) return; // nu seed-uim de doua ori

        Ingredient egg = ingredientRepository.save(Ingredient.builder().name("egg").category(Ingredient.Category.OTHER).defaultUnit(Ingredient.Unit.PIECES).build());
        Ingredient milk = ingredientRepository.save(Ingredient.builder().name("milk").category(Ingredient.Category.DAIRY).defaultUnit(Ingredient.Unit.ML).build());
        Ingredient flour = ingredientRepository.save(Ingredient.builder().name("flour").category(Ingredient.Category.GRAIN).defaultUnit(Ingredient.Unit.GRAMS).build());
        Ingredient butter = ingredientRepository.save(Ingredient.builder().name("butter").category(Ingredient.Category.DAIRY).defaultUnit(Ingredient.Unit.GRAMS).build());
        Ingredient sugar = ingredientRepository.save(Ingredient.builder().name("sugar").category(Ingredient.Category.OTHER).defaultUnit(Ingredient.Unit.GRAMS).build());
        Ingredient pasta = ingredientRepository.save(Ingredient.builder().name("pasta").category(Ingredient.Category.GRAIN).defaultUnit(Ingredient.Unit.GRAMS).build());
        Ingredient tomatoSauce = ingredientRepository.save(Ingredient.builder().name("tomato sauce").category(Ingredient.Category.VEGETABLE).defaultUnit(Ingredient.Unit.ML).build());
        Ingredient cheese = ingredientRepository.save(Ingredient.builder().name("cheese").category(Ingredient.Category.DAIRY).defaultUnit(Ingredient.Unit.GRAMS).build());

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