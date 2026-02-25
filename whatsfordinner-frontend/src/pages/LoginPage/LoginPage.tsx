import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import axios from "axios";

export default function LoginPage() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async () => {
        setError("");
        setLoading(true);
        try {
            const response = await axios.post("http://localhost:8080/api/auth/login", {
                email,
                password,
            });
            login(response.data.token);
            navigate("/fridge");
        } catch (err) {
            setError("Invalid email or password");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-cream-50 flex items-center justify-center p-4">
            <div className="w-full max-w-sm">

                {/* Hero */}
                <div className="bg-gradient-to-br from-primary-500 to-primary-700 rounded-3xl p-8 mb-6 text-center relative overflow-hidden">
                    <div className="relative z-10">
                        <div className="text-5xl mb-3">🍽️</div>
                        <h1 className="font-serif text-2xl text-white font-bold">What's For Dinner</h1>
                        <p className="text-orange-100 text-sm mt-1">Cook smarter, waste less</p>
                    </div>
                </div>

                {/* Form */}
                <div className="bg-white rounded-3xl p-6 shadow-sm">
                    <h2 className="text-xl font-semibold text-gray-800 mb-5">Sign In</h2>

                    {error && (
                        <div className="bg-red-50 text-red-600 text-sm rounded-xl p-3 mb-4">
                            {error}
                        </div>
                    )}

                    <div className="mb-4">
                        <label className="text-xs font-semibold text-gray-500 uppercase tracking-wide block mb-1.5">
                            Email
                        </label>
                        <input
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            className="w-full h-11 bg-cream-50 border border-cream-200 rounded-xl px-4 text-sm focus:outline-none focus:border-primary-400 focus:ring-2 focus:ring-primary-100"
                            placeholder="you@example.com"
                        />
                    </div>

                    <div className="mb-5">
                        <label className="text-xs font-semibold text-gray-500 uppercase tracking-wide block mb-1.5">
                            Password
                        </label>
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className="w-full h-11 bg-cream-50 border border-cream-200 rounded-xl px-4 text-sm focus:outline-none focus:border-primary-400 focus:ring-2 focus:ring-primary-100"
                            placeholder="••••••••"
                        />
                    </div>

                    <button
                        onClick={handleSubmit}
                        disabled={loading}
                        className="w-full h-12 bg-gradient-to-r from-primary-500 to-primary-600 text-white rounded-xl font-semibold text-sm shadow-md hover:shadow-lg transition-all disabled:opacity-60"
                    >
                        {loading ? "Signing in..." : "Sign In →"}
                    </button>

                    <p className="text-center text-sm text-gray-500 mt-4">
                        Don't have an account?{" "}
                        <Link to="/register" className="text-primary-500 font-semibold">
                            Register
                        </Link>
                    </p>
                </div>
            </div>
        </div>
    );
}