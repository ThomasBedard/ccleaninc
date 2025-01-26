import { Link, useNavigate } from "react-router-dom";
import "./Navbarcss.css";
import { ReactNode } from "react";
import { toast } from "react-toastify";
import { FaHome, FaBuilding, FaCalendarAlt, FaUserTie, FaTools, FaInfoCircle, FaPhone, FaCalendarCheck } from 'react-icons/fa';

interface NavbarProps {
  children: ReactNode;
}

const Navbar = ({ children }: NavbarProps) => {
  const navigate = useNavigate();

  const handleLanguageSwitch = () => {
    // For demonstration, show a toast instead of an alert
    toast.info("Switched language to French (FR)");
    // any actual language switch logic here...
  };

  return (
    <>
      <nav className="navbar">
        <div className="navbar-brand">C CLEAN inc.</div>
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
        <div className="navbar-actions">
          <button className="navbar-button" onClick={handleLanguageSwitch}>
            FR
          </button>
          <Link to="/login" className="navbar-link">
            Sign in
          </Link>
          <button
            onClick={() => navigate("/register")}
            className="navbar-register"
          >
            Register
          </button>
        </div>
      </nav>
      <main>{children}</main>
    </>
  );
};

export default Navbar;
