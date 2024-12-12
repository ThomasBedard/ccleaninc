import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Layout from './components/Navbar';
import Home from './pages/Home';
import Services from './pages/Services';
import Appointments from './pages/Appointments';
import AboutUs from './pages/AboutUs';
import Contacts from './pages/Contacts';
import Login from './pages/Login';
import Register from './pages/Register';
import Schedule from './pages/Schedule'; 
import Employees from './pages/Employees';
import FormAddService from './pages/FormAddService';

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
          <Route path="/schedule" element={<Schedule />} />
          <Route path="/add-service" element={<FormAddService />} />

        </Routes>
      </Layout>
    </Router>
  );
};

export default App;
