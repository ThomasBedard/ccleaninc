import { Link, useNavigate } from 'react-router-dom';
import './Navbarcss.css';
import { ReactNode } from 'react';

interface NavbarProps {
  children: ReactNode;
}

const Navbar = ({ children }: NavbarProps) => {
  const navigate = useNavigate();

  return (
    <>
      <nav className="navbar">
        <div className="navbar-brand">C CLEAN inc.</div>
        <div className="navbar-links">
          <Link to="/" className="navbar-link">
            Home
          </Link>
          <Link to="/services" className="navbar-link">
            Services
          </Link>
          <Link to="/appointments" className="navbar-link">
            Appointments
          </Link>
          <Link to="/schedule" className="navbar-link">
            Schedule
          </Link>
          <Link to="/employees" className="navbar-link">
            Employees
          </Link>
          <Link to="/about-us" className="navbar-link">
            About Us
          </Link>
          <Link to="/contacts" className="navbar-link">
            Contacts
          </Link>
        </div>
        <div className="navbar-actions">
          <button className="navbar-button">FR</button>
          <Link to="/login" className="navbar-link">
            Sign in
          </Link>
          <button
            onClick={() => navigate('/register')}
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
