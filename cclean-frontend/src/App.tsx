import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { motion } from "framer-motion"; // For global animations
import { useAuth0 } from "@auth0/auth0-react";
import Profile from "./pages/Profile";
import Layout from "./components/Navbar";
import Home from "./pages/Home";
import Services from "./pages/Services";
import Appointments from "./pages/Appointments";
import AppointmentsAddForm from "./pages/AppointmentsAddForm";
import AppointmentsEditForm from "./pages/AppointmentsEditForm";
import AboutUs from "./pages/AboutUs";
import Contacts from "./pages/Contacts";
import Schedule from "./pages/Schedule";
import EmployeesList from "./components/employees/EmployeesList";
import CreateEmployee from "./components/employees/CreateEmployee";
import EditEmployee from "./components/employees/EditEmployee";
import AdminDashboard from "./components/dashboards/AdminDashboard";
import FormAddService from "./pages/FormAddService";
import FormEditService from "./pages/FormEditService";
import CreateCustomer from "./components/customers/CreateCustomer";
import EditCustomer from "./components/customers/EditCustomer";
import SubmitFeedback from "./pages/SubmitFeedback";
import CalendarTestPage from "./pages/CalendarTestPage";
import SelectDateTimePage from "./pages/SelectDateTimePage";
import CheckoutPage from "./pages/CheckoutPage";
import MyAppointments from "./pages/MyAppointments";
import CustomerAppointmentsEditForm from "./pages/CustomerAppointmentsEditForm";

import "./pages/GlobalOverride.css";
import "./index.css";

const App = () => {
  // From main branch
  const { isLoading } = useAuth0();
  if (isLoading) {
    return <div>Loading...</div>;
  }

  return (
    <Router>
      {/* Framer Motion fade animation wrapper (from feat/CCICC-78-UI_Overhaul) */}
      <motion.div
        className="global-page-wrapper"
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ duration: 0.4 }}
      >
        <Layout>
          <Routes>
            {/* Common routes */}
            <Route path="/" element={<Home />} />
            <Route path="/services" element={<Services />} />
            <Route path="/appointments" element={<Appointments />} />
            <Route path="/appointments/add" element={<AppointmentsAddForm />} />
            <Route
              path="/appointments/edit/:appointmentId"
              element={<AppointmentsEditForm />}
            />
            <Route path="/about-us" element={<AboutUs />} />
            <Route path="/contacts" element={<Contacts />} />
            <Route path="/employees" element={<EmployeesList />} />
            <Route path="/add-employee" element={<CreateEmployee />} />
            <Route
              path="/edit-employee/:employeeId"
              element={<EditEmployee />}
            />
            <Route path="/admin-dashboard" element={<AdminDashboard />} />
            <Route path="/schedule" element={<Schedule />} />

            {/* Services CRUD */}
            <Route path="/add-service" element={<FormAddService />} />
            <Route path="/edit-service/:serviceId" element={<FormEditService />} />

            {/* Customer CRUD */}
            <Route path="/add-customer" element={<CreateCustomer />} />
            <Route
              path="/edit-customer/:customerId"
              element={<EditCustomer />}
            />

            {/* Feedback/Calendar/Checkout */}
            <Route path="/submit-feedback" element={<SubmitFeedback />} />
            <Route path="/calendar-test" element={<CalendarTestPage />} />

            {/* Keep the motion fade specifically for calendar-select (feat/CCICC-78-UI_Overhaul) */}
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

            {/* Route from main branch */}
            <Route path="/profile" element={<Profile />} />
          </Routes>
        </Layout>
      </motion.div>

      {/* Toast notifications (feat/CCICC-78-UI_Overhaul) */}
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
