import { Link } from 'react-router-dom';
import './Navbarcss.css';
import { ReactNode } from 'react';
import LoginButton from './buttons/login-button';
import LogoutButton from './buttons/logout-button';

interface NavbarProps {
  children: ReactNode;
}

const Navbar = ({ children }: NavbarProps) => {

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
          <Link to="/admin-dashboard" className="navbar-link">
            Admin Dashboard
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
