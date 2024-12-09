import { Link, useNavigate } from 'react-router-dom';

const Navbar = () => {
  const navigate = useNavigate();

  return (
    <nav className="bg-white shadow-md px-6 py-4 flex justify-between items-center">
      {/* Logo */}
      <div className="text-2xl font-bold text-gray-900">C CLEAN inc.</div>

      {/* Navigation Links */}
      <div className="hidden md:flex gap-6 text-gray-800 font-medium">
        <Link to="/" className="hover:text-blue-500">
          Home
        </Link>
        <Link to="/services" className="hover:text-blue-500">
          Services
        </Link>
        <Link to="/appointments" className="hover:text-blue-500">
          Appointments
        </Link>
        <Link to="/about-us" className="hover:text-blue-500">
          About Us
        </Link>
        <Link to="/contacts" className="hover:text-blue-500">
          Contacts
        </Link>
        <Link to="/employees" className="hover:text-blue-500">
          Employees
        </Link>
      </div>

      {/* Language and Authentication */}
      <div className="flex items-center gap-4">
        <button className="text-gray-600 font-medium hover:text-blue-500">
          FR
        </button>
        <Link to="/login" className="text-gray-600 font-medium hover:text-blue-500">
          Sign in
        </Link>
        <button
          onClick={() => navigate('/register')}
          className="border border-gray-300 px-4 py-2 rounded-md text-gray-600 hover:border-blue-500 hover:text-blue-500"
        >
          Register
        </button>
      </div>
    </nav>
  );
};

export default Navbar;