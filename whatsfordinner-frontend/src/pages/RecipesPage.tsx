import { useState, useEffect } from "react";
import { useAuth } from "../context/AuthContext.tsx";
import axios from "axios";

interface RecipeIngredient {
    ingredientName: string;
    quantity: number;
    unit: string;
    isOptional: boolean;
}

interface Recipe {
    id: number;
    name: string;
    description: string;
    instructions: string;
    prepTime: number;
    servings: number;
    mealType: string;
    dietType: string;
    ingredients: RecipeIngredient[];
    missingIngredients: string[];
    allergenWarnings: string[];
}

const MEAL_TYPES = ["BREAKFAST", "LUNCH", "DINNER", "SNACK"];
const DIET_TYPES = ["NORMAL", "VEGETARIAN", "VEGAN"];

const MEAL_LABELS: Record<string, string> = {
    BREAKFAST: "Breakfast",
    LUNCH: "Lunch",
    DINNER: "Dinner",
    SNACK: "Snack",
};

const DIET_LABELS: Record<string, string> = {
    NORMAL: "Normal",
    VEGETARIAN: "Vegetarian",
    VEGAN: "Vegan",
};

const UNIT_LABELS: Record<string, string> = {
    GRAMS: "g",
    ML: "ml",
    PIECES: "",
    TEASPOON: "tsp",
    TABLESPOON: "tbsp",
    TASTE: "to taste",
};

function formatQuantity(quantity: number, unit: string): string {
    if (unit === "TASTE") return "to taste";
    const label = UNIT_LABELS[unit] ?? unit.toLowerCase();
    const qty = quantity % 1 === 0 ? quantity.toFixed(0) : quantity.toString();
    return label ? `${qty} ${label}` : qty;
}

