import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import apiClient from "../api/client";
import type { Recipe } from "../types";

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
    const rounded = Math.round(quantity * 100) / 100;
    const qty = rounded % 1 === 0 ? rounded.toFixed(0) : rounded.toString();
    return label ? `${qty} ${label}` : qty;
}

const MEAL_LABELS: Record<string, string> = {
    BREAKFAST: "Breakfast", LUNCH: "Lunch", DINNER: "Dinner", SNACK: "Snack",
};
const DIET_LABELS: Record<string, string> = {
    NORMAL: "Normal", VEGETARIAN: "Vegetarian", VEGAN: "Vegan",
};

export default function RecipePage() {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();

    const [recipe, setRecipe] = useState<Recipe | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [multiplier, setMultiplier] = useState<number>(1);

    const [showModal, setShowModal] = useState(false);
    const [consumedIds, setConsumedIds] = useState<Set<string>>(new Set());
    const [saving, setSaving] = useState(false);
    const [saveError, setSaveError] = useState("");

    useEffect(() => {
        const fetch = async () => {
            try {
                const res = await apiClient.get(`/api/recipes/${id}`);
                setRecipe(res.data);
            } catch {
                setError("Could not load this recipe. Please try again.");
            } finally {
                setLoading(false);
            }
        };
        fetch();
    }, [id]);

    const openModal = () => {
        if (!recipe) return;
        const missing = new Set(recipe.missingIngredients);
        const preSelected = new Set(
            recipe.ingredients
                .filter(i => !i.isOptional && !missing.has(i.ingredientName))
                .map(i => i.ingredientName)
        );
        setConsumedIds(preSelected);
        setSaveError("");
        setShowModal(true);
    };

    const toggleConsumed = (name: string) => {
        setConsumedIds(prev => {
            const next = new Set(prev);
            next.has(name) ? next.delete(name) : next.add(name);
            return next;
        });
    };

    const confirmCooked = async () => {
        if (!recipe) return;
        setSaving(true);
        setSaveError("");
        try {
            await apiClient.post(
                `/api/recipes/${recipe.id}/cooked`,
                Array.from(consumedIds)
            );
            setShowModal(false);
            navigate("/recipes");
        } catch {
            setSaveError("Failed to update your fridge. Please try again.");
        } finally {
            setSaving(false);
        }
    };

    const cookableIngredients = recipe?.ingredients.filter(i => !i.isOptional) ?? [];

    const scaleQuantity = (quantity: number): number => {
        return quantity * multiplier;
    };

    if (loading) {
        return (
            <div className="flex items-center justify-center py-32">
                <div className="w-8 h-8 border-4 border-primary-200 border-t-primary-500 rounded-full animate-spin" />
            </div>
        );
    }

    if (error || !recipe) {
        return (
            <div className="flex flex-col items-center justify-center py-32 text-center">
                <p className="text-gray-500 font-medium mb-4">{error || "Recipe not found."}</p>
                <button
                    onClick={() => navigate("/recipes")}
                    className="text-primary-500 text-sm font-medium hover:underline"
                >
                    ← Back to Recipes
                </button>
            </div>
        );
    }

    return (
        <div className="max-w-2xl mx-auto">

            {/* Back button */}
            <button
                onClick={() => navigate("/recipes")}
                className="flex items-center gap-1.5 text-sm text-gray-400 hover:text-gray-600 transition-colors mb-5"
            >
                <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                    <path strokeLinecap="round" strokeLinejoin="round" d="M15 19l-7-7 7-7" />
                </svg>
                Back to Recipes
            </button>

            {/* Header */}
            <div className="mb-6">
                <div className="flex items-start justify-between gap-4">
                    <h1 className="font-serif text-3xl font-bold text-gray-800">{recipe.name}</h1>
                    {recipe.missingIngredients.length === 0 ? (
                        <span className="px-3 py-1 bg-green-50 text-green-600 text-xs font-semibold rounded-full flex-shrink-0 mt-1">
                            Ready to cook
                        </span>
                    ) : (
                        <span className="px-3 py-1 bg-amber-50 text-amber-600 text-xs font-semibold rounded-full flex-shrink-0 mt-1">
                            {recipe.missingIngredients.length} missing
                        </span>
                    )}
                </div>

                {recipe.description && (
                    <p className="text-gray-500 mt-2 leading-relaxed">{recipe.description}</p>
                )}

                {/* Meta */}
                <div className="flex items-stretch gap-4 mt-4 flex-wrap">
                    {[
                        { label: "Meal", value: MEAL_LABELS[recipe.mealType] },
                        { label: "Diet", value: DIET_LABELS[recipe.dietType] },
                        { label: "Prep time", value: `${recipe.prepTime} min` },
                    ].map(({ label, value }) => (
                        <div key={label} className="bg-white rounded-xl px-4 py-2.5 shadow-sm">
                            <p className="text-xs text-gray-400">{label}</p>
                            <p className="text-sm font-semibold text-gray-700">{value}</p>
                        </div>
                    ))}

                    {/* Servings slider — inline, wider card */}
                    <div className="bg-white rounded-xl px-4 py-2.5 shadow-sm flex-1 min-w-[180px]">
                        <div className="flex items-center justify-between mb-1">
                            <p className="text-xs text-gray-400">Servings</p>
                            <p className="text-sm font-semibold text-gray-700">
                                {recipe.servings * multiplier}
                                {multiplier > 1 && <span className="ml-1 text-primary-400 font-normal text-xs">({multiplier}x)</span>}
                            </p>
                        </div>
                        <input
                            type="range"
                            min={1}
                            max={4}
                            step={1}
                            value={multiplier}
                            onChange={e => setMultiplier(Number(e.target.value))}
                            className="w-full accent-primary-500"
                        />
                        <div className="flex justify-between text-xs text-gray-300 mt-0.5">
                            <span>1x</span>
                            <span>2x</span>
                            <span>3x</span>
                            <span>4x</span>
                        </div>
                    </div>
                </div>
            </div>

            {/* Allergen warning */}
            {recipe.allergenWarnings?.length > 0 && (
                <div className="bg-red-50 border border-red-100 rounded-xl p-4 mb-5 flex items-start gap-3">
                    <svg className="w-5 h-5 text-red-400 flex-shrink-0 mt-0.5" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                        <path strokeLinecap="round" strokeLinejoin="round" d="M12 9v2m0 4h.01M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z" />
                    </svg>
                    <div>
                        <p className="text-sm font-semibold text-red-600">Allergen warning</p>
                        <p className="text-sm text-red-500 mt-0.5">Contains: {recipe.allergenWarnings.join(", ")}</p>
                    </div>
                </div>
            )}

            {/* Missing ingredients */}
            {recipe.missingIngredients.length > 0 && (
                <div className="bg-amber-50 border border-amber-100 rounded-xl p-4 mb-5">
                    <p className="text-sm font-semibold text-amber-700 mb-1">You're missing:</p>
                    <p className="text-sm text-amber-600">{recipe.missingIngredients.join(", ")}</p>
                </div>
            )}

            {/* Ingredients */}
            <div className="bg-white rounded-2xl shadow-sm overflow-hidden mb-5">
                <div className="px-5 py-4 border-b border-gray-50">
                    <h2 className="text-sm font-semibold text-gray-700">Ingredients</h2>
                </div>
                <div className="divide-y divide-gray-50">
                    {recipe.ingredients.map((ing, idx) => (
                        <div key={idx} className="flex items-center justify-between px-5 py-3">
                            <span className={`text-sm capitalize ${ing.isOptional ? "text-gray-400" : "text-gray-700"}`}>
                                {ing.ingredientName}
                                {ing.isOptional && <span className="text-xs ml-1.5 text-gray-300">(optional)</span>}
                            </span>
                            <span className="text-sm font-medium text-gray-500 ml-4 flex-shrink-0">
                                {formatQuantity(scaleQuantity(ing.quantity), ing.unit)}
                            </span>
                        </div>
                    ))}
                </div>
            </div>

            {/* Instructions */}
            <div className="bg-white rounded-2xl shadow-sm overflow-hidden mb-8">
                <div className="px-5 py-4 border-b border-gray-50">
                    <h2 className="text-sm font-semibold text-gray-700">Instructions</h2>
                </div>
                <div className="px-5 py-4">
                    <p className="text-sm text-gray-600 leading-relaxed whitespace-pre-line">
                        {recipe.instructions}
                    </p>
                </div>
            </div>

            {/* Done cooking button */}
            <button
                onClick={openModal}
                className="w-full h-13 py-3.5 bg-gradient-to-r from-primary-500 to-primary-600 text-white rounded-2xl font-semibold text-base shadow-lg hover:shadow-xl transition-all"
            >
                Done Cooking
            </button>

            {/* Done cooking modal */}
            {showModal && (
                <div className="fixed inset-0 bg-black/40 z-50 flex items-end sm:items-center justify-center p-4">
                    <div className="bg-white rounded-3xl w-full max-w-md shadow-2xl">

                        <div className="px-6 pt-6 pb-4 border-b border-gray-50">
                            <h2 className="text-lg font-semibold text-gray-800">What did you use up?</h2>
                            <p className="text-sm text-gray-400 mt-1">
                                Deselect anything you still have left. We'll remove the rest from your fridge.
                            </p>
                        </div>

                        <div className="px-6 py-3 max-h-72 overflow-y-auto divide-y divide-gray-50">
                            {cookableIngredients.map((ing, idx) => {
                                const selected = consumedIds.has(ing.ingredientName);
                                const isMissing = recipe.missingIngredients.includes(ing.ingredientName);
                                return (
                                    <div
                                        key={idx}
                                        onClick={() => !isMissing && toggleConsumed(ing.ingredientName)}
                                        className={`flex items-center justify-between py-3 transition-colors ${
                                            isMissing ? "opacity-40 cursor-not-allowed" : "cursor-pointer"
                                        }`}
                                    >
                                        <div>
                                            <p className="text-sm font-medium text-gray-700 capitalize">
                                                {ing.ingredientName}
                                            </p>
                                            {isMissing && (
                                                <p className="text-xs text-gray-400">Not in your fridge</p>
                                            )}
                                        </div>
                                        <div className={`w-5 h-5 rounded-full border-2 flex items-center justify-center flex-shrink-0 transition-all ${
                                            selected && !isMissing
                                                ? "bg-primary-500 border-primary-500"
                                                : "border-gray-300"
                                        }`}>
                                            {selected && !isMissing && (
                                                <svg className="w-3 h-3 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={3}>
                                                    <path strokeLinecap="round" strokeLinejoin="round" d="M5 13l4 4L19 7" />
                                                </svg>
                                            )}
                                        </div>
                                    </div>
                                );
                            })}
                        </div>

                        {saveError && (
                            <p className="mx-6 text-xs text-red-500 mt-2">{saveError}</p>
                        )}

                        <div className="px-6 py-4 flex gap-3">
                            <button
                                onClick={() => setShowModal(false)}
                                className="flex-1 h-11 bg-white border border-gray-200 text-gray-600 rounded-xl text-sm font-medium hover:bg-gray-50 transition-all"
                            >
                                Cancel
                            </button>
                            <button
                                onClick={confirmCooked}
                                disabled={saving || consumedIds.size === 0}
                                className="flex-1 h-11 bg-gradient-to-r from-primary-500 to-primary-600 text-white rounded-xl text-sm font-semibold shadow-md hover:shadow-lg transition-all disabled:opacity-60"
                            >
                                {saving ? "Saving..." : `Remove ${consumedIds.size} item${consumedIds.size !== 1 ? "s" : ""}`}
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}