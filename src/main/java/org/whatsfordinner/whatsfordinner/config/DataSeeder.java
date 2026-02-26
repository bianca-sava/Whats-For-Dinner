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

        // --- Allergies ---
        Allergy lactose   = seedAllergy("lactose");
        Allergy eggs      = seedAllergy("eggs");
        Allergy gluten    = seedAllergy("gluten");
        Allergy nuts      = seedAllergy("nuts");
        Allergy peanuts   = seedAllergy("peanuts");
        Allergy fish      = seedAllergy("fish");
        Allergy shellfish = seedAllergy("shellfish");
        Allergy soy       = seedAllergy("soy");

        // --- Pantry staples ---
        Ingredient salt        = seedIngredient("salt",         Ingredient.Category.SPICE,     Ingredient.Unit.TASTE,     true,  null);
        Ingredient pepper      = seedIngredient("pepper",       Ingredient.Category.SPICE,     Ingredient.Unit.TASTE,     true,  null);
        Ingredient oil         = seedIngredient("oil",          Ingredient.Category.OTHER,     Ingredient.Unit.ML,        true,  null);
        Ingredient sugar       = seedIngredient("sugar",        Ingredient.Category.OTHER,     Ingredient.Unit.GRAMS,     true,  null);
        Ingredient butter      = seedIngredient("butter",       Ingredient.Category.DAIRY,     Ingredient.Unit.GRAMS,     false, lactose);
        Ingredient soySauce    = seedIngredient("soy sauce",    Ingredient.Category.OTHER,     Ingredient.Unit.TABLESPOON,true,  soy);
        Ingredient garlic      = seedIngredient("garlic",       Ingredient.Category.VEGETABLE, Ingredient.Unit.PIECES,    true,  null);

        // --- Dairy ---
        Ingredient milk        = seedIngredient("milk",         Ingredient.Category.DAIRY,     Ingredient.Unit.ML,        false, lactose);
        Ingredient cheese      = seedIngredient("cheese",       Ingredient.Category.DAIRY,     Ingredient.Unit.GRAMS,     false, lactose);
        Ingredient heavyCream  = seedIngredient("heavy cream",  Ingredient.Category.DAIRY,     Ingredient.Unit.ML,        false, lactose);

        // --- Grains ---
        Ingredient flour       = seedIngredient("flour",        Ingredient.Category.GRAIN,     Ingredient.Unit.GRAMS,     false, gluten);
        Ingredient pasta       = seedIngredient("pasta",        Ingredient.Category.GRAIN,     Ingredient.Unit.GRAMS,     false, gluten);
        Ingredient rice        = seedIngredient("rice",         Ingredient.Category.GRAIN,     Ingredient.Unit.GRAMS,     false, null);
        Ingredient breadcrumbs = seedIngredient("breadcrumbs",  Ingredient.Category.GRAIN,     Ingredient.Unit.GRAMS,     true,  gluten);
        Ingredient oats        = seedIngredient("oats",         Ingredient.Category.GRAIN,     Ingredient.Unit.GRAMS,     false, gluten);
        Ingredient wildRice       = seedIngredient("wild rice",        Ingredient.Category.GRAIN,     Ingredient.Unit.GRAMS,      false, null);
        Ingredient orzo           = seedIngredient("orzo",             Ingredient.Category.GRAIN,     Ingredient.Unit.GRAMS,      false, gluten);
        Ingredient quinoa         = seedIngredient("quinoa",           Ingredient.Category.GRAIN,     Ingredient.Unit.GRAMS,      false, null);
        Ingredient rolledOats     = seedIngredient("rolled oats",      Ingredient.Category.GRAIN,     Ingredient.Unit.GRAMS,      true,  gluten);
        Ingredient lasagnaNoodles = seedIngredient("lasagna noodles",  Ingredient.Category.GRAIN,     Ingredient.Unit.GRAMS,      false, gluten);
        Ingredient tortillas      = seedIngredient("flour tortillas",  Ingredient.Category.GRAIN,     Ingredient.Unit.PIECES,     false, gluten);

        // --- Proteins ---
        Ingredient egg         = seedIngredient("egg",          Ingredient.Category.OTHER,     Ingredient.Unit.PIECES,    false, eggs);
        Ingredient chickenBreast = seedIngredient("chicken breast", Ingredient.Category.MEAT,  Ingredient.Unit.GRAMS,     false, null);
        Ingredient groundBeef  = seedIngredient("ground beef",  Ingredient.Category.MEAT,      Ingredient.Unit.GRAMS,     false, null);
        Ingredient bacon       = seedIngredient("bacon",        Ingredient.Category.MEAT,      Ingredient.Unit.GRAMS,     false, null);
        Ingredient tuna        = seedIngredient("canned tuna",  Ingredient.Category.MEAT,      Ingredient.Unit.GRAMS,     false, fish);
        Ingredient groundTurkey   = seedIngredient("ground turkey",    Ingredient.Category.MEAT,      Ingredient.Unit.GRAMS,      false, null);
        Ingredient sausage        = seedIngredient("sausage",          Ingredient.Category.MEAT,      Ingredient.Unit.GRAMS,      false, null);
        Ingredient shrimp         = seedIngredient("shrimp",           Ingredient.Category.MEAT,      Ingredient.Unit.GRAMS,      false, shellfish);
        Ingredient cannedChickpeas= seedIngredient("canned chickpeas", Ingredient.Category.OTHER,     Ingredient.Unit.GRAMS,      true,  null);
        Ingredient cannedBlackBeans=seedIngredient("canned black beans",Ingredient.Category.OTHER,    Ingredient.Unit.GRAMS,      true,  null);
        Ingredient cannedWhiteBeans=seedIngredient("canned white beans",Ingredient.Category.OTHER,    Ingredient.Unit.GRAMS,      true,  null);
        Ingredient greenLentils   = seedIngredient("green lentils",    Ingredient.Category.OTHER,     Ingredient.Unit.GRAMS,      true,  null);
        Ingredient tofu           = seedIngredient("tofu",             Ingredient.Category.OTHER,     Ingredient.Unit.GRAMS,      false, soy);
        Ingredient ricotta        = seedIngredient("ricotta",          Ingredient.Category.DAIRY,     Ingredient.Unit.GRAMS,      false, lactose);

        // --- Vegetables ---
        Ingredient tomatoSauce = seedIngredient("tomato sauce", Ingredient.Category.VEGETABLE, Ingredient.Unit.ML,        false, null);
        Ingredient cannedTomatoes = seedIngredient("canned tomatoes", Ingredient.Category.VEGETABLE, Ingredient.Unit.GRAMS, false, null);
        Ingredient onion       = seedIngredient("onion",        Ingredient.Category.VEGETABLE, Ingredient.Unit.PIECES,    false, null);
        Ingredient carrot      = seedIngredient("carrot",       Ingredient.Category.VEGETABLE, Ingredient.Unit.PIECES,    false, null);
        Ingredient frozenPeas  = seedIngredient("frozen peas",  Ingredient.Category.VEGETABLE, Ingredient.Unit.GRAMS,     false, null);
        Ingredient spinach     = seedIngredient("spinach",      Ingredient.Category.VEGETABLE, Ingredient.Unit.GRAMS,     false, null);
        Ingredient potato      = seedIngredient("potato",       Ingredient.Category.VEGETABLE, Ingredient.Unit.PIECES,    false, null);
        Ingredient broccoli    = seedIngredient("broccoli",     Ingredient.Category.VEGETABLE, Ingredient.Unit.GRAMS,     false, null);
        Ingredient celery         = seedIngredient("celery",           Ingredient.Category.VEGETABLE, Ingredient.Unit.PIECES,     false, null);
        Ingredient bellPepper     = seedIngredient("bell pepper",      Ingredient.Category.VEGETABLE, Ingredient.Unit.PIECES,     false, null);
        Ingredient zucchini       = seedIngredient("zucchini",         Ingredient.Category.VEGETABLE, Ingredient.Unit.PIECES,     false, null);
        Ingredient corn           = seedIngredient("canned corn",      Ingredient.Category.VEGETABLE, Ingredient.Unit.GRAMS,      true,  null);
        Ingredient diceGreenChiles= seedIngredient("diced green chiles",Ingredient.Category.VEGETABLE,Ingredient.Unit.GRAMS,      true,  null);
        Ingredient kale           = seedIngredient("kale",             Ingredient.Category.VEGETABLE, Ingredient.Unit.GRAMS,      false, null);
        Ingredient mushrooms      = seedIngredient("mushrooms",        Ingredient.Category.VEGETABLE, Ingredient.Unit.GRAMS,      false, null);
        Ingredient sweetPotato    = seedIngredient("sweet potato",     Ingredient.Category.VEGETABLE, Ingredient.Unit.PIECES,     false, null);
        Ingredient crushedTomatoes= seedIngredient("crushed tomatoes", Ingredient.Category.VEGETABLE, Ingredient.Unit.GRAMS,      true,  null);

        // --- Other ---
        Ingredient chickenBroth = seedIngredient("chicken broth", Ingredient.Category.OTHER,  Ingredient.Unit.ML,        false, null);
        Ingredient lemon       = seedIngredient("lemon",        Ingredient.Category.FRUIT,     Ingredient.Unit.PIECES,    false, null);
        Ingredient banana      = seedIngredient("banana",       Ingredient.Category.FRUIT,     Ingredient.Unit.PIECES,    false, null);
        Ingredient vegetableBroth = seedIngredient("vegetable broth",  Ingredient.Category.OTHER,     Ingredient.Unit.ML,         true,  null);
        Ingredient coconutMilk    = seedIngredient("coconut milk",     Ingredient.Category.OTHER,     Ingredient.Unit.ML,         true,  null);
        Ingredient redCurryPaste  = seedIngredient("red curry paste",  Ingredient.Category.SPICE,     Ingredient.Unit.TABLESPOON, true,  null);
        Ingredient cumin          = seedIngredient("cumin",            Ingredient.Category.SPICE,     Ingredient.Unit.TEASPOON,   true,  null);
        Ingredient paprika        = seedIngredient("paprika",          Ingredient.Category.SPICE,     Ingredient.Unit.TEASPOON,   true,  null);
        Ingredient chiliPowder    = seedIngredient("chili powder",     Ingredient.Category.SPICE,     Ingredient.Unit.TEASPOON,   true,  null);
        Ingredient oregano        = seedIngredient("oregano",          Ingredient.Category.SPICE,     Ingredient.Unit.TEASPOON,   true,  null);
        Ingredient peanutButter   = seedIngredient("peanut butter",    Ingredient.Category.OTHER,     Ingredient.Unit.TABLESPOON, true,  peanuts);
        Ingredient sesameOil      = seedIngredient("sesame oil",       Ingredient.Category.OTHER,     Ingredient.Unit.TABLESPOON, true,  null);
        Ingredient lime           = seedIngredient("lime",             Ingredient.Category.FRUIT,     Ingredient.Unit.PIECES,     false, null);
        Ingredient pumpkinCan     = seedIngredient("canned pumpkin",   Ingredient.Category.OTHER,     Ingredient.Unit.GRAMS,      true,  null);
        Ingredient bakingPowder   = seedIngredient("baking powder",    Ingredient.Category.OTHER,     Ingredient.Unit.TEASPOON,   true,  null);
        Ingredient nutritionalYeast=seedIngredient("nutritional yeast",Ingredient.Category.OTHER,     Ingredient.Unit.TABLESPOON, true,  null);
        Ingredient enchiladadSauce= seedIngredient("enchilada sauce",  Ingredient.Category.OTHER,     Ingredient.Unit.ML,         true,  null);
        Ingredient salsaJar       = seedIngredient("jarred salsa",     Ingredient.Category.OTHER,     Ingredient.Unit.GRAMS,      true,  null);
        Ingredient molasses       = seedIngredient("molasses",         Ingredient.Category.OTHER,     Ingredient.Unit.TABLESPOON, true,  null);
        Ingredient honey          = seedIngredient("honey",            Ingredient.Category.OTHER,     Ingredient.Unit.TABLESPOON, true,  null);
        Ingredient tahini         = seedIngredient("tahini",           Ingredient.Category.OTHER,     Ingredient.Unit.TABLESPOON, true,  null);
        Ingredient lemonJuice     = seedIngredient("lemon juice",      Ingredient.Category.OTHER,     Ingredient.Unit.TABLESPOON, true,  null);
        Ingredient riceNoodles    = seedIngredient("rice noodles",     Ingredient.Category.GRAIN,     Ingredient.Unit.GRAMS,      false, null);
        Ingredient brownSugar     = seedIngredient("brown sugar",      Ingredient.Category.OTHER,     Ingredient.Unit.GRAMS,      true,  null);
        Ingredient chocolateChips = seedIngredient("chocolate chips",  Ingredient.Category.OTHER,     Ingredient.Unit.GRAMS,      true,  null);
        Ingredient cocoaPowder    = seedIngredient("cocoa powder",     Ingredient.Category.OTHER,     Ingredient.Unit.GRAMS,      true,  null);
        Ingredient vanillaExtract = seedIngredient("vanilla extract",  Ingredient.Category.OTHER,     Ingredient.Unit.TEASPOON,   true,  null);
        Ingredient shreddedCheese = seedIngredient("shredded cheddar", Ingredient.Category.DAIRY,     Ingredient.Unit.GRAMS,      false, lactose);
        Ingredient sourceCream    = seedIngredient("sour cream",       Ingredient.Category.DAIRY,     Ingredient.Unit.GRAMS,      false, lactose);
        Ingredient ginger         = seedIngredient("ginger",           Ingredient.Category.SPICE,     Ingredient.Unit.TEASPOON,   true,  null);

        if (recipeRepository.count() == 0) {

            // 1. PANCAKES (BREAKFAST, VEGETARIAN)
            Recipe pancakes = recipeRepository.save(Recipe.builder()
                    .name("Fluffy Pancakes")
                    .description("Light and fluffy breakfast pancakes, ready in 15 minutes.")
                    .instructions(
                            """
                                    1. In a large bowl, whisk together the flour, sugar, salt, and baking powder.
                                    2. In a separate bowl, beat the eggs, then add the milk and melted butter. Mix well.
                                    3. Pour the wet ingredients into the dry ingredients and stir until just combined. Do not overmix — lumps are okay.
                                    4. Heat a non-stick pan over medium heat and lightly grease with butter.
                                    5. Pour about 60ml of batter per pancake onto the pan.
                                    6. Cook until bubbles form on the surface (about 2 minutes), then flip and cook for 1-2 more minutes until golden.
                                    7. Serve immediately with your favorite toppings."""
                    )
                    .prepTime(15)
                    .servings(2)
                    .mealType(Recipe.MealType.BREAKFAST)
                    .dietType(Recipe.DietType.VEGETARIAN)
                    .imageUrl("https://images.unsplash.com/photo-1567620905732-2d1ec7ab7445?w=800")
                    .build());
            pancakes.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(pancakes).ingredient(flour).quantity(150.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(pancakes).ingredient(egg).quantity(2.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(pancakes).ingredient(milk).quantity(200.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(pancakes).ingredient(butter).quantity(30.0).isOptional(true).build(),
                    RecipeIngredient.builder().recipe(pancakes).ingredient(sugar).quantity(15.0).isOptional(false).build()
            ));
            recipeRepository.save(pancakes);

            // 2. SCRAMBLED EGGS (BREAKFAST, VEGETARIAN)
            Recipe scrambledEggs = recipeRepository.save(Recipe.builder()
                    .name("Creamy Scrambled Eggs")
                    .description("Soft, creamy scrambled eggs — the perfect quick breakfast.")
                    .instructions(
                            """
                                    1. Crack the eggs into a bowl, add a pinch of salt and pepper, and whisk until fully combined.
                                    2. Heat a non-stick pan over low-medium heat and add the butter.
                                    3. Pour in the eggs and let them sit for 20 seconds without stirring.
                                    4. Gently push the eggs from the edges to the center using a spatula, forming soft folds.
                                    5. Remove the pan from heat while the eggs still look slightly underdone — they will continue cooking.
                                    6. Add the heavy cream, stir once, and serve immediately."""
                    )
                    .prepTime(10)
                    .servings(1)
                    .mealType(Recipe.MealType.BREAKFAST)
                    .dietType(Recipe.DietType.VEGETARIAN)
                    .imageUrl("https://images.unsplash.com/photo-1525351484163-7529414344d8?w=800")
                    .build());
            scrambledEggs.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(scrambledEggs).ingredient(egg).quantity(3.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(scrambledEggs).ingredient(butter).quantity(10.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(scrambledEggs).ingredient(heavyCream).quantity(30.0).isOptional(true).build()
            ));
            recipeRepository.save(scrambledEggs);

            // 3. BANANA OATMEAL (BREAKFAST, VEGAN)
            Recipe bananaOatmeal = recipeRepository.save(Recipe.builder()
                    .name("Banana Oatmeal")
                    .description("Warm and filling oatmeal with banana — a simple vegan breakfast.")
                    .instructions(
                            "1. Add the oats and milk (or water) to a small saucepan over medium heat.\n" +
                                    "2. Stir continuously and cook for 4-5 minutes until the oats absorb the liquid and thicken.\n" +
                                    "3. While cooking, slice the banana.\n" +
                                    "4. Pour the oatmeal into a bowl and top with the sliced banana.\n" +
                                    "5. Add a pinch of sugar or a drizzle of honey if desired. Serve immediately."
                    )
                    .prepTime(10)
                    .servings(1)
                    .mealType(Recipe.MealType.BREAKFAST)
                    .dietType(Recipe.DietType.VEGAN)
                    .imageUrl("https://images.unsplash.com/photo-1517673400267-0251440c45dc?w=800")
                    .build());
            bananaOatmeal.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(bananaOatmeal).ingredient(oats).quantity(80.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(bananaOatmeal).ingredient(milk).quantity(200.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(bananaOatmeal).ingredient(banana).quantity(1.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(bananaOatmeal).ingredient(sugar).quantity(10.0).isOptional(true).build()
            ));
            recipeRepository.save(bananaOatmeal);

            // 4. PASTA WITH TOMATO SAUCE (LUNCH, VEGAN)
            Recipe pastaRecipe = recipeRepository.save(Recipe.builder()
                    .name("Pasta with Tomato Sauce")
                    .description("A classic, quick and satisfying pasta with homemade tomato sauce.")
                    .instructions(
                            "1. Bring a large pot of salted water to a boil.\n" +
                                    "2. Add the pasta and cook according to package instructions until al dente.\n" +
                                    "3. Meanwhile, heat oil in a pan over medium heat. Add the garlic and cook for 1 minute until fragrant.\n" +
                                    "4. Add the canned tomatoes, salt, and pepper. Stir and simmer for 10 minutes.\n" +
                                    "5. Drain the pasta, reserving a cup of pasta water.\n" +
                                    "6. Add the pasta to the sauce and toss well. Add a splash of pasta water if the sauce is too thick.\n" +
                                    "7. Serve hot, topped with grated cheese if desired."
                    )
                    .prepTime(20)
                    .servings(2)
                    .mealType(Recipe.MealType.LUNCH)
                    .dietType(Recipe.DietType.VEGAN)
                    .imageUrl("https://images.unsplash.com/photo-1598866594230-a7c12756260f?w=800")
                    .build());
            pastaRecipe.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(pastaRecipe).ingredient(pasta).quantity(200.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(pastaRecipe).ingredient(cannedTomatoes).quantity(400.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(pastaRecipe).ingredient(garlic).quantity(2.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(pastaRecipe).ingredient(cheese).quantity(50.0).isOptional(true).build()
            ));
            recipeRepository.save(pastaRecipe);

            // 5. PASTA CARBONARA (LUNCH, NORMAL)
            Recipe carbonara = recipeRepository.save(Recipe.builder()
                    .name("Pasta Carbonara")
                    .description("A rich and creamy Italian pasta with bacon and egg sauce.")
                    .instructions(
                            "1. Bring a large pot of salted water to a boil and cook the pasta until al dente.\n" +
                                    "2. While the pasta cooks, cut the bacon into small pieces and fry in a pan over medium heat until crispy. Remove from heat.\n" +
                                    "3. In a bowl, whisk together the eggs, grated cheese, and a generous amount of black pepper.\n" +
                                    "4. Drain the pasta, reserving 1 cup of pasta water. Do NOT rinse.\n" +
                                    "5. Add the hot pasta to the pan with the bacon (heat off). Toss to coat in the bacon fat.\n" +
                                    "6. Wait 30 seconds, then pour in the egg mixture and toss quickly, adding pasta water a little at a time until you have a creamy sauce.\n" +
                                    "7. Serve immediately with extra cheese and black pepper."
                    )
                    .prepTime(25)
                    .servings(2)
                    .mealType(Recipe.MealType.LUNCH)
                    .dietType(Recipe.DietType.NORMAL)
                    .imageUrl("https://images.unsplash.com/photo-1612874742237-6526221588e3?w=800")
                    .build());
            carbonara.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(carbonara).ingredient(pasta).quantity(200.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(carbonara).ingredient(bacon).quantity(150.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(carbonara).ingredient(egg).quantity(3.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(carbonara).ingredient(cheese).quantity(80.0).isOptional(false).build()
            ));
            recipeRepository.save(carbonara);

            // 6. FRIED RICE (LUNCH, NORMAL)
            Recipe friedRice = recipeRepository.save(Recipe.builder()
                    .name("Fried Rice")
                    .description("Quick and flavorful fried rice — great for using up leftover rice.")
                    .instructions(
                            "1. Cook the rice according to package instructions. For best results, use day-old rice.\n" +
                                    "2. Heat oil in a large pan or wok over high heat.\n" +
                                    "3. Add the carrots and frozen peas, stir-fry for 2-3 minutes until tender.\n" +
                                    "4. Push the vegetables to the side and scramble the eggs in the empty space. Once cooked, mix with the vegetables.\n" +
                                    "5. Add the rice and break up any clumps. Stir-fry everything together for 2-3 minutes.\n" +
                                    "6. Add the soy sauce and toss to coat evenly.\n" +
                                    "7. Taste and adjust seasoning. Serve hot."
                    )
                    .prepTime(20)
                    .servings(2)
                    .mealType(Recipe.MealType.LUNCH)
                    .dietType(Recipe.DietType.NORMAL)
                    .imageUrl("https://images.unsplash.com/photo-1603133872878-684f208fb84b?w=800")
                    .build());
            friedRice.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(friedRice).ingredient(rice).quantity(200.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(friedRice).ingredient(egg).quantity(2.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(friedRice).ingredient(carrot).quantity(1.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(friedRice).ingredient(frozenPeas).quantity(100.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(friedRice).ingredient(soySauce).quantity(3.0).isOptional(false).build()
            ));
            recipeRepository.save(friedRice);

            // 7. TOMATO SOUP (LUNCH, VEGAN)
            Recipe tomatoSoup = recipeRepository.save(Recipe.builder()
                    .name("20-Minute Tomato Soup")
                    .description("A simple and comforting tomato soup ready in just 20 minutes.")
                    .instructions(
                            "1. Heat oil in a large pot over medium heat. Add the chopped onion and cook for 5 minutes until softened.\n" +
                                    "2. Add the garlic and cook for 1 more minute until fragrant.\n" +
                                    "3. Add the canned tomatoes and chicken broth. Stir and bring to a boil.\n" +
                                    "4. Reduce heat and simmer for 10 minutes.\n" +
                                    "5. Use an immersion blender (or transfer to a blender in batches) and blend until smooth.\n" +
                                    "6. Season with salt and pepper to taste.\n" +
                                    "7. Serve hot, optionally with a swirl of cream or crusty bread."
                    )
                    .prepTime(20)
                    .servings(2)
                    .mealType(Recipe.MealType.LUNCH)
                    .dietType(Recipe.DietType.VEGAN)
                    .imageUrl("https://images.unsplash.com/photo-1547592166-23ac45744acd?w=800")
                    .build());
            tomatoSoup.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(tomatoSoup).ingredient(cannedTomatoes).quantity(800.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(tomatoSoup).ingredient(onion).quantity(1.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(tomatoSoup).ingredient(garlic).quantity(2.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(tomatoSoup).ingredient(chickenBroth).quantity(300.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(tomatoSoup).ingredient(heavyCream).quantity(50.0).isOptional(true).build()
            ));
            recipeRepository.save(tomatoSoup);

            // 8. CHICKEN AND RICE (DINNER, NORMAL)
            Recipe chickenRice = recipeRepository.save(Recipe.builder()
                    .name("One-Pan Chicken and Rice")
                    .description("Tender chicken cooked with fluffy rice in one pan — an easy weeknight dinner.")
                    .instructions(
                            "1. Season the chicken breasts with salt and pepper on both sides.\n" +
                                    "2. Heat oil in a large oven-safe pan over medium-high heat. Sear the chicken for 3-4 minutes per side until golden. Remove and set aside.\n" +
                                    "3. In the same pan, add the chopped onion and cook for 3 minutes until softened.\n" +
                                    "4. Add the garlic and cook for 1 minute.\n" +
                                    "5. Add the rice and stir to coat in the pan juices for 1 minute.\n" +
                                    "6. Pour in the chicken broth and bring to a boil.\n" +
                                    "7. Place the chicken back on top of the rice. Cover with a lid and reduce heat to low.\n" +
                                    "8. Cook for 18-20 minutes until the rice is fluffy and the chicken is cooked through.\n" +
                                    "9. Rest for 5 minutes before serving."
                    )
                    .prepTime(40)
                    .servings(2)
                    .mealType(Recipe.MealType.DINNER)
                    .dietType(Recipe.DietType.NORMAL)
                    .imageUrl("https://images.unsplash.com/photo-1604908176997-125f25cc6f3d?w=800")
                    .build());
            chickenRice.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(chickenRice).ingredient(chickenBreast).quantity(400.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(chickenRice).ingredient(rice).quantity(200.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(chickenRice).ingredient(chickenBroth).quantity(500.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(chickenRice).ingredient(onion).quantity(1.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(chickenRice).ingredient(garlic).quantity(2.0).isOptional(false).build()
            ));
            recipeRepository.save(chickenRice);

            // 9. BOLOGNESE (DINNER, NORMAL)
            Recipe bolognese = recipeRepository.save(Recipe.builder()
                    .name("Spaghetti Bolognese")
                    .description("A hearty and rich meat sauce served over spaghetti.")
                    .instructions(
                            "1. Heat oil in a large pan over medium heat. Add the onion and cook for 5 minutes until softened.\n" +
                                    "2. Add the garlic and cook for 1 more minute.\n" +
                                    "3. Add the ground beef and cook, breaking it up with a spoon, until browned (about 8 minutes).\n" +
                                    "4. Pour in the canned tomatoes, season with salt and pepper, and stir well.\n" +
                                    "5. Reduce heat to low and simmer for 20 minutes, stirring occasionally.\n" +
                                    "6. While the sauce simmers, cook the pasta in salted boiling water until al dente.\n" +
                                    "7. Drain the pasta and serve topped with the bolognese sauce and grated cheese."
                    )
                    .prepTime(40)
                    .servings(2)
                    .mealType(Recipe.MealType.DINNER)
                    .dietType(Recipe.DietType.NORMAL)
                    .imageUrl("https://images.unsplash.com/photo-1555949258-eb67b1ef0ceb?w=800")
                    .build());
            bolognese.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(bolognese).ingredient(pasta).quantity(200.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(bolognese).ingredient(groundBeef).quantity(300.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(bolognese).ingredient(cannedTomatoes).quantity(400.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(bolognese).ingredient(onion).quantity(1.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(bolognese).ingredient(garlic).quantity(2.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(bolognese).ingredient(cheese).quantity(50.0).isOptional(true).build()
            ));
            recipeRepository.save(bolognese);

            // 10. CREAMY CHICKEN PASTA (DINNER, NORMAL)
            Recipe creamyChickenPasta = recipeRepository.save(Recipe.builder()
                    .name("Creamy Chicken Pasta")
                    .description("Tender chicken in a rich, creamy sauce tossed with pasta.")
                    .instructions(
                            "1. Cook the pasta in salted boiling water until al dente. Reserve 1 cup of pasta water before draining.\n" +
                                    "2. Cut the chicken breast into bite-sized pieces and season with salt and pepper.\n" +
                                    "3. Heat oil in a pan over medium-high heat. Cook the chicken for 5-6 minutes until golden and cooked through. Set aside.\n" +
                                    "4. In the same pan, add the garlic and cook for 1 minute.\n" +
                                    "5. Pour in the heavy cream and bring to a gentle simmer. Cook for 3-4 minutes until slightly thickened.\n" +
                                    "6. Add the cheese and stir until melted into the sauce.\n" +
                                    "7. Add the pasta and chicken back to the pan. Toss to coat, adding pasta water as needed.\n" +
                                    "8. Season to taste and serve immediately."
                    )
                    .prepTime(30)
                    .servings(2)
                    .mealType(Recipe.MealType.DINNER)
                    .dietType(Recipe.DietType.NORMAL)
                    .imageUrl("https://images.unsplash.com/photo-1621996346565-e3dbc646d9a9?w=800")
                    .build());
            creamyChickenPasta.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(creamyChickenPasta).ingredient(pasta).quantity(200.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(creamyChickenPasta).ingredient(chickenBreast).quantity(300.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(creamyChickenPasta).ingredient(heavyCream).quantity(200.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(creamyChickenPasta).ingredient(cheese).quantity(60.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(creamyChickenPasta).ingredient(garlic).quantity(2.0).isOptional(false).build()
            ));
            recipeRepository.save(creamyChickenPasta);

            // 11. ROASTED BROCCOLI (SNACK, VEGAN)
            Recipe roastedBroccoli = recipeRepository.save(Recipe.builder()
                    .name("Roasted Garlic Broccoli")
                    .description("Crispy oven-roasted broccoli with garlic — a simple and healthy side.")
                    .instructions(
                            "1. Preheat the oven to 220°C (425°F).\n" +
                                    "2. Cut the broccoli into florets and place on a baking sheet.\n" +
                                    "3. Drizzle with oil and toss with minced garlic, salt, and pepper.\n" +
                                    "4. Spread in a single layer — don't overcrowd or it will steam instead of roast.\n" +
                                    "5. Roast for 20-25 minutes, flipping halfway, until the edges are crispy and golden.\n" +
                                    "6. Squeeze a little lemon juice over the top before serving."
                    )
                    .prepTime(25)
                    .servings(2)
                    .mealType(Recipe.MealType.SNACK)
                    .dietType(Recipe.DietType.VEGAN)
                    .imageUrl("https://images.unsplash.com/photo-1584270354949-c26b0d5b4a0c?w=800")
                    .build());
            roastedBroccoli.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(roastedBroccoli).ingredient(broccoli).quantity(400.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(roastedBroccoli).ingredient(garlic).quantity(2.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(roastedBroccoli).ingredient(lemon).quantity(1.0).isOptional(true).build()
            ));
            recipeRepository.save(roastedBroccoli);

            // 12. SPINACH AND EGG SCRAMBLE (BREAKFAST, VEGETARIAN)
            Recipe spinachEggs = recipeRepository.save(Recipe.builder()
                    .name("Spinach and Egg Scramble")
                    .description("A quick and nutritious breakfast scramble with fresh spinach.")
                    .instructions(
                            """
                                    1. Heat butter in a non-stick pan over medium heat.
                                    2. Add the spinach and cook for 1-2 minutes, stirring, until wilted.
                                    3. Crack the eggs into a bowl, add salt and pepper, and whisk well.
                                    4. Pour the egg mixture over the spinach.
                                    5. Gently stir with a spatula, pulling the eggs from the edges inward.
                                    6. Remove from heat when eggs are just set but still slightly glossy.
                                    7. Top with cheese if desired and serve immediately."""
                    )
                    .prepTime(10)
                    .servings(1)
                    .mealType(Recipe.MealType.BREAKFAST)
                    .dietType(Recipe.DietType.VEGETARIAN)
                    .imageUrl("https://images.unsplash.com/photo-1510693206972-df098062cb71?w=800")
                    .build());
            spinachEggs.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(spinachEggs).ingredient(egg).quantity(3.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(spinachEggs).ingredient(spinach).quantity(60.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(spinachEggs).ingredient(butter).quantity(10.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(spinachEggs).ingredient(cheese).quantity(30.0).isOptional(true).build()
            ));
            recipeRepository.save(spinachEggs);



// 13. LEMONY LENTIL SOUP (DINNER, VEGAN)
            Recipe lentilSoup = recipeRepository.save(Recipe.builder()
                    .name("Lemony Lentil Soup")
                    .description("A cozy, hearty vegan soup with green lentils, cumin, and a bright squeeze of lemon.")
                    .instructions("""
                1. Heat oil in a large pot over medium heat. Add the onion, carrot, and celery. Cook for 5-7 minutes until softened.
                2. Add the garlic, cumin, and paprika. Cook for 1 minute until fragrant.
                3. Stir in the green lentils and vegetable broth. Bring to a boil.
                4. Reduce heat and simmer for 25-30 minutes until lentils are tender.
                5. Squeeze in the lemon juice and season generously with salt and pepper.
                6. Use an immersion blender to partially blend the soup for a creamier texture, if desired.
                7. Serve hot with crusty bread.""")
                    .prepTime(35)
                    .servings(4)
                    .mealType(Recipe.MealType.DINNER)
                    .dietType(Recipe.DietType.VEGAN)
                    .imageUrl("https://images.unsplash.com/photo-1547592180-85f173990554?w=800")
                    .build());
            lentilSoup.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(lentilSoup).ingredient(greenLentils).quantity(300.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(lentilSoup).ingredient(vegetableBroth).quantity(1200.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(lentilSoup).ingredient(onion).quantity(1.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(lentilSoup).ingredient(carrot).quantity(2.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(lentilSoup).ingredient(celery).quantity(2.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(lentilSoup).ingredient(garlic).quantity(3.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(lentilSoup).ingredient(cumin).quantity(2.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(lentilSoup).ingredient(lemonJuice).quantity(3.0).isOptional(false).build()
            ));
            recipeRepository.save(lentilSoup);

// 14. PAD THAI (DINNER, NORMAL)
            Recipe padThai = recipeRepository.save(Recipe.builder()
                    .name("Pad Thai")
                    .description("Classic Thai stir-fried rice noodles with a tangy, sweet, and savory sauce.")
                    .instructions("""
                1. Soak the rice noodles in warm water for 20 minutes, then drain.
                2. Whisk together the soy sauce, brown sugar, and lemon juice for the sauce.
                3. Heat oil in a wok or large skillet over high heat. Add the shrimp and cook 2-3 minutes until pink. Set aside.
                4. Add the egg to the wok and scramble quickly.
                5. Add the drained noodles and pour the sauce over them. Toss well.
                6. Return shrimp to the pan. Add sesame oil and toss everything together.
                7. Serve topped with peanuts, lime wedge, and green onion.""")
                    .prepTime(30)
                    .servings(2)
                    .mealType(Recipe.MealType.DINNER)
                    .dietType(Recipe.DietType.NORMAL)
                    .imageUrl("https://images.unsplash.com/photo-1559314809-0d155014e29e?w=800")
                    .build());
            padThai.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(padThai).ingredient(riceNoodles).quantity(200.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(padThai).ingredient(shrimp).quantity(200.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(padThai).ingredient(egg).quantity(2.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(padThai).ingredient(soySauce).quantity(3.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(padThai).ingredient(brownSugar).quantity(15.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(padThai).ingredient(lemonJuice).quantity(2.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(padThai).ingredient(sesameOil).quantity(1.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(padThai).ingredient(peanutButter).quantity(2.0).isOptional(true).build(),
                    RecipeIngredient.builder().recipe(padThai).ingredient(lime).quantity(1.0).isOptional(true).build()
            ));
            recipeRepository.save(padThai);

// 15. ULTIMATE CHILI (DINNER, NORMAL)
            Recipe chili = recipeRepository.save(Recipe.builder()
                    .name("Ultimate Chili")
                    .description("A hearty, deeply flavored chili with ground beef, black beans, and bold spices.")
                    .instructions("""
                1. Heat oil in a large pot over medium-high heat. Add the onion and bell pepper, cook 5 minutes.
                2. Add the garlic and ground beef. Brown the meat, breaking it apart, about 7 minutes. Drain excess fat.
                3. Stir in the cumin, chili powder, paprika, salt, and pepper. Cook 1 minute.
                4. Add the crushed tomatoes, canned black beans, and corn. Stir well.
                5. Bring to a boil, then reduce heat and simmer uncovered for 25 minutes, stirring occasionally.
                6. Taste and adjust seasoning. Serve with shredded cheddar and sour cream.""")
                    .prepTime(40)
                    .servings(6)
                    .mealType(Recipe.MealType.DINNER)
                    .dietType(Recipe.DietType.NORMAL)
                    .imageUrl("https://images.unsplash.com/photo-1643385763566-3a5490e5e56d?w=800")
                    .build());
            chili.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(chili).ingredient(groundBeef).quantity(500.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(chili).ingredient(cannedBlackBeans).quantity(400.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(chili).ingredient(crushedTomatoes).quantity(400.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(chili).ingredient(corn).quantity(200.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(chili).ingredient(onion).quantity(1.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(chili).ingredient(bellPepper).quantity(1.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(chili).ingredient(garlic).quantity(3.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(chili).ingredient(cumin).quantity(2.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(chili).ingredient(chiliPowder).quantity(2.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(chili).ingredient(shreddedCheese).quantity(80.0).isOptional(true).build(),
                    RecipeIngredient.builder().recipe(chili).ingredient(sourceCream).quantity(50.0).isOptional(true).build()
            ));
            recipeRepository.save(chili);

// 16. SESAME NOODLES (DINNER, VEGAN)
            Recipe sesameNoodles = recipeRepository.save(Recipe.builder()
                    .name("Cold Sesame Noodles")
                    .description("Chilled noodles tossed in a rich, nutty sesame-peanut sauce — ready in 20 minutes.")
                    .instructions("""
                1. Cook pasta according to package instructions. Drain and rinse under cold water.
                2. Whisk together the peanut butter, soy sauce, sesame oil, honey, ginger, and a squeeze of lime juice.
                3. Thin the sauce with 2-3 tablespoons of warm water until pourable.
                4. Toss the cold noodles with the sauce until well coated.
                5. Serve cold or at room temperature, garnished with sesame seeds and sliced green onion.""")
                    .prepTime(20)
                    .servings(2)
                    .mealType(Recipe.MealType.DINNER)
                    .dietType(Recipe.DietType.VEGAN)
                    .imageUrl("https://images.unsplash.com/photo-1645696301019-35adcc18406f?w=800")
                    .build());
            sesameNoodles.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(sesameNoodles).ingredient(pasta).quantity(200.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(sesameNoodles).ingredient(peanutButter).quantity(4.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(sesameNoodles).ingredient(soySauce).quantity(3.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(sesameNoodles).ingredient(sesameOil).quantity(2.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(sesameNoodles).ingredient(honey).quantity(2.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(sesameNoodles).ingredient(ginger).quantity(1.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(sesameNoodles).ingredient(lime).quantity(1.0).isOptional(false).build()
            ));
            recipeRepository.save(sesameNoodles);

// 17. THAI RED CURRY SOUP (DINNER, VEGAN)
            Recipe thaiCurrySoup = recipeRepository.save(Recipe.builder()
                    .name("Thai Red Curry Soup")
                    .description("A fragrant, creamy coconut curry soup with vegetables — warming and full of flavor.")
                    .instructions("""
                1. Heat oil in a large pot over medium heat. Add the onion and cook for 3-4 minutes.
                2. Add the garlic and red curry paste. Stir and cook for 1-2 minutes until very fragrant.
                3. Pour in the coconut milk and vegetable broth. Bring to a simmer.
                4. Add the spinach and bell pepper. Cook for 5 minutes.
                5. Season with soy sauce, salt, and pepper.
                6. Serve over cooked rice with a wedge of lime.""")
                    .prepTime(25)
                    .servings(4)
                    .mealType(Recipe.MealType.DINNER)
                    .dietType(Recipe.DietType.VEGAN)
                    .imageUrl("https://images.unsplash.com/photo-1562565652-a0d8f0c59eb4?w=800")
                    .build());
            thaiCurrySoup.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(thaiCurrySoup).ingredient(coconutMilk).quantity(400.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(thaiCurrySoup).ingredient(vegetableBroth).quantity(500.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(thaiCurrySoup).ingredient(redCurryPaste).quantity(2.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(thaiCurrySoup).ingredient(onion).quantity(1.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(thaiCurrySoup).ingredient(garlic).quantity(2.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(thaiCurrySoup).ingredient(spinach).quantity(100.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(thaiCurrySoup).ingredient(bellPepper).quantity(1.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(thaiCurrySoup).ingredient(soySauce).quantity(2.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(thaiCurrySoup).ingredient(rice).quantity(200.0).isOptional(true).build(),
                    RecipeIngredient.builder().recipe(thaiCurrySoup).ingredient(lime).quantity(1.0).isOptional(true).build()
            ));
            recipeRepository.save(thaiCurrySoup);

// 18. TOMATO LENTIL CURRY (DINNER, VEGAN)
            Recipe lentilCurry = recipeRepository.save(Recipe.builder()
                    .name("Tomato Lentil Curry")
                    .description("A simple, satisfying red lentil curry with canned tomatoes and warm spices.")
                    .instructions("""
                1. Heat oil in a pan over medium heat. Add the onion and cook for 5 minutes until golden.
                2. Add garlic, ginger, cumin, and chili powder. Stir and cook 1 minute.
                3. Add the crushed tomatoes and green lentils. Stir to combine.
                4. Pour in the vegetable broth, bring to a boil, then simmer 20 minutes until lentils are soft.
                5. Stir in coconut milk and season with salt and pepper.
                6. Serve over rice with a squeeze of lemon.""")
                    .prepTime(30)
                    .servings(4)
                    .mealType(Recipe.MealType.DINNER)
                    .dietType(Recipe.DietType.VEGAN)
                    .imageUrl("https://images.unsplash.com/photo-1585937421612-70a008356fbe?w=800")
                    .build());
            lentilCurry.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(lentilCurry).ingredient(greenLentils).quantity(250.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(lentilCurry).ingredient(crushedTomatoes).quantity(400.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(lentilCurry).ingredient(coconutMilk).quantity(200.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(lentilCurry).ingredient(vegetableBroth).quantity(400.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(lentilCurry).ingredient(onion).quantity(1.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(lentilCurry).ingredient(garlic).quantity(3.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(lentilCurry).ingredient(ginger).quantity(1.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(lentilCurry).ingredient(cumin).quantity(1.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(lentilCurry).ingredient(chiliPowder).quantity(1.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(lentilCurry).ingredient(rice).quantity(200.0).isOptional(true).build()
            ));
            recipeRepository.save(lentilCurry);

// 20. CHICKEN ENCHILADA CASSEROLE (DINNER, NORMAL)
            Recipe enchiladaCasserole = recipeRepository.save(Recipe.builder()
                    .name("Chicken Enchilada Casserole")
                    .description("Layers of tortillas, shredded chicken, enchilada sauce, and melted cheese — comfort food at its best.")
                    .instructions("""
                1. Preheat oven to 190°C (375°F). Shred the cooked chicken breast.
                2. Mix the shredded chicken with half the enchilada sauce.
                3. Spread a thin layer of enchilada sauce on the bottom of a 9x13 baking dish.
                4. Layer: tortillas, chicken mixture, shredded cheese. Repeat layers twice.
                5. Pour remaining enchilada sauce over the top and sprinkle with remaining cheese.
                6. Cover with foil and bake for 20 minutes. Uncover and bake 10 more minutes until bubbly.
                7. Let rest 5 minutes before cutting. Serve with sour cream and salsa.""")
                    .prepTime(40)
                    .servings(6)
                    .mealType(Recipe.MealType.DINNER)
                    .dietType(Recipe.DietType.NORMAL)
                    .imageUrl("https://images.unsplash.com/photo-1565299585323-38d6b0865b47?w=800")
                    .build());
            enchiladaCasserole.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(enchiladaCasserole).ingredient(chickenBreast).quantity(500.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(enchiladaCasserole).ingredient(tortillas).quantity(8.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(enchiladaCasserole).ingredient(enchiladadSauce).quantity(400.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(enchiladaCasserole).ingredient(shreddedCheese).quantity(200.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(enchiladaCasserole).ingredient(sourceCream).quantity(100.0).isOptional(true).build(),
                    RecipeIngredient.builder().recipe(enchiladaCasserole).ingredient(salsaJar).quantity(100.0).isOptional(true).build()
            ));
            recipeRepository.save(enchiladaCasserole);

// 21. WHITE CHICKEN CHILI (DINNER, NORMAL)
            Recipe whiteChickenChili = recipeRepository.save(Recipe.builder()
                    .name("White Chicken Chili")
                    .description("A creamy, mild chili with white beans, chicken, and diced green chiles.")
                    .instructions("""
                1. Heat oil in a large pot over medium heat. Add the onion and cook 5 minutes.
                2. Add garlic, cumin, and chili powder. Cook 1 minute.
                3. Add the chicken broth, white beans, corn, and diced green chiles. Stir to combine.
                4. Add the shredded chicken breast. Simmer for 15 minutes.
                5. Stir in sour cream to make it creamy. Season with salt and pepper.
                6. Serve topped with shredded cheese and a squeeze of lime.""")
                    .prepTime(30)
                    .servings(4)
                    .mealType(Recipe.MealType.DINNER)
                    .dietType(Recipe.DietType.NORMAL)
                    .imageUrl("https://images.unsplash.com/photo-1608500218807-a3b8a29a5b43?w=800")
                    .build());
            whiteChickenChili.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(whiteChickenChili).ingredient(chickenBreast).quantity(400.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(whiteChickenChili).ingredient(cannedWhiteBeans).quantity(400.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(whiteChickenChili).ingredient(corn).quantity(200.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(whiteChickenChili).ingredient(diceGreenChiles).quantity(100.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(whiteChickenChili).ingredient(chickenBroth).quantity(600.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(whiteChickenChili).ingredient(onion).quantity(1.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(whiteChickenChili).ingredient(garlic).quantity(2.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(whiteChickenChili).ingredient(cumin).quantity(2.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(whiteChickenChili).ingredient(sourceCream).quantity(100.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(whiteChickenChili).ingredient(shreddedCheese).quantity(80.0).isOptional(true).build()
            ));
            recipeRepository.save(whiteChickenChili);

// 22. CHICKEN WILD RICE SOUP (DINNER, NORMAL)
            Recipe wildRiceSoup = recipeRepository.save(Recipe.builder()
                    .name("Chicken Wild Rice Soup")
                    .description("A thick, creamy soup packed with tender chicken, wild rice, and vegetables.")
                    .instructions("""
                1. Heat oil in a large pot over medium heat. Cook onion, carrot, and celery for 6-7 minutes.
                2. Add garlic and cook 1 minute. Stir in flour to coat the vegetables (creates a roux).
                3. Slowly pour in the chicken broth, whisking to prevent lumps.
                4. Add the wild rice and bring to a boil. Reduce heat, cover, and simmer 40 minutes.
                5. Add the shredded chicken breast and heavy cream. Simmer 5 more minutes.
                6. Season with salt, pepper, and oregano. Serve hot.""")
                    .prepTime(55)
                    .servings(6)
                    .mealType(Recipe.MealType.DINNER)
                    .dietType(Recipe.DietType.NORMAL)
                    .imageUrl("https://images.unsplash.com/photo-1603106037897-51c24fe6cf69?w=800")
                    .build());
            wildRiceSoup.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(wildRiceSoup).ingredient(chickenBreast).quantity(400.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(wildRiceSoup).ingredient(wildRice).quantity(180.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(wildRiceSoup).ingredient(chickenBroth).quantity(1200.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(wildRiceSoup).ingredient(heavyCream).quantity(200.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(wildRiceSoup).ingredient(onion).quantity(1.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(wildRiceSoup).ingredient(carrot).quantity(2.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(wildRiceSoup).ingredient(celery).quantity(2.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(wildRiceSoup).ingredient(garlic).quantity(2.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(wildRiceSoup).ingredient(flour).quantity(30.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(wildRiceSoup).ingredient(oregano).quantity(1.0).isOptional(false).build()
            ));
            recipeRepository.save(wildRiceSoup);

// 23. LASAGNA (DINNER, NORMAL)
            Recipe lasagna = recipeRepository.save(Recipe.builder()
                    .name("Classic Lasagna")
                    .description("Layers of pasta, rich meat sauce, creamy ricotta, and bubbly mozzarella.")
                    .instructions("""
                1. Preheat oven to 190°C (375°F). Cook lasagna noodles per package and set aside.
                2. Brown the ground beef with onion and garlic in a large pan. Drain fat.
                3. Add the canned tomatoes, oregano, salt, and pepper. Simmer 15 minutes for the meat sauce.
                4. Mix ricotta with egg, salt, and pepper in a bowl.
                5. Assemble: spread meat sauce on the bottom of a baking dish, layer noodles, ricotta mixture, cheese, meat sauce. Repeat.
                6. Finish with noodles, remaining sauce, and a generous layer of cheese.
                7. Cover with foil and bake 40 minutes. Uncover and bake 15 more minutes until golden.
                8. Rest for 15 minutes before serving.""")
                    .prepTime(75)
                    .servings(8)
                    .mealType(Recipe.MealType.DINNER)
                    .dietType(Recipe.DietType.NORMAL)
                    .imageUrl("https://images.unsplash.com/photo-1574894709920-11b28e7367e3?w=800")
                    .build());
            lasagna.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(lasagna).ingredient(lasagnaNoodles).quantity(250.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(lasagna).ingredient(groundBeef).quantity(500.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(lasagna).ingredient(cannedTomatoes).quantity(800.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(lasagna).ingredient(ricotta).quantity(400.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(lasagna).ingredient(cheese).quantity(300.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(lasagna).ingredient(onion).quantity(1.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(lasagna).ingredient(garlic).quantity(3.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(lasagna).ingredient(egg).quantity(1.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(lasagna).ingredient(oregano).quantity(2.0).isOptional(false).build()
            ));
            recipeRepository.save(lasagna);

// 24. HUMMUS (SNACK, VEGAN)
            Recipe hummus = recipeRepository.save(Recipe.builder()
                    .name("Classic Hummus")
                    .description("Creamy, smooth homemade hummus made with chickpeas, tahini, and lemon — ready in 10 minutes.")
                    .instructions("""
                1. Drain and rinse the canned chickpeas. Reserve a few for garnish.
                2. Add chickpeas, tahini, lemon juice, garlic, salt, and olive oil to a food processor.
                3. Blend for 1 minute. With the motor running, add 2-4 tablespoons of cold water until smooth and creamy.
                4. Taste and adjust seasoning — more lemon or salt as needed.
                5. Spread in a bowl, drizzle with olive oil, and sprinkle with paprika.
                6. Serve with pita bread or vegetables for dipping.""")
                    .prepTime(10)
                    .servings(6)
                    .mealType(Recipe.MealType.SNACK)
                    .dietType(Recipe.DietType.VEGAN)
                    .imageUrl("https://images.unsplash.com/photo-1585937421612-70a008356fbe?w=800")
                    .build());
            hummus.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(hummus).ingredient(cannedChickpeas).quantity(400.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(hummus).ingredient(tahini).quantity(4.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(hummus).ingredient(lemonJuice).quantity(3.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(hummus).ingredient(garlic).quantity(1.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(hummus).ingredient(oil).quantity(30.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(hummus).ingredient(paprika).quantity(1.0).isOptional(true).build()
            ));
            recipeRepository.save(hummus);

// 25. NO-BAKE ENERGY BITES (SNACK, VEGETARIAN)
            Recipe energyBites = recipeRepository.save(Recipe.builder()
                    .name("No-Bake Energy Bites")
                    .description("Chewy, chocolatey oat bites packed with peanut butter and honey — no oven needed.")
                    .instructions("""
                1. Combine the rolled oats, peanut butter, honey, chocolate chips, and vanilla extract in a large bowl.
                2. Mix until everything is well combined. If the mixture is too sticky, refrigerate for 15 minutes.
                3. Roll into 1-inch balls using slightly damp hands.
                4. Place on a parchment-lined baking sheet and refrigerate for at least 30 minutes.
                5. Store in the fridge for up to 1 week.""")
                    .prepTime(15)
                    .servings(20)
                    .mealType(Recipe.MealType.SNACK)
                    .dietType(Recipe.DietType.VEGETARIAN)
                    .imageUrl("https://images.unsplash.com/photo-1604329760661-e71dc83f8f26?w=800")
                    .build());
            energyBites.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(energyBites).ingredient(rolledOats).quantity(200.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(energyBites).ingredient(peanutButter).quantity(6.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(energyBites).ingredient(honey).quantity(4.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(energyBites).ingredient(chocolateChips).quantity(80.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(energyBites).ingredient(vanillaExtract).quantity(1.0).isOptional(false).build()
            ));
            recipeRepository.save(energyBites);

// 26. PEANUT BUTTER COOKIES (DESSERT, VEGETARIAN)
            Recipe pbCookies = recipeRepository.save(Recipe.builder()
                    .name("Peanut Butter Cookies")
                    .description("Soft, chewy, 3-ingredient peanut butter cookies with no flour needed.")
                    .instructions("""
                1. Preheat oven to 180°C (350°F). Line a baking sheet with parchment paper.
                2. Mix together the peanut butter, sugar, and egg until a dough forms.
                3. Roll the dough into 1-inch balls and place on the baking sheet.
                4. Press each ball with a fork to create the classic crosshatch pattern.
                5. Bake for 10-12 minutes until the edges are just set (they will firm up as they cool).
                6. Let cool on the baking sheet for 5 minutes before transferring.""")
                    .prepTime(20)
                    .servings(2)
                    .mealType(Recipe.MealType.SNACK)
                    .dietType(Recipe.DietType.VEGETARIAN)
                    .imageUrl("https://images.unsplash.com/photo-1558961363-fa8fdf82db35?w=800")
                    .build());
            pbCookies.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(pbCookies).ingredient(peanutButter).quantity(8.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(pbCookies).ingredient(sugar).quantity(200.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(pbCookies).ingredient(egg).quantity(1.0).isOptional(false).build()
            ));
            recipeRepository.save(pbCookies);

// 27. VEGAN BROWNIES (DESSERT, VEGAN)
            Recipe veganBrownies = recipeRepository.save(Recipe.builder()
                    .name("Vegan Brownies")
                    .description("Fudgy, rich chocolate brownies — completely dairy-free and egg-free.")
                    .instructions("""
                1. Preheat oven to 180°C (350°F). Line an 8x8 baking pan with parchment paper.
                2. Whisk together the flour, cocoa powder, sugar, and salt in a large bowl.
                3. In a separate bowl, mix oil, vanilla extract, and 180ml of water.
                4. Pour wet ingredients into dry and stir until just combined.
                5. Fold in the chocolate chips.
                6. Pour batter into the prepared pan and spread evenly.
                7. Bake for 25-30 minutes until a toothpick comes out with moist crumbs.
                8. Cool completely before cutting into squares.""")
                    .prepTime(40)
                    .servings(16)
                    .mealType(Recipe.MealType.SNACK)
                    .dietType(Recipe.DietType.VEGAN)
                    .imageUrl("https://images.unsplash.com/photo-1606313564200-e75d5e30476c?w=800")
                    .build());
            veganBrownies.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(veganBrownies).ingredient(flour).quantity(120.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(veganBrownies).ingredient(cocoaPowder).quantity(60.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(veganBrownies).ingredient(sugar).quantity(200.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(veganBrownies).ingredient(oil).quantity(120.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(veganBrownies).ingredient(chocolateChips).quantity(100.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(veganBrownies).ingredient(vanillaExtract).quantity(1.0).isOptional(false).build()
            ));
            recipeRepository.save(veganBrownies);

// 28. HEALTHY BANANA BREAD (BREAKFAST, VEGETARIAN)
            Recipe bananaBread = recipeRepository.save(Recipe.builder()
                    .name("Healthy Banana Bread")
                    .description("Moist, naturally sweetened banana bread made with oats and ripe bananas.")
                    .instructions("""
                1. Preheat oven to 175°C (350°F). Grease a loaf pan.
                2. Mash 3 ripe bananas in a large bowl until smooth.
                3. Whisk in the eggs, oil, honey, and vanilla extract.
                4. Add the flour, rolled oats, baking powder, and salt. Stir until just combined.
                5. Pour into the loaf pan and bake for 55-65 minutes until a toothpick comes out clean.
                6. Cool for 10 minutes in the pan, then transfer to a wire rack.""")
                    .prepTime(70)
                    .servings(10)
                    .mealType(Recipe.MealType.BREAKFAST)
                    .dietType(Recipe.DietType.VEGETARIAN)
                    .imageUrl("https://images.unsplash.com/photo-1605135870498-d5f5d2cdacbb?w=800")
                    .build());
            bananaBread.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(bananaBread).ingredient(banana).quantity(3.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(bananaBread).ingredient(flour).quantity(180.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(bananaBread).ingredient(rolledOats).quantity(50.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(bananaBread).ingredient(egg).quantity(2.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(bananaBread).ingredient(oil).quantity(60.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(bananaBread).ingredient(honey).quantity(3.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(bananaBread).ingredient(bakingPowder).quantity(1.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(bananaBread).ingredient(vanillaExtract).quantity(1.0).isOptional(false).build()
            ));
            recipeRepository.save(bananaBread);

// 29. CATALAN CHICKPEAS AND SPINACH (DINNER, VEGAN)
            Recipe catalanChickpeas = recipeRepository.save(Recipe.builder()
                    .name("Catalan Chickpeas and Spinach")
                    .description("A Spanish-inspired tapas dish with garlicky chickpeas, wilted spinach, and smoky paprika.")
                    .instructions("""
                1. Heat oil in a large skillet over medium heat. Add garlic and cook 1-2 minutes until golden.
                2. Add the canned chickpeas (drained) and paprika. Stir and cook for 3 minutes.
                3. Add the crushed tomatoes and cook for 5 minutes until the sauce thickens slightly.
                4. Add the spinach in handfuls, stirring until fully wilted.
                5. Season with salt and pepper. Squeeze lemon juice over the top before serving.
                6. Serve warm as a side dish or over crusty bread.""")
                    .prepTime(20)
                    .servings(4)
                    .mealType(Recipe.MealType.DINNER)
                    .dietType(Recipe.DietType.VEGAN)
                    .imageUrl("https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=800")
                    .build());
            catalanChickpeas.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(catalanChickpeas).ingredient(cannedChickpeas).quantity(400.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(catalanChickpeas).ingredient(spinach).quantity(200.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(catalanChickpeas).ingredient(crushedTomatoes).quantity(200.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(catalanChickpeas).ingredient(garlic).quantity(3.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(catalanChickpeas).ingredient(paprika).quantity(2.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(catalanChickpeas).ingredient(lemon).quantity(1.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(catalanChickpeas).ingredient(oil).quantity(30.0).isOptional(false).build()
            ));
            recipeRepository.save(catalanChickpeas);

// 30. QUINOA TORTILLA SOUP (DINNER, VEGAN)
            Recipe quinoaTortillaSoup = recipeRepository.save(Recipe.builder()
                    .name("Quinoa Tortilla Soup")
                    .description("A bold, smoky Mexican-inspired soup with quinoa, black beans, and corn.")
                    .instructions("""
                1. Heat oil in a large pot over medium heat. Add onion and bell pepper, cook 5 minutes.
                2. Add garlic, cumin, and chili powder. Cook 1 minute.
                3. Add the crushed tomatoes, black beans, corn, quinoa, and vegetable broth.
                4. Bring to a boil, reduce heat, and simmer for 20 minutes until quinoa is cooked.
                5. Season with salt and pepper. Squeeze lime juice over the top.
                6. Serve topped with shredded cheese, sour cream, and crushed tortilla chips.""")
                    .prepTime(35)
                    .servings(6)
                    .mealType(Recipe.MealType.DINNER)
                    .dietType(Recipe.DietType.VEGAN)
                    .imageUrl("https://images.unsplash.com/photo-1611071855975-4cba47c64e36?w=800")
                    .build());
            quinoaTortillaSoup.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(quinoaTortillaSoup).ingredient(quinoa).quantity(150.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(quinoaTortillaSoup).ingredient(cannedBlackBeans).quantity(400.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(quinoaTortillaSoup).ingredient(corn).quantity(200.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(quinoaTortillaSoup).ingredient(crushedTomatoes).quantity(400.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(quinoaTortillaSoup).ingredient(vegetableBroth).quantity(800.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(quinoaTortillaSoup).ingredient(onion).quantity(1.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(quinoaTortillaSoup).ingredient(bellPepper).quantity(1.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(quinoaTortillaSoup).ingredient(garlic).quantity(3.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(quinoaTortillaSoup).ingredient(cumin).quantity(2.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(quinoaTortillaSoup).ingredient(chiliPowder).quantity(1.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(quinoaTortillaSoup).ingredient(lime).quantity(1.0).isOptional(false).build()
            ));
            recipeRepository.save(quinoaTortillaSoup);

// 31. TUNA CASSEROLE (DINNER, NORMAL)
            Recipe tunaCasserole = recipeRepository.save(Recipe.builder()
                    .name("Lighter Tuna Casserole")
                    .description("A comforting weeknight casserole with canned tuna, pasta, and a creamy sauce — no canned soup required.")
                    .instructions("""
                1. Preheat oven to 190°C (375°F). Cook pasta until just al dente and drain.
                2. In a saucepan, melt butter over medium heat. Whisk in flour and cook 1 minute.
                3. Slowly whisk in milk until smooth. Cook, stirring, until thickened (5 minutes). Season with salt and pepper.
                4. Stir in the drained tuna, frozen peas, and half the cheese.
                5. Mix in the cooked pasta. Transfer to a greased baking dish.
                6. Top with remaining cheese and breadcrumbs.
                7. Bake for 20-25 minutes until golden and bubbly.""")
                    .prepTime(40)
                    .servings(6)
                    .mealType(Recipe.MealType.DINNER)
                    .dietType(Recipe.DietType.NORMAL)
                    .imageUrl("https://images.unsplash.com/photo-1621852004158-f3bc188ace2d?w=800")
                    .build());
            tunaCasserole.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(tunaCasserole).ingredient(tuna).quantity(300.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(tunaCasserole).ingredient(pasta).quantity(250.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(tunaCasserole).ingredient(frozenPeas).quantity(150.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(tunaCasserole).ingredient(milk).quantity(400.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(tunaCasserole).ingredient(butter).quantity(30.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(tunaCasserole).ingredient(flour).quantity(30.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(tunaCasserole).ingredient(cheese).quantity(150.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(tunaCasserole).ingredient(breadcrumbs).quantity(50.0).isOptional(true).build()
            ));
            recipeRepository.save(tunaCasserole);

// 32. SHEPHERD'S PIE (DINNER, NORMAL)
            Recipe shepherdsPie = recipeRepository.save(Recipe.builder()
                    .name("Shepherd's Pie")
                    .description("Classic comfort food with a savory ground beef filling topped with fluffy mashed potatoes.")
                    .instructions("""
                1. Boil the potatoes until tender, about 15 minutes. Drain, then mash with butter and milk. Season and set aside.
                2. Preheat oven to 200°C (400°F).
                3. Cook the ground beef in an oven-safe skillet over medium-high heat until browned. Drain fat.
                4. Add onion, carrot, and garlic. Cook 5 minutes until softened.
                5. Stir in the frozen peas, canned tomatoes, and oregano. Season well. Simmer 5 minutes.
                6. Spread the mashed potatoes evenly over the meat filling.
                7. Bake for 20-25 minutes until the top is golden.""")
                    .prepTime(55)
                    .servings(6)
                    .mealType(Recipe.MealType.DINNER)
                    .dietType(Recipe.DietType.NORMAL)
                    .imageUrl("https://images.unsplash.com/photo-1619895092538-128341789043?w=800")
                    .build());
            shepherdsPie.setRecipeIngredients(List.of(
                    RecipeIngredient.builder().recipe(shepherdsPie).ingredient(groundBeef).quantity(500.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(shepherdsPie).ingredient(potato).quantity(4.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(shepherdsPie).ingredient(frozenPeas).quantity(150.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(shepherdsPie).ingredient(carrot).quantity(2.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(shepherdsPie).ingredient(onion).quantity(1.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(shepherdsPie).ingredient(garlic).quantity(2.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(shepherdsPie).ingredient(cannedTomatoes).quantity(200.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(shepherdsPie).ingredient(butter).quantity(40.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(shepherdsPie).ingredient(milk).quantity(100.0).isOptional(false).build(),
                    RecipeIngredient.builder().recipe(shepherdsPie).ingredient(oregano).quantity(1.0).isOptional(false).build()
            ));
            recipeRepository.save(shepherdsPie);


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