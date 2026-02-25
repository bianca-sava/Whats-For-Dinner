import { useState, useEffect } from "react";
import { useAuth } from "../context/AuthContext.tsx";
import { useNavigate } from "react-router-dom";
import apiClient from "../api/client";

interface Preferences {
    isVegetarian: boolean;
    isVegan: boolean;
}

interface Allergy {
    id: number;
    name: string;
}

export default function ProfilePage() {
    const { logout } = useAuth();
    const navigate = useNavigate();

    const [preferences, setPreferences] = useState<Preferences>({ isVegetarian: false, isVegan: false });
    const [userAllergies, setUserAllergies] = useState<Allergy[]>([]);
    const [allAllergies, setAllAllergies] = useState<Allergy[]>([]);
    const [loading, setLoading] = useState(true);
    const [savingPrefs, setSavingPrefs] = useState(false);
    const [savedPrefs, setSavedPrefs] = useState(false);
    const [togglingAllergyId, setTogglingAllergyId] = useState<number | null>(null);

    useEffect(() => {
        const init = async () => {
            try {
                const [prefsRes, userAllergyRes, allAllergyRes] = await Promise.all([
                    apiClient.get("/api/profile/preferences"),
                    apiClient.get("/api/profile/allergies"),
                    apiClient.get("/api/allergies"),
                ]);
                setPreferences(prefsRes.data);
                setUserAllergies(userAllergyRes.data);
                setAllAllergies(allAllergyRes.data);
            } catch {
                // silent
            } finally {
                setLoading(false);
            }
        };

        init();
    }, []);

    const savePreferences = async (updated: Preferences) => {
        setSavingPrefs(true);
        setSavedPrefs(false);
        try {
            await apiClient.put("/api/profile/preferences", updated);
            setSavedPrefs(true);
            setTimeout(() => setSavedPrefs(false), 2000);
        } catch {
            // silent
        } finally {
            setSavingPrefs(false);
        }
    };

    const togglePreference = (key: keyof Preferences) => {
        const updated = { ...preferences, [key]: !preferences[key] };
        if (key === "isVegan" && updated.isVegan) updated.isVegetarian = true;
        if (key === "isVegetarian" && !updated.isVegetarian) updated.isVegan = false;
        setPreferences(updated);
        savePreferences(updated);
    };

    const hasAllergy = (id: number) => userAllergies.some(a => a.id === id);

    const toggleAllergy = async (allergy: Allergy) => {
        setTogglingAllergyId(allergy.id);
        try {
            if (hasAllergy(allergy.id)) {
                await apiClient.delete(`/api/profile/allergies/${allergy.id}`);
                setUserAllergies(prev => prev.filter(a => a.id !== allergy.id));
            } else {
                await apiClient.post("/api/profile/allergies", { allergyId: allergy.id });
                setUserAllergies(prev => [...prev, allergy]);
            }
        } catch {
            // silent
        } finally {
            setTogglingAllergyId(null);
        }
    };

    const handleLogout = () => {
        logout();
        navigate("/login");
    };

    if (loading) {
        return (
            <div className="flex items-center justify-center py-20">
                <div className="w-8 h-8 border-4 border-primary-200 border-t-primary-500 rounded-full animate-spin" />
            </div>
        );
    }

    return (
        <div className="max-w-lg">
            <div className="mb-6">
                <h1 className="text-2xl font-serif font-bold text-gray-800">Profile</h1>
                <p className="text-sm text-gray-400 mt-0.5">Manage your dietary preferences</p>
            </div>

            {/* Dietary preferences */}
            <div className="bg-white rounded-2xl shadow-sm overflow-hidden mb-4">
                <div className="px-5 py-4 border-b border-gray-50">
                    <h2 className="text-sm font-semibold text-gray-700">Dietary Preferences</h2>
                    <p className="text-xs text-gray-400 mt-0.5">Used to filter recipe suggestions</p>
                </div>

                <div className="divide-y divide-gray-50">
                    {[
                        { key: "isVegetarian" as const, label: "Vegetarian", description: "No meat or fish" },
                        { key: "isVegan" as const, label: "Vegan", description: "No animal products" },
                    ].map(({ key, label, description }) => (
                        <div key={key} className="flex items-center justify-between px-5 py-4">
                            <div>
                                <p className="text-sm font-medium text-gray-800">{label}</p>
                                <p className="text-xs text-gray-400">{description}</p>
                            </div>
                            <button
                                onClick={() => togglePreference(key)}
                                disabled={savingPrefs}
                                className={`relative w-11 h-6 rounded-full transition-colors duration-200 focus:outline-none disabled:opacity-60 ${
                                    preferences[key] ? "bg-primary-500" : "bg-gray-200"
                                }`}
                            >
                                <span className={`absolute top-0.5 left-0.5 w-5 h-5 bg-white rounded-full shadow transition-transform duration-200 ${
                                    preferences[key] ? "translate-x-5" : "translate-x-0"
                                }`} />
                            </button>
                        </div>
                    ))}
                </div>

                {savedPrefs && (
                    <div className="px-5 py-2.5 bg-green-50 border-t border-green-100">
                        <p className="text-xs text-green-600 font-medium">Preferences saved</p>
                    </div>
                )}
            </div>

            {/* Allergies */}
            <div className="bg-white rounded-2xl shadow-sm overflow-hidden mb-4">
                <div className="px-5 py-4 border-b border-gray-50">
                    <h2 className="text-sm font-semibold text-gray-700">Allergies</h2>
                    <p className="text-xs text-gray-400 mt-0.5">Toggle the ones that apply to you</p>
                </div>

                <div className="divide-y divide-gray-50">
                    {allAllergies.map(allergy => {
                        const active = hasAllergy(allergy.id);
                        const toggling = togglingAllergyId === allergy.id;
                        return (
                            <div key={allergy.id} className="flex items-center justify-between px-5 py-3.5">
                                <p className="text-sm font-medium text-gray-800 capitalize">{allergy.name}</p>
                                <button
                                    onClick={() => toggleAllergy(allergy)}
                                    disabled={toggling}
                                    className={`relative w-11 h-6 rounded-full transition-colors duration-200 focus:outline-none disabled:opacity-60 ${
                                        active ? "bg-primary-500" : "bg-gray-200"
                                    }`}
                                >
                                    <span className={`absolute top-0.5 left-0.5 w-5 h-5 bg-white rounded-full shadow transition-transform duration-200 ${
                                        active ? "translate-x-5" : "translate-x-0"
                                    }`} />
                                </button>
                            </div>
                        );
                    })}
                </div>
            </div>

            {/* Logout */}
            <div className="bg-white rounded-2xl shadow-sm overflow-hidden">
                <button
                    onClick={handleLogout}
                    className="w-full px-5 py-4 flex items-center justify-between text-left hover:bg-red-50 transition-colors"
                >
                    <div>
                        <p className="text-sm font-medium text-red-500">Sign Out</p>
                        <p className="text-xs text-gray-400">You'll need to log in again</p>
                    </div>
                    <svg className="w-4 h-4 text-red-400" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                        <path strokeLinecap="round" strokeLinejoin="round" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                    </svg>
                </button>
            </div>
        </div>
    );
}