import { Routes, Route, Navigate } from "react-router-dom";
import { useAuth } from "./context/AuthContext";
import LoginPage from "./pages/LoginPage/LoginPage";

function App() {
    const { isAuthenticated } = useAuth();

    return (
        <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<div>Register Page - coming soon</div>} />
            <Route
                path="/fridge"
                element={isAuthenticated ? <div>Fridge Page - coming soon</div> : <Navigate to="/login" />}
            />
            <Route
                path="/"
                element={<Navigate to={isAuthenticated ? "/fridge" : "/login"} />}
            />
        </Routes>
    );
}

export default App;