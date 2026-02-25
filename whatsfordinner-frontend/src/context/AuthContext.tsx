import { createContext, useContext, useState } from "react";
import type { ReactNode } from "react";

interface AuthContextType {
    token: string | null;
    login: (token: string, completedOnboarding: boolean) => void;
    logout: () => void;
    completeOnboarding: () => void;
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

    const login = (newToken: string, completedOnboarding: boolean) => {
        localStorage.setItem("token", newToken);
        localStorage.setItem("onboardingComplete", String(completedOnboarding));
        setToken(newToken);
        setHasCompletedOnboarding(completedOnboarding);
    };

    const logout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("onboardingComplete");
        setToken(null);
        setHasCompletedOnboarding(false);
    };

    const completeOnboarding = () => {
        localStorage.setItem("onboardingComplete", "true");
        setHasCompletedOnboarding(true);
    };

    return (
        <AuthContext.Provider
            value={{
                token,
                login,
                logout,
                completeOnboarding,
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