export interface RecipeIngredient {
    ingredientName: string;
    quantity: number;
    unit: string;
    isOptional: boolean;
}

export interface Recipe {
    id: number;
    name: string;
    description: string;
    instructions: string;
    prepTime: number;
    servings: number;
    mealType: string;
    dietType: string;
    imageUrl: string | null;
    ingredients: RecipeIngredient[];
    missingIngredients: string[];
    allergenWarnings: string[];
}

export interface FridgeItem {
    id: number;
    ingredientId: number;
    ingredientName: string;
    category: string;
    isPantryItem: boolean;
}

export interface Ingredient {
    id: number;
    name: string;
    category: string;
    isPantryItem: boolean;
}

export interface Allergy {
    id: number;
    name: string;
}

export interface Preferences {
    isVegetarian: boolean;
    isVegan: boolean;
}

export interface ScannedIngredient {
    ingredientId: number;
    receiptName: string;
    mappedName: string;
}