import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext.tsx";

interface OnboardingData {
    firstName: string;
    lastName: string;
    isVegetarian: boolean;
    isVegan: boolean;
    defaultServings: number;
    allergyIds: Set<number>;
}

interface Allergy {
    id: number;
    name: string;
}

const ALL_ALLERGIES: Allergy[] = [
    { id: 1, name: "gluten" },
    { id: 2, name: "lactose" },
    { id: 3, name: "eggs" },
    { id: 4, name: "nuts" },
    { id: 5, name: "peanuts" },
    { id: 6, name: "fish" },
    { id: 7, name: "shellfish" },
    { id: 8, name: "soy" },
];

const STEPS = ["Name", "Diet", "Allergies", "Household"];
const TOTAL_STEPS = STEPS.length;

const SERVING_OPTIONS = [1, 2, 3, 4, 5, 6];

export default function OnboardingPage() {
    const navigate = useNavigate();
    const { completeOnboarding } = useAuth();

    const [step, setStep] = useState(0);
    const [data, setData] = useState<OnboardingData>({
        firstName: "",
        lastName: "",
        isVegetarian: false,
        isVegan: false,
        defaultServings: 2,
        allergyIds: new Set(),
    });
    const [submitting, setSubmitting] = useState(false);

    const goNext = () => setStep(s => Math.min(s + 1, TOTAL_STEPS - 1));
    const goBack = () => setStep(s => Math.max(s - 1, 0));

    const toggleAllergy = (id: number) => {
        setData(prev => {
            const next = new Set(prev.allergyIds);
            next.has(id) ? next.delete(id) : next.add(id);
            return { ...prev, allergyIds: next };
        });
    };

    const toggleDiet = (key: "isVegetarian" | "isVegan") => {
        setData(prev => {
            const updated = { ...prev, [key]: !prev[key] };
            if (key === "isVegan" && updated.isVegan) updated.isVegetarian = true;
            if (key === "isVegetarian" && !updated.isVegetarian) updated.isVegan = false;
            return updated;
        });
    };

    const handleFinish = async () => {
        setSubmitting(true);
        // TODO: replace with POST /api/auth/onboarding once backend is ready
        await new Promise(r => setTimeout(r, 600)); // simulate async
        completeOnboarding();
        navigate("/fridge");
    };

    const canProceed = () => {
        if (step === 0) return data.firstName.trim().length > 0;
        return true;
    };

    return (
        <div className="min-h-screen bg-cream-50 flex items-center justify-center p-4">
            <div className="w-full max-w-md">

                {/* Header */}
                <div className="text-center mb-8">
                    <span className="text-4xl">🍽️</span>
                    <h1 className="font-serif text-2xl font-bold text-gray-800 mt-2">
                        Let's set up your profile
                    </h1>
                    <p className="text-sm text-gray-400 mt-1">
                        Step {step + 1} of {TOTAL_STEPS} — {STEPS[step]}
                    </p>
                </div>

                {/* Progress bar */}
                <div className="flex gap-1.5 mb-8">
                    {STEPS.map((_, i) => (
                        <div
                            key={i}
                            className={`h-1.5 flex-1 rounded-full transition-all duration-500 ${
                                i <= step ? "bg-primary-500" : "bg-gray-200"
                            }`}
                        />
                    ))}
                </div>

                {/* Card */}
                <div className="bg-white rounded-3xl shadow-sm p-6 mb-4">

                    {/* Step 0 — Name */}
                    {step === 0 && (
                        <div>
                            <h2 className="text-lg font-semibold text-gray-800 mb-1">What's your name?</h2>
                            <p className="text-sm text-gray-400 mb-5">So we can greet you properly.</p>

                            <div className="space-y-4">
                                <div>
                                    <label className="text-xs font-semibold text-gray-500 uppercase tracking-wide block mb-1.5">
                                        First Name
                                    </label>
                                    <input
                                        type="text"
                                        value={data.firstName}
                                        onChange={e => setData(d => ({ ...d, firstName: e.target.value }))}
                                        autoFocus
                                        placeholder="e.g. Maria"
                                        className="w-full h-11 bg-cream-50 border border-cream-200 rounded-xl px-4 text-sm focus:outline-none focus:border-primary-400 focus:ring-2 focus:ring-primary-100 transition-all"
                                    />
                                </div>
                                <div>
                                    <label className="text-xs font-semibold text-gray-500 uppercase tracking-wide block mb-1.5">
                                        Last Name <span className="text-gray-300 normal-case font-normal">(optional)</span>
                                    </label>
                                    <input
                                        type="text"
                                        value={data.lastName}
                                        onChange={e => setData(d => ({ ...d, lastName: e.target.value }))}
                                        placeholder="e.g. Popescu"
                                        className="w-full h-11 bg-cream-50 border border-cream-200 rounded-xl px-4 text-sm focus:outline-none focus:border-primary-400 focus:ring-2 focus:ring-primary-100 transition-all"
                                    />
                                </div>
                            </div>
                        </div>
                    )}

                    {/* Step 1 — Diet */}
                    {step === 1 && (
                        <div>
                            <h2 className="text-lg font-semibold text-gray-800 mb-1">
                                Any dietary preferences?
                            </h2>
                            <p className="text-sm text-gray-400 mb-5">
                                We'll use this to filter recipe suggestions for you.
                            </p>

                            <div className="space-y-3">
                                {[
                                    { key: "isVegetarian" as const, label: "Vegetarian", description: "No meat or fish", emoji: "🥦" },
                                    { key: "isVegan" as const, label: "Vegan", description: "No animal products at all", emoji: "🌱" },
                                ].map(({ key, label, description, emoji }) => (
                                    <button
                                        key={key}
                                        onClick={() => toggleDiet(key)}
                                        className={`w-full flex items-center justify-between p-4 rounded-2xl border-2 transition-all text-left ${
                                            data[key]
                                                ? "border-primary-400 bg-primary-50"
                                                : "border-gray-100 bg-gray-50 hover:border-gray-200"
                                        }`}
                                    >
                                        <div className="flex items-center gap-3">
                                            <span className="text-2xl">{emoji}</span>
                                            <div>
                                                <p className={`text-sm font-semibold ${data[key] ? "text-primary-700" : "text-gray-700"}`}>
                                                    {label}
                                                </p>
                                                <p className="text-xs text-gray-400">{description}</p>
                                            </div>
                                        </div>
                                        <div className={`w-5 h-5 rounded-full border-2 flex items-center justify-center flex-shrink-0 transition-all ${
                                            data[key] ? "bg-primary-500 border-primary-500" : "border-gray-300"
                                        }`}>
                                            {data[key] && (
                                                <svg className="w-3 h-3 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={3}>
                                                    <path strokeLinecap="round" strokeLinejoin="round" d="M5 13l4 4L19 7" />
                                                </svg>
                                            )}
                                        </div>
                                    </button>
                                ))}

                                {!data.isVegetarian && !data.isVegan && (
                                    <p className="text-xs text-gray-400 text-center pt-1">
                                        No preference selected — we'll show all recipes.
                                    </p>
                                )}
                            </div>
                        </div>
                    )}

                    {/* Step 2 — Allergies */}
                    {step === 2 && (
                        <div>
                            <h2 className="text-lg font-semibold text-gray-800 mb-1">Any food allergies?</h2>
                            <p className="text-sm text-gray-400 mb-5">
                                We'll warn you when a recipe contains these. Select all that apply.
                            </p>

                            <div className="grid grid-cols-2 gap-2">
                                {ALL_ALLERGIES.map(allergy => {
                                    const active = data.allergyIds.has(allergy.id);
                                    return (
                                        <button
                                            key={allergy.id}
                                            onClick={() => toggleAllergy(allergy.id)}
                                            className={`flex items-center justify-between px-4 py-3 rounded-xl border-2 transition-all text-left ${
                                                active
                                                    ? "border-red-300 bg-red-50"
                                                    : "border-gray-100 bg-gray-50 hover:border-gray-200"
                                            }`}
                                        >
                                            <span className={`text-sm font-medium capitalize ${active ? "text-red-700" : "text-gray-600"}`}>
                                                {allergy.name}
                                            </span>
                                            {active && (
                                                <svg className="w-4 h-4 text-red-500 flex-shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2.5}>
                                                    <path strokeLinecap="round" strokeLinejoin="round" d="M5 13l4 4L19 7" />
                                                </svg>
                                            )}
                                        </button>
                                    );
                                })}
                            </div>

                            {data.allergyIds.size === 0 && (
                                <p className="text-xs text-gray-400 text-center mt-4">
                                    No allergies selected — you can always update this in your profile.
                                </p>
                            )}
                        </div>
                    )}

                    {/* Step 3 — Household */}
                    {step === 3 && (
                        <div>
                            <h2 className="text-lg font-semibold text-gray-800 mb-1">
                                How many people are you cooking for?
                            </h2>
                            <p className="text-sm text-gray-400 mb-5">
                                We'll use this as the default serving size for recipes.
                            </p>

                            <div className="grid grid-cols-3 gap-3 mb-6">
                                {SERVING_OPTIONS.map(n => (
                                    <button
                                        key={n}
                                        onClick={() => setData(d => ({ ...d, defaultServings: n }))}
                                        className={`flex flex-col items-center justify-center py-4 rounded-2xl border-2 transition-all ${
                                            data.defaultServings === n
                                                ? "border-primary-400 bg-primary-50"
                                                : "border-gray-100 bg-gray-50 hover:border-gray-200"
                                        }`}
                                    >
                                        <span className="text-2xl mb-1">
                                            {n === 1 ? "🧑" : n <= 2 ? "👫" : n <= 4 ? "👨‍👩‍👧‍👦" : "🏘️"}
                                        </span>
                                        <span className={`text-lg font-bold ${data.defaultServings === n ? "text-primary-600" : "text-gray-700"}`}>
                                            {n}
                                        </span>
                                        <span className="text-xs text-gray-400">
                                            {n === 1 ? "person" : "people"}
                                        </span>
                                    </button>
                                ))}
                            </div>

                            {/* Summary */}
                            <div className="bg-cream-50 rounded-2xl p-4 space-y-2">
                                <p className="text-xs font-semibold text-gray-400 uppercase tracking-wide mb-3">Your profile summary</p>
                                <div className="flex items-center justify-between text-sm">
                                    <span className="text-gray-500">Name</span>
                                    <span className="font-medium text-gray-800">
                                        {data.firstName}{data.lastName ? ` ${data.lastName}` : ""}
                                    </span>
                                </div>
                                <div className="flex items-center justify-between text-sm">
                                    <span className="text-gray-500">Diet</span>
                                    <span className="font-medium text-gray-800">
                                        {data.isVegan ? "Vegan" : data.isVegetarian ? "Vegetarian" : "No preference"}
                                    </span>
                                </div>
                                <div className="flex items-center justify-between text-sm">
                                    <span className="text-gray-500">Allergies</span>
                                    <span className="font-medium text-gray-800">
                                        {data.allergyIds.size === 0
                                            ? "None"
                                            : ALL_ALLERGIES
                                                .filter(a => data.allergyIds.has(a.id))
                                                .map(a => a.name)
                                                .join(", ")}
                                    </span>
                                </div>
                                <div className="flex items-center justify-between text-sm">
                                    <span className="text-gray-500">Cooking for</span>
                                    <span className="font-medium text-gray-800">{data.defaultServings} {data.defaultServings === 1 ? "person" : "people"}</span>
                                </div>
                            </div>
                        </div>
                    )}
                </div>

                {/* Navigation */}
                <div className="flex gap-3">
                    {step > 0 && (
                        <button
                            onClick={goBack}
                            className="h-12 px-6 bg-white border border-gray-200 text-gray-600 rounded-xl text-sm font-medium hover:bg-gray-50 transition-all"
                        >
                            ← Back
                        </button>
                    )}

                    {step < TOTAL_STEPS - 1 ? (
                        <button
                            onClick={goNext}
                            disabled={!canProceed()}
                            className="flex-1 h-12 bg-gradient-to-r from-primary-500 to-primary-600 text-white rounded-xl font-semibold text-sm shadow-md hover:shadow-lg transition-all disabled:opacity-50"
                        >
                            Continue →
                        </button>
                    ) : (
                        <button
                            onClick={handleFinish}
                            disabled={submitting}
                            className="flex-1 h-12 bg-gradient-to-r from-primary-500 to-primary-600 text-white rounded-xl font-semibold text-sm shadow-md hover:shadow-lg transition-all disabled:opacity-60"
                        >
                            {submitting ? "Saving..." : "Let's cook! 🍳"}
                        </button>
                    )}
                </div>

                {/* Skip */}
                {step < TOTAL_STEPS - 1 && (
                    <button
                        onClick={() => navigate("/fridge")}
                        className="w-full text-center text-xs text-gray-400 hover:text-gray-500 mt-4 transition-colors"
                    >
                        Skip for now — I'll set this up later
                    </button>
                )}
            </div>
        </div>
    );
}