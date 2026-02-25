import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import axios from "axios";

export default function RegisterPage() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async () => {
        setError("");

        if (password !== confirmPassword) {
            setError("Passwords do not match");
            return;
        }

        if (password.length < 8) {
            setError("Password must be at least 8 characters");
            return;
        }

        setLoading(true);
        try {
            await axios.post("http://localhost:8080/api/auth/register", {
                email,
                password,
            });
            navigate("/login");
        } catch (err: any) {
            const msg = err?.response?.data?.message;
            setError(typeof msg === "string" ? msg : "Registration failed. Please try again.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-cream-50 flex items-center justify-center p-4">
            <div className="w-full max-w-sm">

                {/* Hero */}
                <div className="bg-gradient-to-br from-primary-500 to-primary-700 rounded-3xl p-8 mb-6 text-center relative overflow-hidden">
                    <div className="absolute -top-6 -right-6 w-24 h-24 bg-white/10 rounded-full" />
                    <div className="absolute -bottom-8 -left-4 w-32 h-32 bg-white/5 rounded-full" />
                    <div className="relative z-10">
                        <div className="text-5xl mb-3">🍽️</div>
                        <h1 className="font-serif text-2xl text-white font-bold">What's For Dinner</h1>
                        <p className="text-orange-100 text-sm mt-1">Cook smarter, waste less</p>
                    </div>
                </div>

                {/* Form */}
                <div className="bg-white rounded-3xl p-6 shadow-sm">
                    <h2 className="text-xl font-semibold text-gray-800 mb-1">Create Account</h2>
                    <p className="text-sm text-gray-400 mb-5">Start cooking smarter today</p>

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
                            className="w-full h-11 bg-cream-50 border border-cream-200 rounded-xl px-4 text-sm focus:outline-none focus:border-primary-400 focus:ring-2 focus:ring-primary-100 transition-all"
                            placeholder="you@example.com"
                        />
                    </div>

                    <div className="mb-4">
                        <label className="text-xs font-semibold text-gray-500 uppercase tracking-wide block mb-1.5">
                            Password
                        </label>
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className="w-full h-11 bg-cream-50 border border-cream-200 rounded-xl px-4 text-sm focus:outline-none focus:border-primary-400 focus:ring-2 focus:ring-primary-100 transition-all"
                            placeholder="Min. 8 characters"
                        />
                    </div>

                    <div className="mb-5">
                        <label className="text-xs font-semibold text-gray-500 uppercase tracking-wide block mb-1.5">
                            Confirm Password
                        </label>
                        <input
                            type="password"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            className="w-full h-11 bg-cream-50 border border-cream-200 rounded-xl px-4 text-sm focus:outline-none focus:border-primary-400 focus:ring-2 focus:ring-primary-100 transition-all"
                            placeholder="••••••••"
                        />
                    </div>

                    {/* Password strength hint */}
                    {password.length > 0 && (
                        <div className="mb-4 -mt-3">
                            <div className="flex gap-1 mt-1">
                                {[...Array(4)].map((_, i) => (
                                    <div
                                        key={i}
                                        className={`h-1 flex-1 rounded-full transition-all duration-300 ${
                                            password.length === 0
                                                ? "bg-gray-100"
                                                : password.length < 6
                                                    ? i === 0 ? "bg-red-400" : "bg-gray-100"
                                                    : password.length < 8
                                                        ? i < 2 ? "bg-orange-400" : "bg-gray-100"
                                                        : password.length < 12
                                                            ? i < 3 ? "bg-yellow-400" : "bg-gray-100"
                                                            : "bg-green-400"
                                        }`}
                                    />
                                ))}
                            </div>
                            <p className="text-xs text-gray-400 mt-1">
                                {password.length < 6
                                    ? "Too short"
                                    : password.length < 8
                                        ? "Almost there"
                                        : password.length < 12
                                            ? "Good password"
                                            : "Strong password"}
                            </p>
                        </div>
                    )}

                    <button
                        onClick={handleSubmit}
                        disabled={loading}
                        className="w-full h-12 bg-gradient-to-r from-primary-500 to-primary-600 text-white rounded-xl font-semibold text-sm shadow-md hover:shadow-lg transition-all disabled:opacity-60"
                    >
                        {loading ? "Creating account..." : "Create Account →"}
                    </button>

                    <p className="text-center text-sm text-gray-500 mt-4">
                        Already have an account?{" "}
                        <Link to="/login" className="text-primary-500 font-semibold">
                            Sign In
                        </Link>
                    </p>
                </div>
            </div>
        </div>
    );
}