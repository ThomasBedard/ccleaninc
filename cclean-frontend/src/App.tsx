import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Layout from './components/Navbarcss';
import Home from './pages/Home';
import Services from './pages/Services';
import Appointments from './pages/Appointments';
import AboutUs from './pages/AboutUs';
import Contacts from './pages/Contacts';
import Login from './pages/Login';
import Register from './pages/Register';
import Employees from './pages/Employees';
import Schedule from './pages/Schedule'; // Import the Schedule page


const App = () => {
  return (
    <Router>
      <Layout>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/services" element={<Services />} />
          <Route path="/appointments" element={<Appointments />} />
          <Route path="/about-us" element={<AboutUs />} />
          <Route path="/contacts" element={<Contacts />} />
          <Route path="/employees" element={<Employees />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/schedule" element={<Schedule />} /> {/* Add this line */}

        </Routes>
      </Layout>
    </Router>
  );
};

export default App;