export default function RecipesPage() {
    const { token } = useAuth();
    const [recipes, setRecipes] = useState<Recipe[]>([]);
    const [loading, setLoading] = useState(false);
    const [searched, setSearched] = useState(false);
    const [searchError, setSearchError] = useState("");
    const [expandedId, setExpandedId] = useState<number | null>(null);
    const [prefsLoading, setPrefsLoading] = useState(true);
    const [prefsError, setPrefsError] = useState("");

    const [mealType, setMealType] = useState<string | null>(null);
    const [dietType, setDietType] = useState<string | null>(null);
    const [maxMissing, setMaxMissing] = useState<number>(0);

    const headers = { Authorization: `Bearer ${token}` };

    useEffect(() => {
        const fetchPrefs = async () => {
            try {
                const res = await axios.get("http://localhost:8080/api/profile/preferences", { headers });
                const { isVegan, isVegetarian } = res.data;
                if (isVegan) setDietType("VEGAN");
                else if (isVegetarian) setDietType("VEGETARIAN");
            } catch {
                setPrefsError("Couldn't load your dietary preferences. You can still search manually.");
            } finally {
                setPrefsLoading(false);
            }
        };
        fetchPrefs();
    }, []);

    const search = async () => {
        setLoading(true);
        setSearched(true);
        setSearchError("");
        setExpandedId(null);
        try {
            const res = await axios.post(
                "http://localhost:8080/api/recipes/search",
                {
                    mealType: mealType ?? undefined,
                    dietType: dietType ?? "NORMAL",
                    maxMissingIngredients: maxMissing,
                },
                { headers }
            );
            setRecipes(res.data);
        } catch {
            setRecipes([]);
            setSearchError("Failed to search recipes. Please try again.");
        } finally {
            setLoading(false);
        }
    };

    const toggleDietType = (type: string) => {
        setDietType(prev => prev === type ? null : type);
    };

    const toggleExpand = (id: number) => {
        setExpandedId(prev => prev === id ? null : id);
    };

    return (
        <div>
            {/* Header */}
            <div className="mb-6">
                <h1 className="text-2xl font-serif font-bold text-gray-800">Recipes</h1>
                <p className="text-sm text-gray-400 mt-0.5">Find what you can cook with what you have</p>
            </div>

            {/* Preferences warning */}
            {prefsError && (
                <div className="bg-amber-50 border border-amber-200 text-amber-700 text-sm rounded-xl p-3 mb-4 flex items-start gap-2">
                    <svg className="w-4 h-4 mt-0.5 flex-shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                        <path strokeLinecap="round" strokeLinejoin="round" d="M12 9v2m0 4h.01M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z" />
                    </svg>
                    {prefsError}
                </div>
            )}

            {/* Filters */}
            <div className="bg-white rounded-2xl shadow-sm p-5 mb-6">
                <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-4">

                    {/* Meal type */}
                    <div>
                        <label className="text-xs font-semibold text-gray-400 uppercase tracking-wide block mb-2">
                            Meal Type
                        </label>
                        <div className="flex flex-wrap gap-2">
                            {MEAL_TYPES.map(type => (
                                <button
                                    key={type}
                                    onClick={() => setMealType(prev => prev === type ? null : type)}
                                    className={`px-3 py-1.5 rounded-lg text-xs font-medium transition-all ${
                                        mealType === type
                                            ? "bg-primary-500 text-white"
                                            : "bg-gray-100 text-gray-500 hover:bg-gray-200"
                                    }`}
                                >
                                    {MEAL_LABELS[type]}
                                </button>
                            ))}
                        </div>
                    </div>

                    {/* Diet type */}
                    <div>
                        <label className="text-xs font-semibold text-gray-400 uppercase tracking-wide block mb-2">
                            Diet
                            {dietType && !prefsError && (
                                <span className="ml-2 text-primary-400 normal-case font-normal">from your profile</span>
                            )}
                        </label>
                        <div className="flex flex-wrap gap-2">
                            {DIET_TYPES.map(type => (
                                <button
                                    key={type}
                                    onClick={() => toggleDietType(type)}
                                    disabled={prefsLoading}
                                    className={`px-3 py-1.5 rounded-lg text-xs font-medium transition-all ${
                                        dietType === type
                                            ? "bg-primary-500 text-white"
                                            : "bg-gray-100 text-gray-500 hover:bg-gray-200"
                                    } disabled:opacity-40`}
                                >
                                    {DIET_LABELS[type]}
                                </button>
                            ))}
                        </div>
                    </div>

                    {/* Max missing */}
                    <div>
                        <label className="text-xs font-semibold text-gray-400 uppercase tracking-wide block mb-2">
                            Missing Ingredients — up to {maxMissing}
                        </label>
                        <input
                            type="range"
                            min={0}
                            max={5}
                            value={maxMissing}
                            onChange={e => setMaxMissing(Number(e.target.value))}
                            className="w-full accent-primary-500"
                        />
                        <div className="flex justify-between text-xs text-gray-300 mt-1">
                            <span>Only what I have</span>
                            <span>Up to 5 missing</span>
                        </div>
                    </div>
                </div>

                <button
                    onClick={search}
                    disabled={loading || prefsLoading}
                    className="w-full h-11 bg-gradient-to-r from-primary-500 to-primary-600 text-white rounded-xl text-sm font-semibold shadow-md hover:shadow-lg transition-all disabled:opacity-60"
                >
                    {loading ? "Searching..." : "Find Recipes"}
                </button>
            </div>

            {/* Search error */}
            {searchError && (
                <div className="bg-red-50 border border-red-200 text-red-600 text-sm rounded-xl p-4 mb-4 flex items-center gap-2">
                    <svg className="w-4 h-4 flex-shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                        <path strokeLinecap="round" strokeLinejoin="round" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                    {searchError}
                </div>
            )}

            {/* Loading */}
            {loading && (
                <div className="flex items-center justify-center py-20">
                    <div className="w-8 h-8 border-4 border-primary-200 border-t-primary-500 rounded-full animate-spin" />
                </div>
            )}

            {/* Empty state — before search */}
            {!loading && !searched && (
                <div className="flex flex-col items-center justify-center py-16 text-center">
                    <div className="w-16 h-16 bg-gray-100 rounded-2xl flex items-center justify-center mb-4">
                        <svg className="w-8 h-8 text-gray-300" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
                            <path strokeLinecap="round" strokeLinejoin="round" d="M12 6.042A8.967 8.967 0 006 3.75c-1.052 0-2.062.18-3 .512v14.25A8.987 8.987 0 016 18c2.305 0 4.408.867 6 2.292m0-14.25a8.966 8.966 0 016-2.292c1.052 0 2.062.18 3 .512v14.25A8.987 8.987 0 0018 18a8.967 8.967 0 00-6 2.292m0-14.25v14.25" />
                        </svg>
                    </div>
                    <p className="text-gray-500 font-medium">Set your filters and search</p>
                    <p className="text-sm text-gray-400 mt-1">We'll match recipes to what's in your fridge</p>
                </div>
            )}

            {/* No results */}
            {!loading && searched && recipes.length === 0 && !searchError && (
                <div className="flex flex-col items-center justify-center py-16 text-center">
                    <div className="w-16 h-16 bg-gray-100 rounded-2xl flex items-center justify-center mb-4">
                        <svg className="w-8 h-8 text-gray-300" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
                            <path strokeLinecap="round" strokeLinejoin="round" d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                        </svg>
                    </div>
                    <p className="text-gray-500 font-medium">No recipes found</p>
                    <p className="text-sm text-gray-400 mt-1">Try allowing more missing ingredients or changing the filters</p>
                </div>
            )}

            {/* Results */}
            {!loading && recipes.length > 0 && (
                <div className="space-y-3">
                    <p className="text-xs text-gray-400 px-1">
                        {recipes.length} recipe{recipes.length !== 1 ? "s" : ""} found
                    </p>
                    {recipes.map(recipe => {
                        const isExpanded = expandedId === recipe.id;
                        const canMake = recipe.missingIngredients.length === 0;
                        const hasAllergens = recipe.allergenWarnings?.length > 0;

                        return (
                            <div key={recipe.id} className="bg-white rounded-2xl shadow-sm overflow-hidden">
                                <button
                                    onClick={() => toggleExpand(recipe.id)}
                                    className="w-full px-5 py-4 flex items-center justify-between text-left"
                                >
                                    <div className="flex-1 min-w-0">
                                        <div className="flex items-center gap-2 mb-1 flex-wrap">
                                            <h3 className="text-sm font-semibold text-gray-800">{recipe.name}</h3>
                                            {canMake ? (
                                                <span className="px-2 py-0.5 bg-green-50 text-green-600 text-xs font-medium rounded-full">
                                                    Ready to cook
                                                </span>
                                            ) : (
                                                <span className="px-2 py-0.5 bg-amber-50 text-amber-600 text-xs font-medium rounded-full">
                                                    {recipe.missingIngredients.length} missing
                                                </span>
                                            )}
                                            {hasAllergens && (
                                                <span className="px-2 py-0.5 bg-red-50 text-red-500 text-xs font-medium rounded-full flex items-center gap-1">
                                                    <svg className="w-3 h-3" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2.5}>
                                                        <path strokeLinecap="round" strokeLinejoin="round" d="M12 9v2m0 4h.01M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z" />
                                                    </svg>
                                                    Contains allergen
                                                </span>
                                            )}
                                        </div>
                                        <div className="flex items-center gap-3 text-xs text-gray-400">
                                            <span>{MEAL_LABELS[recipe.mealType]}</span>
                                            <span>·</span>
                                            <span>{DIET_LABELS[recipe.dietType]}</span>
                                            <span>·</span>
                                            <span>{recipe.prepTime} min</span>
                                            <span>·</span>
                                            <span>{recipe.servings} servings</span>
                                        </div>
                                    </div>
                                    <svg
                                        className={`w-4 h-4 text-gray-400 flex-shrink-0 ml-3 transition-transform ${isExpanded ? "rotate-180" : ""}`}
                                        fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}
                                    >
                                        <path strokeLinecap="round" strokeLinejoin="round" d="M19 9l-7 7-7-7" />
                                    </svg>
                                </button>

                                {isExpanded && (
                                    <div className="px-5 pb-5 border-t border-gray-50">
                                        {recipe.description && (
                                            <p className="text-sm text-gray-500 mt-4 mb-4">{recipe.description}</p>
                                        )}

                                        {/* Allergen warning banner */}
                                        {hasAllergens && (
                                            <div className="bg-red-50 border border-red-100 rounded-xl p-3 mb-4">
                                                <p className="text-xs font-semibold text-red-600 mb-1">⚠ Allergen warning</p>
                                                <p className="text-xs text-red-500">
                                                    This recipe contains: {recipe.allergenWarnings.join(", ")}
                                                </p>
                                            </div>
                                        )}

                                        {recipe.missingIngredients.length > 0 && (
                                            <div className="bg-amber-50 rounded-xl p-3 mb-4">
                                                <p className="text-xs font-semibold text-amber-700 mb-1">You're missing:</p>
                                                <p className="text-xs text-amber-600">{recipe.missingIngredients.join(", ")}</p>
                                            </div>
                                        )}

                                        <div className="mb-4">
                                            <p className="text-xs font-semibold text-gray-400 uppercase tracking-wide mb-2">Ingredients</p>
                                            <div className="space-y-1.5">
                                                {recipe.ingredients.map((ing, idx) => (
                                                    <div key={idx} className="flex items-center justify-between">
                                                        <span className={`text-sm capitalize ${ing.isOptional ? "text-gray-400" : "text-gray-700"}`}>
                                                            {ing.ingredientName}
                                                            {ing.isOptional && <span className="text-xs ml-1">(optional)</span>}
                                                        </span>
                                                        <span className="text-sm text-gray-400 ml-4 flex-shrink-0">
                                                            {formatQuantity(ing.quantity, ing.unit)}
                                                        </span>
                                                    </div>
                                                ))}
                                            </div>
                                        </div>

                                        <div>
                                            <p className="text-xs font-semibold text-gray-400 uppercase tracking-wide mb-2">Instructions</p>
                                            <p className="text-sm text-gray-600 leading-relaxed">{recipe.instructions}</p>
                                        </div>
                                    </div>
                                )}
                            </div>
                        );
                    })}
                </div>
            )}
        </div>
    );
}