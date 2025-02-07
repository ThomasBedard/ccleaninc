import { Link } from "react-router-dom";
import "./Navbarcss.css";
import { ReactNode } from "react";
import { useLanguage } from "../hooks/useLanguage";

import { toast } from "react-toastify";
import {
  FaHome,
  FaBuilding,
  FaCalendarAlt,
  FaUserTie,
  FaTools,
  FaInfoCircle,
  FaPhone,
  FaCalendarCheck,
  FaClock,
  FaUserCircle, 
} from "react-icons/fa";

import LoginButton from "./buttons/LoginButton";
import LogoutButton from "./buttons/LogoutButton";

interface NavbarProps {
  children: ReactNode;
}

const Navbar = ({ children }: NavbarProps) => {
  const { language, translations, toggleLanguage } = useLanguage(); // Get translations from context

  const handleLanguageSwitch = (newLanguage: string) => {
    if (newLanguage !== language) {
      toggleLanguage();
      toast.info(
        newLanguage === "en"
          ? "Switched language to English (EN)"
          : "Langue changée en Français (FR)"
      );
    }
  };

  return (
    <>
      <nav className="navbar">
        <div className="navbar-brand">{translations.navbar?.brand || "C CLEAN inc."}</div>

        <div className="navbar-links">
          <Link to="/" className="navbar-link">
            <FaHome style={{ marginRight: "4px" }} />
            {translations.navbar?.home || "Home"}
          </Link>
          <Link to="/services" className="navbar-link">
            <FaBuilding style={{ marginRight: "4px" }} />
            {translations.navbar?.services || "Services"}
          </Link>
          <Link to="/appointments" className="navbar-link">
            <FaCalendarAlt style={{ marginRight: "4px" }} />
            {translations.navbar?.appointments || "Appointments"}
          </Link>
          <Link to="/schedule" className="navbar-link">
            <FaCalendarCheck style={{ marginRight: "4px" }} />
            {translations.navbar?.schedule || "Schedule"}
          </Link>
          <Link to="/employees" className="navbar-link">
            <FaUserTie style={{ marginRight: "4px" }} />
            {translations.navbar?.employees || "Employees"}
          </Link>
          <Link to="/admin-dashboard" className="navbar-link">
            <FaTools style={{ marginRight: "4px" }} />
            {translations.navbar?.admin_dashboard || "Admin Dashboard"}
          </Link>
          <Link to="/about-us" className="navbar-link">
            <FaInfoCircle style={{ marginRight: "4px" }} />
            {translations.navbar?.about_us || "About Us"}
          </Link>
          <Link to="/contacts" className="navbar-link">
            <FaPhone style={{ marginRight: "4px" }} />
            {translations.navbar?.contacts || "Contacts"}
          </Link>
          {/* My Availabilities link for employees */}
          <Link to="/my-availabilities" className="navbar-link">
            <FaClock style={{ marginRight: "4px" }} />
            {translations.navbar?.my_availabilities || "My Availabilities"}
          </Link>
          {/* My Appointments link */}
          <Link to="/my-appointments" className="navbar-link">
            <FaCalendarAlt style={{ marginRight: "4px" }} />
            {translations.navbar?.my_appointments || "My Appointments"}
          </Link>
        </div>

        <div className="navbar-actions">
          {/* Language Selector Dropdown */}
          <select
            className="language-selector"
            onChange={(e) => handleLanguageSwitch(e.target.value)}
            value={language}
          >
            <option value="en">EN</option>
            <option value="fr">FR</option>
          </select>

          <LoginButton />
          <LogoutButton />
          <Link to="/profile" className="navbar-link profile-icon">
            <FaUserCircle size={24} />
          </Link>
        </div>
      </nav>
      <main>{children}</main>
    </>
  );
};

export default Navbar;
