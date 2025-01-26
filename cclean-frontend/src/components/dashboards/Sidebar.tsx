import { useState } from "react";

const Sidebar = () => {
  const [active, setActive] = useState("dashboard");

  const menuItems = [
    { id: "dashboard", label: "Dashboard", icon: "home" },
    { id: "search", label: "Search", icon: "search" },
    { id: "insights", label: "Insights", icon: "chart-bar" },
    { id: "docs", label: "Docs", icon: "document" },
    { id: "products", label: "Products", icon: "shopping-cart" },
    { id: "settings", label: "Settings", icon: "cog" },
  ];

  const getIcon = (iconName: string) => {
    switch (iconName) {
      case "home":
        return (
          <svg
            className="w-5 h-5"
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2"
              d="M3 12l2-2m0 0l7-7 7 7m-9 9v-8m6 8v-8m4-4h3m-3-4h3"
            />
          </svg>
        );
      case "search":
        return (
          <svg
            className="w-5 h-5"
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2"
              d="M21 21l-6-6m2-5a7 7 0 1114 0 7 7 0 01-14 0z"
            />
          </svg>
        );
      case "chart-bar":
        return (
          <svg
            className="w-5 h-5"
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2"
              d="M9 12h3m0 0h3m-3 0V9m0 3v3"
            />
          </svg>
        );
      case "document":
        return (
          <svg
            className="w-5 h-5"
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2"
              d="M9 12h6M12 9v6m0 0V9m0 3H6m6 3H9"
            />
          </svg>
        );
      case "shopping-cart":
        return (
          <svg
            className="w-5 h-5"
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2"
              d="M3 3h4l1 2h10l1-2h4"
            />
          </svg>
        );
      case "cog":
        return (
          <svg
            className="w-5 h-5"
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2"
              d="M3 3h4l1 2h10l1-2h4"
            />
          </svg>
        );
    }
  };

  return (
    <div className="flex flex-col w-64 h-screen bg-gray-100 text-gray-800">
      <div className="p-4 text-xl font-bold">C CLEAN INC.</div>
      <div className="flex-1">
        {menuItems.map((item) => (
          <button
            key={item.id}
            className={`flex items-center w-full px-4 py-3 text-left ${
              active === item.id
                ? "bg-gray-300 text-gray-800"
                : "hover:bg-gray-200 hover:text-gray-800"
            }`}
            onClick={() => setActive(item.id)}
          >
            {getIcon(item.icon)}
            <span className="ml-4">{item.label}</span>
          </button>
        ))}
      </div>
    </div>
  );
};

export default Sidebar;
