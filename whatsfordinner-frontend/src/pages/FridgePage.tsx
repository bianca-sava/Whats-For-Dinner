import { useState, useEffect, useRef } from "react";
import apiClient from "../api/client";
import type { FridgeItem, Ingredient, ScannedIngredient } from "../types";

type View = "fridge" | "scanning" | "confirm";

const CATEGORY_LABELS: Record<string, string> = {
    DAIRY: "Dairy",
    MEAT: "Meat",
    VEGETABLE: "Vegetables",
    FRUIT: "Fruit",
    GRAIN: "Grains",
    SPICE: "Spices",
    OTHER: "Other",
};

export default function FridgePage() {
    const [fridgeItems, setFridgeItems] = useState<FridgeItem[]>([]);
    const [allIngredients, setAllIngredients] = useState<Ingredient[]>([]);
    const [loading, setLoading] = useState(true);
    const [loadError, setLoadError] = useState("");
    const [view, setView] = useState<View>("fridge");
    const [scannedItems, setScannedItems] = useState<ScannedIngredient[]>([]);
    const [selectedIds, setSelectedIds] = useState<Set<number>>(new Set());
    const [addingBulk, setAddingBulk] = useState(false);
    const [scanError, setScanError] = useState("");
    const [removeError, setRemoveError] = useState("");
    const [drawerOpen, setDrawerOpen] = useState(false);
    const [search, setSearch] = useState("");
    const [addingId, setAddingId] = useState<number | null>(null);
    const [addError, setAddError] = useState("");
    const fileInputRef = useRef<HTMLInputElement>(null);
    const searchRef = useRef<HTMLInputElement>(null);

    const fetchFridge = async () => {
        try {
            const res = await apiClient.get("/api/fridge");
            setFridgeItems(res.data);
            setLoadError("");
        } catch {
            setLoadError("Failed to load your fridge. Please refresh the page.");
        } finally {
            setLoading(false);
        }
    };

    const fetchIngredients = async () => {
        try {
            const res = await apiClient.get("/api/ingredients");
            setAllIngredients(res.data);
        } catch {
        }
    };

    useEffect(() => {
        fetchFridge();
        fetchIngredients();
    }, []);

    useEffect(() => {
        if (drawerOpen) {
            setTimeout(() => searchRef.current?.focus(), 100);
        } else {
            setSearch("");
            setAddError("");
        }
    }, [drawerOpen]);

    const fridgeIngredientIds = new Set(fridgeItems.map(i => i.ingredientId));

    const filteredIngredients = allIngredients.filter(ing =>
        ing.name.toLowerCase().includes(search.toLowerCase()) &&
        !fridgeIngredientIds.has(ing.id)
    );

    const addIngredient = async (ingredientId: number) => {
        setAddingId(ingredientId);
        setAddError("");
        try {
            await apiClient.post("/api/fridge", { ingredientId });
            await fetchFridge();
        } catch {
            setAddError("Failed to add ingredient. Please try again.");
        } finally {
            setAddingId(null);
        }
    };

    const removeItem = async (id: number) => {
        setRemoveError("");
        try {
            await apiClient.delete(`/api/fridge/${id}`);
            setFridgeItems(prev => prev.filter(i => i.id !== id));
        } catch {
            setRemoveError("Failed to remove item. Please try again.");
        }
    };

    const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (!file) return;

        setScanError("");
        setView("scanning");

        try {
            const base64 = await fileToBase64(file);
            const res = await apiClient.post(
                "/api/fridge/scan",
                { base64Image: base64 }
            );
            setScannedItems(res.data);
            setSelectedIds(new Set(res.data.map((i: ScannedIngredient) => i.ingredientId)));
            setView("confirm");
        } catch {
            setScanError("Failed to scan receipt. Please try again.");
            setView("fridge");
        } finally {
            if (fileInputRef.current) fileInputRef.current.value = "";
        }
    };

    const toggleSelection = (id: number) => {
        setSelectedIds(prev => {
            const next = new Set(prev);
            next.has(id) ? next.delete(id) : next.add(id);
            return next;
        });
    };

    const confirmScanned = async () => {
        if (selectedIds.size === 0) {
            setView("fridge");
            return;
        }
        setAddingBulk(true);
        setScanError("");
        try {
            await apiClient.post("/api/fridge/bulk", Array.from(selectedIds));
            await fetchFridge();
            setView("fridge");
            setScannedItems([]);
            setSelectedIds(new Set());
        } catch {
            setScanError("Failed to add items. Please try again.");
        } finally {
            setAddingBulk(false);
        }
    };

    const fileToBase64 = (file: File): Promise<string> =>
        new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.onload = () => resolve((reader.result as string).split(",")[1]);
            reader.onerror = reject;
            reader.readAsDataURL(file);
        });

    const grouped = fridgeItems.reduce<Record<string, FridgeItem[]>>((acc, item) => {
        const cat = item.category ?? "OTHER";
        if (!acc[cat]) acc[cat] = [];
        acc[cat].push(item);
        return acc;
    }, {});

    if (view === "scanning") {
        return (
            <div className="flex flex-col items-center justify-center min-h-[60vh] gap-4">
                <div className="w-12 h-12 border-4 border-primary-200 border-t-primary-500 rounded-full animate-spin" />
                <p className="text-gray-500 font-medium">Reading your receipt...</p>
                <p className="text-sm text-gray-400">This may take a few seconds</p>
            </div>
        );
    }

    if (view === "confirm") {
        return (
            <div className="max-w-lg mx-auto">
                <div className="mb-6">
                    <h2 className="text-xl font-semibold text-gray-800">Review Scanned Items</h2>
                    <p className="text-sm text-gray-500 mt-1">
                        Found {scannedItems.length} item{scannedItems.length !== 1 ? "s" : ""} on your receipt.
                        Deselect anything you don't want to add.
                    </p>
                </div>

                {scanError && (
                    <div className="bg-red-50 border border-red-200 text-red-600 text-sm rounded-xl p-3 mb-4 flex items-center gap-2">
                        <svg className="w-4 h-4 flex-shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                            <path strokeLinecap="round" strokeLinejoin="round" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                        </svg>
                        {scanError}
                    </div>
                )}

                <div className="bg-white rounded-2xl shadow-sm overflow-hidden mb-4">
                    {scannedItems.map((item, idx) => {
                        const selected = selectedIds.has(item.ingredientId);
                        return (
                            <div
                                key={item.ingredientId}
                                onClick={() => toggleSelection(item.ingredientId)}
                                className={`flex items-center justify-between px-5 py-3.5 cursor-pointer transition-colors select-none
                                    ${idx !== 0 ? "border-t border-gray-50" : ""}
                                    ${selected ? "bg-white" : "bg-gray-50"}`}
                            >
                                <div>
                                    <p className={`text-sm font-medium ${selected ? "text-gray-800" : "text-gray-400 line-through"}`}>
                                        {item.mappedName}
                                    </p>
                                    <p className="text-xs text-gray-400 mt-0.5">on receipt: "{item.receiptName}"</p>
                                </div>
                                <div className={`w-5 h-5 rounded-full border-2 flex items-center justify-center transition-all flex-shrink-0
                                    ${selected ? "bg-primary-500 border-primary-500" : "border-gray-300"}`}>
                                    {selected && (
                                        <svg className="w-3 h-3 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={3}>
                                            <path strokeLinecap="round" strokeLinejoin="round" d="M5 13l4 4L19 7" />
                                        </svg>
                                    )}
                                </div>
                            </div>
                        );
                    })}
                </div>

                <div className="flex gap-3">
                    <button
                        onClick={() => { setView("fridge"); setScannedItems([]); setScanError(""); }}
                        className="flex-1 h-11 bg-white border border-gray-200 text-gray-600 rounded-xl text-sm font-medium hover:bg-gray-50 transition-all"
                    >
                        Cancel
                    </button>
                    <button
                        onClick={confirmScanned}
                        disabled={addingBulk || selectedIds.size === 0}
                        className="flex-1 h-11 bg-gradient-to-r from-primary-500 to-primary-600 text-white rounded-xl text-sm font-semibold shadow-md hover:shadow-lg transition-all disabled:opacity-60"
                    >
                        {addingBulk ? "Adding..." : `Add ${selectedIds.size} item${selectedIds.size !== 1 ? "s" : ""}`}
                    </button>
                </div>
            </div>
        );
    }

    return (
        <div className="relative">

            <div className="flex items-center justify-between mb-6">
                <div>
                    <h1 className="text-2xl font-serif font-bold text-gray-800">My Fridge</h1>
                    <p className="text-sm text-gray-400 mt-0.5">
                        {fridgeItems.length} ingredient{fridgeItems.length !== 1 ? "s" : ""} tracked
                    </p>
                </div>
                <div className="flex items-center gap-2">
                    {scanError && <span className="text-xs text-red-500">{scanError}</span>}
                    <input ref={fileInputRef} type="file" accept="image/*" className="hidden" onChange={handleFileChange} />

                    <button
                        onClick={() => fileInputRef.current?.click()}
                        className="h-10 px-4 bg-white border border-gray-200 text-gray-700 rounded-xl text-sm font-medium hover:bg-gray-50 transition-all flex items-center gap-2 shadow-sm"
                    >
                        <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.8}>
                            <path strokeLinecap="round" strokeLinejoin="round" d="M3 9a2 2 0 012-2h.93a2 2 0 001.664-.89l.812-1.22A2 2 0 0110.07 4h3.86a2 2 0 011.664.89l.812 1.22A2 2 0 0018.07 7H19a2 2 0 012 2v9a2 2 0 01-2 2H5a2 2 0 01-2-2V9z" />
                            <path strokeLinecap="round" strokeLinejoin="round" d="M15 13a3 3 0 11-6 0 3 3 0 016 0z" />
                        </svg>
                        Scan Receipt
                    </button>

                    <button
                        onClick={() => setDrawerOpen(true)}
                        className="h-10 px-4 bg-gradient-to-r from-primary-500 to-primary-600 text-white rounded-xl text-sm font-semibold shadow-md hover:shadow-lg transition-all flex items-center gap-2"
                    >
                        <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2.5}>
                            <path strokeLinecap="round" strokeLinejoin="round" d="M12 4v16m8-8H4" />
                        </svg>
                        Add
                    </button>
                </div>
            </div>

            {/* Load error */}
            {loadError && (
                <div className="bg-red-50 border border-red-200 text-red-600 text-sm rounded-xl p-4 mb-4 flex items-center justify-between">
                    <div className="flex items-center gap-2">
                        <svg className="w-4 h-4 flex-shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                            <path strokeLinecap="round" strokeLinejoin="round" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                        </svg>
                        {loadError}
                    </div>
                    <button
                        onClick={() => { setLoading(true); fetchFridge(); }}
                        className="text-red-500 font-medium text-xs underline ml-4 flex-shrink-0"
                    >
                        Retry
                    </button>
                </div>
            )}

            {/* Remove error */}
            {removeError && (
                <div className="bg-red-50 border border-red-200 text-red-600 text-sm rounded-xl p-3 mb-4">
                    {removeError}
                </div>
            )}

            {/* Loading */}
            {loading && (
                <div className="flex items-center justify-center py-20">
                    <div className="w-8 h-8 border-4 border-primary-200 border-t-primary-500 rounded-full animate-spin" />
                </div>
            )}

            {/* Empty state */}
            {!loading && !loadError && fridgeItems.length === 0 && (
                <div className="flex flex-col items-center justify-center py-20 text-center">
                    <div className="w-16 h-16 bg-gray-100 rounded-2xl flex items-center justify-center mb-4">
                        <svg className="w-8 h-8 text-gray-300" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
                            <path strokeLinecap="round" strokeLinejoin="round" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10" />
                        </svg>
                    </div>
                    <p className="text-gray-500 font-medium">Your fridge is empty</p>
                    <p className="text-sm text-gray-400 mt-1">Scan a receipt or add ingredients manually</p>
                </div>
            )}

            {/* Grouped items */}
            {!loading && Object.keys(grouped).length > 0 && (
                <div className="space-y-6">
                    {Object.entries(grouped).map(([category, items]) => (
                        <div key={category}>
                            <h3 className="text-xs font-semibold text-gray-400 uppercase tracking-widest mb-2 px-1">
                                {CATEGORY_LABELS[category] ?? category}
                            </h3>
                            <div className="bg-white rounded-2xl shadow-sm overflow-hidden">
                                {items.map((item, idx) => (
                                    <div
                                        key={item.id}
                                        className={`flex items-center justify-between px-5 py-3.5 group ${idx !== 0 ? "border-t border-gray-50" : ""}`}
                                    >
                                        <div>
                                            <p className="text-sm font-medium text-gray-800 capitalize">{item.ingredientName}</p>
                                            {item.isPantryItem && (
                                                <p className="text-xs text-gray-400">Pantry staple</p>
                                            )}
                                        </div>
                                        <button
                                            onClick={() => removeItem(item.id)}
                                            className="opacity-0 group-hover:opacity-100 w-7 h-7 flex items-center justify-center rounded-lg text-gray-300 hover:text-red-400 hover:bg-red-50 transition-all"
                                        >
                                            <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                                                <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
                                            </svg>
                                        </button>
                                    </div>
                                ))}
                            </div>
                        </div>
                    ))}
                </div>
            )}

            {/* Backdrop */}
            {drawerOpen && (
                <div className="fixed inset-0 bg-black/20 z-40" onClick={() => setDrawerOpen(false)} />
            )}

            {/* Add ingredient drawer */}
            <div className={`fixed top-0 right-0 h-full w-full max-w-sm bg-white shadow-2xl z-50 flex flex-col transition-transform duration-300 ease-in-out ${drawerOpen ? "translate-x-0" : "translate-x-full"}`}>

                <div className="flex items-center justify-between px-5 py-4 border-b border-gray-100">
                    <h2 className="text-base font-semibold text-gray-800">Add Ingredient</h2>
                    <button
                        onClick={() => setDrawerOpen(false)}
                        className="w-8 h-8 flex items-center justify-center rounded-lg text-gray-400 hover:text-gray-600 hover:bg-gray-100 transition-all"
                    >
                        <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                            <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
                        </svg>
                    </button>
                </div>

                <div className="px-5 py-3 border-b border-gray-100">
                    <input
                        ref={searchRef}
                        type="text"
                        value={search}
                        onChange={e => setSearch(e.target.value)}
                        placeholder="Search ingredients..."
                        className="w-full h-10 bg-gray-50 border border-gray-200 rounded-xl px-4 text-sm focus:outline-none focus:border-primary-400 focus:ring-2 focus:ring-primary-100 transition-all"
                    />
                </div>

                {addError && (
                    <div className="mx-5 mt-3 bg-red-50 border border-red-200 text-red-600 text-xs rounded-xl p-3">
                        {addError}
                    </div>
                )}

                <div className="flex-1 overflow-y-auto">
                    {filteredIngredients.length === 0 ? (
                        <div className="flex items-center justify-center h-full px-6">
                            <p className="text-gray-400 text-sm text-center">
                                {search ? `No results for "${search}"` : "All ingredients are already in your fridge"}
                            </p>
                        </div>
                    ) : (
                        filteredIngredients.map((ing, idx) => (
                            <div
                                key={ing.id}
                                className={`flex items-center justify-between px-5 py-3.5 ${idx !== 0 ? "border-t border-gray-50" : ""}`}
                            >
                                <div>
                                    <p className="text-sm font-medium text-gray-800 capitalize">{ing.name}</p>
                                    <p className="text-xs text-gray-400">{CATEGORY_LABELS[ing.category] ?? ing.category}</p>
                                </div>
                                <button
                                    onClick={() => addIngredient(ing.id)}
                                    disabled={addingId === ing.id}
                                    className="w-8 h-8 flex items-center justify-center rounded-lg bg-primary-50 text-primary-500 hover:bg-primary-100 transition-all disabled:opacity-50"
                                >
                                    {addingId === ing.id ? (
                                        <div className="w-3.5 h-3.5 border-2 border-primary-300 border-t-primary-500 rounded-full animate-spin" />
                                    ) : (
                                        <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2.5}>
                                            <path strokeLinecap="round" strokeLinejoin="round" d="M12 4v16m8-8H4" />
                                        </svg>
                                    )}
                                </button>
                            </div>
                        ))
                    )}
                </div>
            </div>
        </div>
    );
}