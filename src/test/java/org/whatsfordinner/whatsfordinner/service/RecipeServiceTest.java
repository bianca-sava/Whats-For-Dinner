package org.whatsfordinner.whatsfordinner.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.whatsfordinner.whatsfordinner.dto.RecipeResponseDTO;
import org.whatsfordinner.whatsfordinner.dto.RecipeSearchRequestDTO;
import org.whatsfordinner.whatsfordinner.exception.NotFoundException;
import org.whatsfordinner.whatsfordinner.model.*;
import org.whatsfordinner.whatsfordinner.repository.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock private RecipeRepository recipeRepository;
    @Mock private UserFridgeRepository userFridgeRepository;
    @Mock private UserRepository userRepository;
    @Mock private UserPreferencesRepository userPreferencesRepository;
    @Mock private UserAllergyRepository userAllergyRepository;

    @InjectMocks private RecipeService recipeService;

    private User user;
    private Recipe veganRecipe;
    private Recipe vegetarianRecipe;
    private Recipe normalRecipe;
    private Ingredient pasta;
    private Ingredient egg;

    // SINGLE @BeforeEach — everything initialized here in order
    @BeforeEach
    void setUp() {
        // 1. Security context
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@test.com");
        SecurityContext ctx = mock(SecurityContext.class);
        when(ctx.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(ctx);

        // 2. User with UUID
        user = User.builder().email("test@test.com").build();
        user.setId(UUID.randomUUID());

        // 3. Ingredients
        pasta = Ingredient.builder().id(1L).name("pasta")
                .isPantryItem(false).category(Ingredient.Category.GRAIN)
                .defaultUnit(Ingredient.Unit.GRAMS).build();
        egg = Ingredient.builder().id(2L).name("egg")
                .isPantryItem(false).category(Ingredient.Category.OTHER)
                .defaultUnit(Ingredient.Unit.PIECES).build();

        // 4. Recipes
        veganRecipe = Recipe.builder().id(1L).name("Vegan Pasta")
                .mealType(Recipe.MealType.LUNCH).dietType(Recipe.DietType.VEGAN)
                .prepTime(20).servings(2).build();
        veganRecipe.setRecipeIngredients(List.of(
                RecipeIngredient.builder().recipe(veganRecipe).ingredient(pasta)
                        .quantity(200.0).isOptional(false).build()
        ));

        vegetarianRecipe = Recipe.builder().id(2L).name("Veggie Omelette")
                .mealType(Recipe.MealType.BREAKFAST).dietType(Recipe.DietType.VEGETARIAN)
                .prepTime(10).servings(1).build();
        vegetarianRecipe.setRecipeIngredients(List.of(
                RecipeIngredient.builder().recipe(vegetarianRecipe).ingredient(egg)
                        .quantity(2.0).isOptional(false).build()
        ));

        normalRecipe = Recipe.builder().id(3L).name("Chicken Salad")
                .mealType(Recipe.MealType.DINNER).dietType(Recipe.DietType.NORMAL)
                .prepTime(15).servings(2).build();
        normalRecipe.setRecipeIngredients(List.of());

        // 5. Common mocks — user is not null here
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(userFridgeRepository.findByUser(user)).thenReturn(List.of());
        when(userAllergyRepository.findByUser(user)).thenReturn(List.of());
    }

    @Test
    void searchRecipes_withVeganFilter_returnsOnlyVeganRecipes() {
        when(recipeRepository.findAllWithIngredients())
                .thenReturn(List.of(veganRecipe, vegetarianRecipe, normalRecipe));

        List<RecipeResponseDTO> result = recipeService.searchRecipes(
                RecipeSearchRequestDTO.builder()
                        .dietType(Recipe.DietType.VEGAN).maxMissingIngredients(10).build());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Vegan Pasta");
    }

    @Test
    void searchRecipes_withVegetarianFilter_returnsVegetarianAndVeganRecipes() {
        when(recipeRepository.findAllWithIngredients())
                .thenReturn(List.of(veganRecipe, vegetarianRecipe, normalRecipe));

        List<RecipeResponseDTO> result = recipeService.searchRecipes(
                RecipeSearchRequestDTO.builder()
                        .dietType(Recipe.DietType.VEGETARIAN).maxMissingIngredients(10).build());

        assertThat(result).hasSize(2);
        assertThat(result).extracting(RecipeResponseDTO::getName)
                .containsExactlyInAnyOrder("Vegan Pasta", "Veggie Omelette");
    }

    @Test
    void searchRecipes_withNormalFilter_returnsAllRecipes() {
        when(recipeRepository.findAllWithIngredients())
                .thenReturn(List.of(veganRecipe, vegetarianRecipe, normalRecipe));

        List<RecipeResponseDTO> result = recipeService.searchRecipes(
                RecipeSearchRequestDTO.builder()
                        .dietType(Recipe.DietType.NORMAL).maxMissingIngredients(10).build());

        assertThat(result).hasSize(3);
    }

    @Test
    void searchRecipes_withNullDiet_usesProfilePreferences() {
        when(userPreferencesRepository.findByUser(user)).thenReturn(Optional.of(
                UserPreferences.builder().isVegetarian(true).isVegan(false).build()
        ));
        when(recipeRepository.findAllWithIngredients())
                .thenReturn(List.of(veganRecipe, vegetarianRecipe, normalRecipe));

        List<RecipeResponseDTO> result = recipeService.searchRecipes(
                RecipeSearchRequestDTO.builder().dietType(null).maxMissingIngredients(10).build());

        assertThat(result).hasSize(2);
        assertThat(result).extracting(RecipeResponseDTO::getName)
                .containsExactlyInAnyOrder("Vegan Pasta", "Veggie Omelette");
    }

    @Test
    void searchRecipes_withZeroMissing_returnsOnlyRecipesWithAllIngredients() {
        when(userFridgeRepository.findByUser(user)).thenReturn(List.of(
                UserFridge.builder().user(user).ingredient(pasta).build()
        ));
        when(recipeRepository.findAllWithIngredients())
                .thenReturn(List.of(veganRecipe, vegetarianRecipe));

        List<RecipeResponseDTO> result = recipeService.searchRecipes(
                RecipeSearchRequestDTO.builder().maxMissingIngredients(0).build());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Vegan Pasta");
    }

    @Test
    void searchRecipes_missingIngredientsListedCorrectly() {
        when(recipeRepository.findAllWithIngredients()).thenReturn(List.of(veganRecipe));

        List<RecipeResponseDTO> result = recipeService.searchRecipes(
                RecipeSearchRequestDTO.builder().maxMissingIngredients(5).build());

        assertThat(result.get(0).getMissingIngredients()).containsExactly("pasta");
    }

    @Test
    void searchRecipes_pantryItemsNotCountedAsMissing() {
        Ingredient salt = Ingredient.builder().id(3L).name("salt")
                .isPantryItem(true).defaultUnit(Ingredient.Unit.TASTE).build();
        Recipe saltedPasta = Recipe.builder().id(4L).name("Salted Pasta")
                .mealType(Recipe.MealType.LUNCH).dietType(Recipe.DietType.VEGAN)
                .prepTime(10).servings(2).build();
        saltedPasta.setRecipeIngredients(List.of(
                RecipeIngredient.builder().recipe(saltedPasta).ingredient(salt)
                        .quantity(1.0).isOptional(false).build()
        ));
        when(recipeRepository.findAllWithIngredients()).thenReturn(List.of(saltedPasta));

        List<RecipeResponseDTO> result = recipeService.searchRecipes(
                RecipeSearchRequestDTO.builder().maxMissingIngredients(0).build());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMissingIngredients()).isEmpty();
    }

    @Test
    void searchRecipes_optionalIngredientsNotCountedAsMissing() {
        Ingredient cheese = Ingredient.builder().id(4L).name("cheese")
                .isPantryItem(false).defaultUnit(Ingredient.Unit.GRAMS).build();
        Recipe r = Recipe.builder().id(5L).name("Pasta Optional Cheese")
                .mealType(Recipe.MealType.LUNCH).dietType(Recipe.DietType.VEGAN)
                .prepTime(20).servings(2).build();
        r.setRecipeIngredients(List.of(
                RecipeIngredient.builder().recipe(r).ingredient(pasta)
                        .quantity(200.0).isOptional(false).build(),
                RecipeIngredient.builder().recipe(r).ingredient(cheese)
                        .quantity(50.0).isOptional(true).build()
        ));
        when(userFridgeRepository.findByUser(user)).thenReturn(List.of(
                UserFridge.builder().user(user).ingredient(pasta).build()
        ));
        when(recipeRepository.findAllWithIngredients()).thenReturn(List.of(r));

        List<RecipeResponseDTO> result = recipeService.searchRecipes(
                RecipeSearchRequestDTO.builder().maxMissingIngredients(0).build());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMissingIngredients()).isEmpty();
    }

    @Test
    void searchRecipes_withMealTypeFilter_returnsOnlyMatchingMealType() {
        when(recipeRepository.findAllWithIngredients())
                .thenReturn(List.of(veganRecipe, vegetarianRecipe, normalRecipe));

        List<RecipeResponseDTO> result = recipeService.searchRecipes(
                RecipeSearchRequestDTO.builder()
                        .mealType(Recipe.MealType.LUNCH).maxMissingIngredients(10).build());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Vegan Pasta");
    }

    @Test
    void searchRecipes_allergenWarningsIncludedWhenUserHasAllergy() {
        Allergy eggsAllergy = Allergy.builder().id(1L).name("eggs").build();
        egg.setAllergen(eggsAllergy);
        when(userAllergyRepository.findByUser(user)).thenReturn(List.of(
                UserAllergy.builder().user(user).allergy(eggsAllergy).build()
        ));
        when(recipeRepository.findAllWithIngredients()).thenReturn(List.of(vegetarianRecipe));

        List<RecipeResponseDTO> result = recipeService.searchRecipes(
                RecipeSearchRequestDTO.builder().maxMissingIngredients(10).build());

        assertThat(result.get(0).getAllergenWarnings()).containsExactly("eggs");
    }

    @Test
    void getRecipeById_throwsNotFoundException_whenRecipeDoesNotExist() {
        when(recipeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recipeService.getRecipeById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Recipe not found");
    }

    @Test
    void getRecipeById_returnsRecipe_whenExists() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(veganRecipe));

        RecipeResponseDTO result = recipeService.getRecipeById(1L);

        assertThat(result.getName()).isEqualTo("Vegan Pasta");
        assertThat(result.getId()).isEqualTo(1L);
    }
}