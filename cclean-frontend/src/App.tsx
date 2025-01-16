import { Routes, Route, BrowserRouter } from "react-router-dom";
import { PageLayout } from "./components/page-layout";
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
import Profile from "./components/Profile";

const App = () => {
  return (
    <BrowserRouter>
      <PageLayout>
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
        </Routes>
      </PageLayout>
    </BrowserRouter>
  );
};

export default App;
