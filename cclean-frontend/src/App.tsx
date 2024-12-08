import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Layout from './components/Navbarcss';
import Home from './pages/Home';
import Services from './pages/Services';
import Appointments from './pages/Appointments';
import AboutUs from './pages/AboutUs';
import Contacts from './pages/Contacts';
import Login from './pages/Login';
import Register from './pages/Register';
import Schedule from './pages/Schedule'; // Import the Schedule page



const App = () => {
  return (
    <Router>
      {/* Shared background color and full height */}
      <div className="bg-gradient-to-b from-[#E4EDF5] to-[#F7F8FC] min-h-screen">
        <Navbar />
        <div className="max-w-7xl mx-auto px-4">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/services" element={<Services />} />
            <Route path="/appointments" element={<Appointments />} />
            <Route path="/about-us" element={<AboutUs />} />
            <Route path="/contacts" element={<Contacts />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/schedule" element={<Schedule />} /> {/* Add this line */}
          </Routes>
        </div>
      </div>

      <Layout>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/services" element={<Services />} />
          <Route path="/appointments" element={<Appointments />} />
          <Route path="/about-us" element={<AboutUs />} />
          <Route path="/contacts" element={<Contacts />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
        </Routes>
      </Layout>
    </Router>
  );
};

export default App;
