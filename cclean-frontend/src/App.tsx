import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Layout from './components/Navbar';
import Home from './pages/Home';
import Services from './pages/Services';
import Appointments from './pages/Appointments';
import AboutUs from './pages/AboutUs';
import Contacts from './pages/Contacts';
import Schedule from './pages/Schedule'; 
import Employees from './pages/Employees';
import AdminDashboard from './components/dashboards/AdminDashboard';
import FormAddService from './pages/FormAddService';
import FormEditService from './pages/FormEditService';
import CreateCustomer from './components/customers/CreateCustomer';
import EditCustomer from './components/customers/EditCustomer';
import SubmitFeedback from './pages/SubmitFeedback';
import Profile from './pages/Profile';

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
          <Route path="/admin-dashboard" element={<AdminDashboard />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/schedule" element={<Schedule />} />
          <Route path="/add-service" element={<FormAddService />} />
          <Route path="/edit-service/:serviceId" element={<FormEditService />} /> 
          <Route path="/add-customer" element={<CreateCustomer />} />
          <Route path="/edit-customer/:customerId" element={<EditCustomer />} />
          <Route path="/submit-feedback" element={<SubmitFeedback />} />
        </Routes>
      </Layout>
    </Router>
  );
};

export default App;
