import { Link } from "react-router-dom";
import "./Navbarcss.css";
import { ReactNode } from "react";

// From feat/CCICC-78-UI_Overhaul
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
} from "react-icons/fa";

// From main
import LoginButton from "./buttons/LoginButton";
import LogoutButton from "./buttons/LogoutButton";

// 3) Navbar component
interface NavbarProps {
  children: ReactNode;
}

const Navbar = ({ children }: NavbarProps) => {


  const handleLanguageSwitch = () => {
    toast.info("Switched language to French (FR)");
    // Actual language switch logic would go here
  };

  return (
    <>
      <nav className="navbar">
        <div className="navbar-brand">C CLEAN inc.</div>

        {/* Navigation Links (feat/CCICC-78-UI_Overhaul's icon usage) */}
        <div className="navbar-links">
          <Link to="/" className="navbar-link">
            <FaHome style={{ marginRight: "4px" }} />
            Home
          </Link>
          <Link to="/services" className="navbar-link">
            <FaBuilding style={{ marginRight: "4px" }} />
            Services
          </Link>
          <Link to="/appointments" className="navbar-link">
            <FaCalendarAlt style={{ marginRight: "4px" }} />
            Appointments
          </Link>
          <Link to="/schedule" className="navbar-link">
            <FaCalendarCheck style={{ marginRight: "4px" }} />
            Schedule
          </Link>
          <Link to="/employees" className="navbar-link">
            <FaUserTie style={{ marginRight: "4px" }} />
            Employees
          </Link>
          <Link to="/admin-dashboard" className="navbar-link">
            <FaTools style={{ marginRight: "4px" }} />
            Admin Dashboard
          </Link>
          <Link to="/about-us" className="navbar-link">
            <FaInfoCircle style={{ marginRight: "4px" }} />
            About Us
          </Link>
          <Link to="/contacts" className="navbar-link">
            <FaPhone style={{ marginRight: "4px" }} />
            Contacts
          </Link>
          <Link to="/my-appointments" className="navbar-link">
            <FaCalendarAlt style={{ marginRight: "4px" }} />
            My Appointments
          </Link>
        </div>

        {/* Actions (Merged from both branches) */}
        <div className="navbar-actions">
          {/* Language Switch from feat/CCICC-78-UI_Overhaul */}
          <button className="navbar-button" onClick={handleLanguageSwitch}>
            FR
          </button>
          {/* Login/Logout/Profile from main */}
          <LoginButton />
          <LogoutButton />
          <Link to="/profile" className="navbar-link">
            Profile
          </Link>
        </div>
      </nav>
      <main>{children}</main>
    </>
  );
};

export default Navbar;