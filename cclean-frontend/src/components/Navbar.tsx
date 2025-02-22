import { Link } from "react-router-dom";
import "./Navbarcss.css";
import { ReactNode, useEffect, useState } from "react";
import { useLanguage } from "../hooks/useLanguage";
import { toast } from "react-toastify";
import {
  FaHome,
  FaBuilding,
  FaCalendarAlt,
  FaTools,
  FaInfoCircle,
  FaPhone,
  FaCalendarCheck,
  FaClock,
} from "react-icons/fa";
import LoginButton from "./buttons/LoginButton";
import LogoutButton from "./buttons/LogoutButton";

import { useAuth0 } from "@auth0/auth0-react";
import { jwtDecode } from "jwt-decode";

interface NavbarProps {
  children: ReactNode;
}

const Navbar = ({ children }: NavbarProps) => {
  const { language, translations, toggleLanguage } = useLanguage();
  const { isAuthenticated, getAccessTokenSilently } = useAuth0();

  // State to hold permissions
  const [permissions, setPermissions] = useState<string[]>([]);
  const [isAnonymous, setIsAnonymous] = useState<boolean>(true); // Track if user is anonymous

  useEffect(() => {
    const fetchPermissions = async () => {
      try {
        const token = await getAccessTokenSilently();
        interface DecodedToken {
          permissions: string[];
        }
        const decodedToken: DecodedToken = jwtDecode<DecodedToken>(token);
        setPermissions(decodedToken.permissions || []);
        setIsAnonymous(false); // User is authenticated
      } catch (error) {
        console.error("Error fetching token/permissions:", error);
        setPermissions([]); // No permissions for anonymous users
        setIsAnonymous(true); // User is anonymous
      }
    };

    if (isAuthenticated) {
      fetchPermissions();
    } else {
      setIsAnonymous(true);
    }
  }, [isAuthenticated, getAccessTokenSilently]);

  // Define roles based on permissions
  const isAdmin = permissions.includes("admin");
  const isEmployee = permissions.includes("employee");
  const isCustomer = permissions.includes("customer");

  const handleLanguageSwitch = () => {
    toggleLanguage();
    toast.info(
      language === "en"
        ? "Langue changée en Français (FR)"
        : "Switched language to English (EN)"
    );
  };

  return (
    <>
      <nav className="navbar">
        <div className="navbar-brand">
          {translations.navbar?.brand || "C CLEAN inc."}
        </div>

        <div className="navbar-links">
          {/* Public pages (always visible) */}
          <Link to="/" className="navbar-link">
            <FaHome style={{ marginRight: "4px" }} />
            {translations.navbar?.home || "Home"}
          </Link>
          {" "}
          {!isAdmin && (
            <Link to="/services" className="navbar-link">
              <FaBuilding style={{ marginRight: "4px" }} />
              {translations.navbar?.services || "Services"}
            </Link>
          )}
          <Link to="/about-us" className="navbar-link">
            <FaInfoCircle style={{ marginRight: "4px" }} />
            {translations.navbar?.about_us || "About Us"}
          </Link>
          <Link to="/contacts" className="navbar-link">
            <FaPhone style={{ marginRight: "4px" }} />
            {translations.navbar?.contacts || "Contacts"}
          </Link>
          {/* Authenticated users only */}
          {!isAnonymous && (
            <>
              {isEmployee && (
                <>
                  <Link to="/appointments" className="navbar-link">
                    <FaCalendarAlt style={{ marginRight: "4px" }} />
                    {translations.navbar?.appointments || "Appointments"}
                  </Link>
                  <Link to="/schedule" className="navbar-link">
                    <FaCalendarCheck style={{ marginRight: "4px" }} />
                    {translations.navbar?.schedule || "Schedule"}
                  </Link>
                </>
              )}

              {isAdmin && (
                <>
                  <Link to="/admin-dashboard" className="navbar-link">
                    <FaTools style={{ marginRight: "4px" }} />
                    {translations.navbar?.admin_dashboard || "Admin Dashboard"}
                  </Link>
                </>
              )}

              {isEmployee && (
                <Link to="/my-availabilities" className="navbar-link">
                  <FaClock style={{ marginRight: "4px" }} />
                  {translations.navbar?.my_availabilities ||
                    "My Availabilities"}
                </Link>
              )}

              {(isEmployee || isCustomer) && (
                <Link to="/my-appointments" className="navbar-link">
                  <FaCalendarAlt style={{ marginRight: "4px" }} />
                  {translations.navbar?.my_appointments || "My Appointments"}
                </Link>
              )}
            </>
          )}
        </div>

        <div className="navbar-actions">
          <button className="navbar-button" onClick={handleLanguageSwitch}>
            {language === "en" ? "FR" : "EN"}
          </button>

          {/* Show Login button only if the user is anonymous */}
          {isAnonymous ? <LoginButton /> : <LogoutButton />}

          {!isAnonymous && (
            <Link to="/profile" className="navbar-link">
              {translations.navbar?.profile || "Profile"}
            </Link>
          )}
        </div>
      </nav>
      <main>{children}</main>
    </>
  );
};

export default Navbar;
