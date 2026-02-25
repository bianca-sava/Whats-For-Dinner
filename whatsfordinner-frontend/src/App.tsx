import { Routes, Route, Navigate } from "react-router-dom";
import { useAuth } from "./context/AuthContext";
import LoginPage from "./pages/LoginPage.tsx";
import RegisterPage from "./pages/RegisterPage.tsx";
import OnboardingPage from "./pages/OnboardingPage.tsx";
import Navbar from "./components/Navbar";
import FridgePage from "./pages/FridgePage";
import RecipesPage from "./pages/RecipesPage";
import RecipePage from "./pages/RecipePage";
import ProfilePage from "./pages/ProfilePage";

function AuthenticatedLayout({ children }: { children: React.ReactNode }) {
    return (
        <div className="min-h-screen bg-cream-50">
            <Navbar />
            <main className="max-w-5xl mx-auto px-4 py-6">
                {children}
            </main>
        </div>
    );
}

function ProtectedRoute({ children }: { children: React.ReactNode }) {
    const { isAuthenticated } = useAuth();
    return isAuthenticated ? <>{children}</> : <Navigate to="/login" />;
}

function App() {
    const { isAuthenticated, hasCompletedOnboarding } = useAuth();

    return (
        <Routes>
            {/* Public routes */}
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />

            {/* Onboarding — must be logged in but hasn't completed quiz */}
            <Route
                path="/onboarding"
                element={
                    <ProtectedRoute>
                        <OnboardingPage />
                    </ProtectedRoute>
                }
            />

            {/* Protected routes */}
            <Route
                path="/fridge"
                element={
                    <ProtectedRoute>
                        <AuthenticatedLayout>
                            <FridgePage />
                        </AuthenticatedLayout>
                    </ProtectedRoute>
                }
            />
            <Route
                path="/recipes"
                element={
                    <ProtectedRoute>
                        <AuthenticatedLayout>
                            <RecipesPage />
                        </AuthenticatedLayout>
                    </ProtectedRoute>
                }
            />
            <Route
                path="/profile"
                element={
                    <ProtectedRoute>
                        <AuthenticatedLayout>
                            <ProfilePage />
                        </AuthenticatedLayout>
                    </ProtectedRoute>
                }
            />
            <Route
                path="/recipes/:id"
                element={
                    <ProtectedRoute>
                        <AuthenticatedLayout>
                            <RecipePage />
                        </AuthenticatedLayout>
                    </ProtectedRoute>
                }
            />

            {/* Default redirect */}
            <Route
                path="/"
                element={
                    !isAuthenticated
                        ? <Navigate to="/login" />
                        : !hasCompletedOnboarding
                            ? <Navigate to="/onboarding" />
                            : <Navigate to="/fridge" />
                }
            />
        </Routes>
    );
}

export default App;