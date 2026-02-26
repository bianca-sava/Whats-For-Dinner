import { Link, useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext.tsx";

const navItems = [
    {
        path: "/fridge",
        label: "My Fridge",
        icon: (
            <svg xmlns="http://www.w3.org/2000/svg" className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                <path strokeLinecap="round" strokeLinejoin="round" d="M20 7H4a2 2 0 00-2 2v10a2 2 0 002 2h16a2 2 0 002-2V9a2 2 0 00-2-2z" />
                <path strokeLinecap="round" strokeLinejoin="round" d="M16 3v4M8 3v4M3 13h18" />
            </svg>
        ),
    },
    {
        path: "/recipes",
        label: "Recipes",
        icon: (
            <svg xmlns="http://www.w3.org/2000/svg" className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                <path strokeLinecap="round" strokeLinejoin="round" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
            </svg>
        ),
    },
    {
        path: "/profile",
        label: "Profile",
        icon: (
            <svg xmlns="http://www.w3.org/2000/svg" className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                <path strokeLinecap="round" strokeLinejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
            </svg>
        ),
    },
];

export default function Navbar() {
    const { logout, userName } = useAuth();
    const location = useLocation();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate("/login");
    };

    const greeting = userName
        ? `What's for Dinner, ${userName}?`
        : "What's for Dinner";

    return (
        <>
            {/* ── Desktop top navbar ── */}
            <nav className="bg-white border-b border-cream-100 sticky top-0 z-50">
                <div className="max-w-5xl mx-auto px-4 h-16 flex items-center justify-between">

                    {/* Logo */}
                    <Link to="/fridge" className="flex items-center gap-2.5 group">
                        <span className="text-2xl">🍽️</span>
                        <span className="font-serif font-bold text-gray-800 text-lg leading-none">
                            {greeting}
                        </span>
                    </Link>

                    {/* Nav links*/}
                    <div className="hidden sm:flex items-center gap-1">
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
                                    {item.icon}
                                    <span>{item.label}</span>
                                </Link>
                            );
                        })}

                        <div className="w-px h-5 bg-gray-200 mx-2" />

                        <button
                            onClick={handleLogout}
                            className="flex items-center gap-1.5 px-4 py-2 rounded-xl text-sm font-medium text-gray-500 hover:text-red-500 hover:bg-red-50 transition-all"
                        >
                            <svg xmlns="http://www.w3.org/2000/svg" className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                                <path strokeLinecap="round" strokeLinejoin="round" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a2 2 0 01-2 2H5a2 2 0 01-2-2V7a2 2 0 012-2h6a2 2 0 012 2v1" />
                            </svg>
                            <span>Logout</span>
                        </button>
                    </div>
                </div>
            </nav>

            {/* ── Mobile bottom navbar ── */}
            <nav className="sm:hidden fixed bottom-0 left-0 right-0 z-50 bg-white border-t border-gray-100 pb-safe">
                <div className="flex items-center justify-around h-16">
                    {navItems.map((item) => {
                        const isActive = location.pathname === item.path;
                        return (
                            <Link
                                key={item.path}
                                to={item.path}
                                className={`flex flex-col items-center justify-center gap-1 flex-1 h-full text-xs font-medium transition-all ${
                                    isActive
                                        ? "text-primary-600"
                                        : "text-gray-400 hover:text-gray-600"
                                }`}
                            >
                                <span className={`transition-all ${isActive ? "scale-110" : ""}`}>
                                    {item.icon}
                                </span>
                                <span>{item.label}</span>
                            </Link>
                        );
                    })}

                    {/* Logout button in bottom nav */}
                    <button
                        onClick={handleLogout}
                        className="flex flex-col items-center justify-center gap-1 flex-1 h-full text-xs font-medium text-gray-400 hover:text-red-500 transition-all"
                    >
                        <svg xmlns="http://www.w3.org/2000/svg" className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                            <path strokeLinecap="round" strokeLinejoin="round" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a2 2 0 01-2 2H5a2 2 0 01-2-2V7a2 2 0 012-2h6a2 2 0 012 2v1" />
                        </svg>
                        <span>Logout</span>
                    </button>
                </div>
            </nav>

            {/* Spacer so content doesn't hide behind bottom nav on mobile */}
            <div className="sm:hidden h-16" />
        </>
    );
}