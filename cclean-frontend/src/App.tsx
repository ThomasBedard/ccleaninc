import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import { motion } from "framer-motion";
import { useAuth0 } from "@auth0/auth0-react";
import { LanguageProvider } from "./context/LanguageProvider";
import Navbar from "./components/Navbar";

import Profile from "./pages/Profile";
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
import MyAvailabilities from "./pages/MyAvailabilities";
import EmployeeAvailabilitiesEditForm from "./pages/EmployeeAvailabilitiesEditForm";
import AddAvailability from "./pages/AddAvailability";
import AssignScheduleModal from "./components/dashboards/AssignScheduleModal";
import EmployeeSchedule from "./components/dashboards/EmployeeSchedule";

import "./pages/GlobalOverride.css";
import "./index.css";

import PrivateRoute from "./pages/PrivateRoute";

const App = () => {
  const { isLoading } = useAuth0();
  if (isLoading) {
    return <div>Loading...</div>;
  }

  return (
    <LanguageProvider>
      <Router>
        <Navbar>
          <motion.div
            className="global-page-wrapper"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ duration: 0.4 }}
          >
            <Routes>
              {/* Public routes (Accessible to all users) */}
              <Route path="/" element={<Home />} />
              <Route path="/services" element={<Services />} />
              <Route path="/about-us" element={<AboutUs />} />
              <Route path="/contacts" element={<Contacts />} />

              {/* 🔐 Protected Routes */}
              {/* Admin & Employee Routes */}
              <Route
                element={
                  <PrivateRoute requiredPermissions={["admin", "employee"]} />
                }
              >
                <Route path="/appointments" element={<Appointments />} />
                <Route
                  path="/appointments/add"
                  element={<AppointmentsAddForm />}
                />
                <Route
                  path="/appointments/edit/:appointmentId"
                  element={<AppointmentsEditForm />}
                />
                <Route path="/schedule" element={<Schedule />} />
                <Route
                  path="/my-availabilities"
                  element={<MyAvailabilities />}
                />
                <Route
                  path="/my-availabilities/edit/:availabilityId"
                  element={<EmployeeAvailabilitiesEditForm />}
                />
                <Route path="/add-availability" element={<AddAvailability />} />
                <Route
                  path="/assign-schedule/:availabilityId"
                  element={<AssignScheduleModal />}
                />
                <Route
                  path="/employee-schedule"
                  element={<EmployeeSchedule />}
                />
              </Route>

              {/* 🔐 Admin-Only Routes */}
              <Route element={<PrivateRoute requiredPermissions={["admin"]} />}>
                <Route path="/admin-dashboard" element={<AdminDashboard />} />
                <Route path="/employees" element={<EmployeesList />} />
                <Route path="/add-employee" element={<CreateEmployee />} />
                <Route
                  path="/edit-employee/:employeeId"
                  element={<EditEmployee />}
                />
                <Route path="/add-service" element={<FormAddService />} />
                <Route
                  path="/edit-service/:serviceId"
                  element={<FormEditService />}
                />
                <Route path="/add-customer" element={<CreateCustomer />} />
                <Route
                  path="/edit-customer/:customerId"
                  element={<EditCustomer />}
                />
              </Route>

              {/* 🔐 Routes for All Authenticated Users (Admin, Employee, Customer) */}
              <Route
                element={
                  <PrivateRoute
                    requiredPermissions={["admin", "employee", "customer"]}
                  />
                }
              >
                <Route path="/my-appointments" element={<MyAppointments />} />
                <Route
                  path="/my-appointments/edit/:appointmentId"
                  element={<CustomerAppointmentsEditForm />}
                />
                <Route path="/submit-feedback" element={<SubmitFeedback />} />
                <Route path="/checkout" element={<CheckoutPage />} />
                <Route
                  path="/calendar-select"
                  element={<SelectDateTimePage />}
                />
                <Route path="/profile" element={<Profile />} />
              </Route>

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
            </Routes>
          </motion.div>
        </Navbar>

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
    </LanguageProvider>
  );
};

export default App;
