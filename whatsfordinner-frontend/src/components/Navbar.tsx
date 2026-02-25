import { Link, useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext.tsx";

const navItems = [
    { path: "/fridge", label: "My Fridge", icon: "" },
    { path: "/recipes", label: "Recipes", icon: "" },
    { path: "/profile", label: "Profile", icon: "" },
];

export default function Navbar() {
    const { logout } = useAuth();
    const location = useLocation();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate("/login");
    };

    return (
        <nav className="bg-white border-b border-cream-100 sticky top-0 z-50">
            <div className="max-w-5xl mx-auto px-4 h-16 flex items-center justify-between">

                {/* Logo */}
                <Link to="/fridge" className="flex items-center gap-2.5 group">
                    <span className="text-2xl">🍽️</span>
                    <span className="font-serif font-bold text-gray-800 text-lg leading-none">
                        What's For Dinner
                    </span>
                </Link>

                {/* Nav links */}
                <div className="flex items-center gap-1">
                    {navItems.map((item) => {
                        const isActive = location.pathname === item.path;
                        return (
                            <Link
                                key={item.path}
                                to={item.path}
                                className={`flex items-center gap-1.5 px-4 py-2 rounded-xl text-sm font-medium transition-all ${
                                    isActive
                                        ? "bg-primary-50 text-primary-600"
                                        : "text-gray-500 hover:text-gray-800 hover:bg-gray-50"
                                }`}
                            >
                                <span className="text-base">{item.icon}</span>
                                <span className="hidden sm:inline">{item.label}</span>
                            </Link>
                        );
                    })}

                    {/* Divider */}
                    <div className="w-px h-5 bg-gray-200 mx-2" />

                    {/* Logout */}
                    <button
                        onClick={handleLogout}
                        className="flex items-center gap-1.5 px-4 py-2 rounded-xl text-sm font-medium text-gray-500 hover:text-red-500 hover:bg-red-50 transition-all"
                    >
                        <span>↩</span>
                        <span className="hidden sm:inline">Logout</span>
                    </button>
                </div>
            </div>
        </nav>
    );
}