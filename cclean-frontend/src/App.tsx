import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { motion } from 'framer-motion'; // For global animations
import Layout from './components/Navbar';
import Home from './pages/Home';
import Services from './pages/Services';
import Appointments from './pages/Appointments';
import AppointmentsAddForm from './pages/AppointmentsAddForm';
import AppointmentsEditForm from './pages/AppointmentsEditForm';
import AboutUs from './pages/AboutUs';
import Contacts from './pages/Contacts';
import Login from './pages/Login';
import Register from './pages/Register';
import Schedule from './pages/Schedule';
import EmployeesList from './components/employees/EmployeesList';
import CreateEmployee from './components/employees/CreateEmployee';
import EditEmployee from './components/employees/EditEmployee';
import AdminDashboard from './components/dashboards/AdminDashboard';
import FormAddService from './pages/FormAddService';
import FormEditService from './pages/FormEditService';
import CreateCustomer from './components/customers/CreateCustomer';
import EditCustomer from './components/customers/EditCustomer';
import SubmitFeedback from './pages/SubmitFeedback';
import CalendarTestPage from './pages/CalendarTestPage';
import SelectDateTimePage from './pages/SelectDateTimePage';
import CheckoutPage from './pages/CheckoutPage';
import MyAppointments from './pages/MyAppointments';
import CustomerAppointmentsEditForm from './pages/CustomerAppointmentsEditForm';
import './pages/GlobalOverride.css';
import './index.css';

const App = () => {
  return (
    <Router>
      {/* Use Framer Motion to apply fade animations globally */}
      <motion.div
        className="global-page-wrapper"
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ duration: 0.4 }}
      >
        <Layout>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/services" element={<Services />} />
            <Route path="/appointments" element={<Appointments />} />
            <Route path="/appointments/add" element={<AppointmentsAddForm />} />
            <Route path="/appointments/edit/:appointmentId" element={<AppointmentsEditForm />} />
            <Route path="/about-us" element={<AboutUs />} />
            <Route path="/contacts" element={<Contacts />} />
            {/* Employee routes */}
            <Route path="/employees" element={<EmployeesList />} />
            <Route path="/add-employee" element={<CreateEmployee />} />
            <Route path="/edit-employee/:employeeId" element={<EditEmployee />} />
            <Route path="/admin-dashboard" element={<AdminDashboard />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/schedule" element={<Schedule />} />
            <Route path="/add-service" element={<FormAddService />} />
            <Route path="/edit-service/:serviceId" element={<FormEditService />} />
            <Route path="/add-customer" element={<CreateCustomer />} />
            <Route path="/edit-customer/:customerId" element={<EditCustomer />} />
            <Route path="/submit-feedback" element={<SubmitFeedback />} />
            <Route path="/calendar-test" element={<CalendarTestPage />} />
            <Route
              path="/calendar-select"
              element={
                <motion.div
                  className="plain-page-wrapper"
                  initial={{ opacity: 0 }}
                  animate={{ opacity: 1 }}
                  transition={{ duration: 0.4 }}
                >
                  <SelectDateTimePage />
                </motion.div>
              }
            />
            <Route path="/checkout" element={<CheckoutPage />} />
            <Route path="/my-appointments" element={<MyAppointments />} />
            <Route
              path="/my-appointments/edit/:appointmentId"
              element={<CustomerAppointmentsEditForm />}
            />
          </Routes>
        </Layout>
      </motion.div>

      {/* Toast notifications */}
      <ToastContainer
        position="top-right"
        autoClose={3000}
        hideProgressBar={false}
        newestOnTop
        closeOnClick
        pauseOnHover
        theme="colored"
      />
    </Router>
  );
};

export default App;
