import { Routes, Route, Navigate } from "react-router-dom";
import { useAuth } from "./context/AuthContext";
import LoginPage from "./pages/LoginPage/LoginPage";
import RegisterPage from "./pages/RegisterPage/RegisterPage";
import Navbar from "./components/Navbar";

// Placeholder pages — replace these as we build each one
const FridgePage = () => <div className="p-8 text-center text-gray-400">Fridge Page — coming soon</div>;
const RecipesPage = () => <div className="p-8 text-center text-gray-400">Recipes Page — coming soon</div>;
const ProfilePage = () => <div className="p-8 text-center text-gray-400">Profile Page — coming soon</div>;

// Layout wrapper for authenticated pages (includes navbar)
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

// Route guard
function ProtectedRoute({ children }: { children: React.ReactNode }) {
    const { isAuthenticated } = useAuth();
    return isAuthenticated ? <>{children}</> : <Navigate to="/login" />;
}

function App() {
    const { isAuthenticated } = useAuth();

    return (
        <Routes>
            {/* Public routes */}
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />

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

            {/* Default redirect */}
            <Route
                path="/"
                element={<Navigate to={isAuthenticated ? "/fridge" : "/login"} />}
            />
        </Routes>
    );
}

export default App;