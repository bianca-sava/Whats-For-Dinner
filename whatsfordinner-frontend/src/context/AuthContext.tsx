import { createContext, useContext, useState, useEffect, useCallback } from "react";
import type { ReactNode } from "react";
import apiClient from "../api/client";

interface AuthContextType {
    token: string | null;
    userName: string | null;
    defaultServings: number;
    login: (token: string, completedOnboarding: boolean) => void;
    logout: () => void;
    completeOnboarding: () => Promise<void>;
    refreshUserName: () => void;
    isAuthenticated: boolean;
    hasCompletedOnboarding: boolean;
}

const AuthContext = createContext<AuthContextType | null>(null);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const [token, setToken] = useState<string | null>(
        localStorage.getItem("token")
    );
    const [hasCompletedOnboarding, setHasCompletedOnboarding] = useState<boolean>(
        localStorage.getItem("onboardingComplete") === "true"
    );
    const [userName, setUserName] = useState<string | null>(
        localStorage.getItem("userName")
    );
    const [defaultServings, setDefaultServings] = useState<number>(
        parseInt(localStorage.getItem("defaultServings") ?? "2") || 2
    );

    const fetchAndStoreProfile = useCallback(() => {
        apiClient
            .get("/api/profile/me")
            .then(res => {
                const name = res.data.firstName ?? null;
                const servings = res.data.defaultServings ?? 2;
                setUserName(name);
                setDefaultServings(servings);
                if (name) localStorage.setItem("userName", name);
                localStorage.setItem("defaultServings", String(servings));
            })
            .catch(() => {});
    }, []);

    useEffect(() => {
        if (!token) return;
        fetchAndStoreProfile();
    }, [token, fetchAndStoreProfile]);

    const login = (newToken: string, completedOnboarding: boolean) => {
        localStorage.setItem("token", newToken);
        localStorage.setItem("onboardingComplete", String(completedOnboarding));
        setToken(newToken);
        setHasCompletedOnboarding(completedOnboarding);
    };

    const logout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("onboardingComplete");
        localStorage.removeItem("userName");
        localStorage.removeItem("defaultServings");
        setToken(null);
        setHasCompletedOnboarding(false);
        setUserName(null);
        setDefaultServings(2);
    };

    const refreshUserName = () => {
        if (!token) return;
        fetchAndStoreProfile();
    };

    const completeOnboarding = (): Promise<void> => {
        return new Promise((resolve) => {
            localStorage.setItem("onboardingComplete", "true");
            setHasCompletedOnboarding(true);
            resolve();
        });
    };

    return (
        <AuthContext.Provider
            value={{
                token,
                userName,
                defaultServings,
                login,
                logout,
                completeOnboarding,
                refreshUserName,
                isAuthenticated: !!token,
                hasCompletedOnboarding,
            }}
        >
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) throw new Error("useAuth must be used within AuthProvider");
    return context;
};