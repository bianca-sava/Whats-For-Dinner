import { createContext, useContext, useState, useEffect } from "react";
import type { ReactNode } from "react";
import apiClient from "../api/client";

interface AuthContextType {
    token: string | null;
    userName: string | null;
    login: (token: string, completedOnboarding: boolean) => void;
    logout: () => void;
    completeOnboarding: () => void;
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

    useEffect(() => {
        if (!token) return;
        apiClient
            .get("/api/profile/me")
            .then(res => {
                const name = res.data.firstName ?? null;
                setUserName(name);
                if (name) localStorage.setItem("userName", name);
            })
            .catch(() => {
            });
    }, [token]);

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
        setToken(null);
        setHasCompletedOnboarding(false);
        setUserName(null);
    };

    const refreshUserName = () => {
        if (!token) return;
        apiClient
            .get("/api/profile/me")
            .then(res => {
                const name = res.data.firstName ?? null;
                setUserName(name);
                if (name) localStorage.setItem("userName", name);
            })
            .catch(() => {});
    };

    const completeOnboarding = () => {
        localStorage.setItem("onboardingComplete", "true");
        setHasCompletedOnboarding(true);
    };

    return (
        <AuthContext.Provider
            value={{
                token,
                userName,
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