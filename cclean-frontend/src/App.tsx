import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Layout from './components/Navbar';
import Home from './pages/Home';
import Services from './pages/Services';
import Appointments from './pages/Appointments';
import AppointmentsAddForm from './pages/AppointmentsAddForm';
import AppointmentsEditForm from './pages/AppointmentsEditForm';
import AboutUs from './pages/AboutUs';
import Contacts from './pages/Contacts';
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
import MyAvailabilities from './pages/MyAvailabilities'; 
import EmployeeAvailabilitiesEditForm from './pages/EmployeeAvailabilitiesEditForm'; 
import AddAvailability from './pages/AddAvailability';
import Profile from "./pages/Profile";
import { useAuth0 } from "@auth0/auth0-react";


const App = () => {
  const { isLoading } = useAuth0();

  if (isLoading) {
    return <div>Loading...</div>;
  }
  return (
    <Router>
      <Layout>
        <Routes>
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
          {/* Employee routes */}
          <Route path="/employees" element={<EmployeesList />} />
          <Route path="/add-employee" element={<CreateEmployee />} />
          <Route path="/edit-employee/:employeeId" element={<EditEmployee />} />
          <Route path="/admin-dashboard" element={<AdminDashboard />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/schedule" element={<Schedule />} />
          <Route path="/add-service" element={<FormAddService />} />
          <Route
            path="/edit-service/:serviceId"
            element={<FormEditService />}
          />
          <Route path="/add-customer" element={<CreateCustomer />} />
          <Route path="/edit-customer/:customerId" element={<EditCustomer />} />
          <Route path="/submit-feedback" element={<SubmitFeedback />} />
          <Route path="/calendar-test" element={<CalendarTestPage />} />
          <Route path="/calendar-select" element={<SelectDateTimePage />} />
          <Route path="/checkout" element={<CheckoutPage />} />
          <Route path="/my-appointments" element={<MyAppointments />} />
          <Route
            path="/my-appointments/edit/:appointmentId"
            element={<CustomerAppointmentsEditForm />}
          />
          {/* Availabilities routes */}
          <Route path="/my-availabilities" element={<MyAvailabilities />} /> 
          <Route path="/my-availabilities/edit/:availabilityId" element={<EmployeeAvailabilitiesEditForm />} /> 
          <Route path="/add-availability" element={<AddAvailability />} />
        </Routes>
      </Layout>
    </Router>
  );
};

export default App;
