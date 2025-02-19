import { useState } from "react";
import EmployeesList from "../employees/EmployeesList";
import CustomersList from "../customers/CustomersList";
import Services from "../../pages/Services";
import AdminFeedbackList from "./AdminFeedbackList";
import Appointments from "../../pages/Appointments";
import EmployeeAvailabilities from "./EmployeeAvailabilities";
import EmployeeSchedule from "./EmployeeSchedule";
import UserRoleManager from "./UserRoleManager";

import { motion } from "framer-motion";
import {
  FaUserTie,
  FaUsers,
  FaConciergeBell,
  FaCommentDots,
  FaCalendarCheck,
  FaCalendarAlt,
  FaClock,
  FaUserShield, // new icon for user roles
} from "react-icons/fa";

const AdminDashboard = () => {
  const [activeTab, setActiveTab] = useState("employees");

  const renderContent = () => {
    switch (activeTab) {
      case "employees":
        return <EmployeesList />;
      case "customers":
        return <CustomersList />;
      case "services":
        return <Services />;
      case "feedbacks":
        return <AdminFeedbackList />;
      case "appointments":
        return <Appointments />;
      case "employeeAvailabilities":
        return <EmployeeAvailabilities />;
      case "employeeSchedule":
        return <EmployeeSchedule />;
      case "userRoles":
        return <UserRoleManager />;
      default:
        return null;
    }
  };

  return (
    <div className="admin-dashboard">
      <h1>Admin Dashboard</h1>
      <div className="tab-navigation">
        <button
          className={`tab-button ${activeTab === "employees" ? "active" : ""}`}
          onClick={() => setActiveTab("employees")}
        >
          <FaUserTie style={{ marginRight: "6px" }} /> Employees
        </button>
        <button
          className={`tab-button ${activeTab === "customers" ? "active" : ""}`}
          onClick={() => setActiveTab("customers")}
        >
          <FaUsers style={{ marginRight: "6px" }} /> Customers
        </button>
        <button
          className={`tab-button ${activeTab === "services" ? "active" : ""}`}
          onClick={() => setActiveTab("services")}
        >
          <FaConciergeBell style={{ marginRight: "6px" }} /> Services
        </button>
        <button
          className={`tab-button ${activeTab === "feedbacks" ? "active" : ""}`}
          onClick={() => setActiveTab("feedbacks")}
        >
          <FaCommentDots style={{ marginRight: "6px" }} /> Feedbacks
        </button>
        <button
          className={`tab-button ${activeTab === "appointments" ? "active" : ""}`}
          onClick={() => setActiveTab("appointments")}
        >
          <FaCalendarCheck style={{ marginRight: "6px" }} /> Appointments
        </button>
        <button
          className={`tab-button ${activeTab === "employeeAvailabilities" ? "active" : ""}`}
          onClick={() => setActiveTab("employeeAvailabilities")}
        >
          <FaClock style={{ marginRight: "6px" }} /> Employee Availabilities
        </button>
        <button
          className={`tab-button ${activeTab === "employeeSchedule" ? "active" : ""}`}
          onClick={() => setActiveTab("employeeSchedule")}
        >
          <FaCalendarAlt style={{ marginRight: "6px" }} /> Employee Schedule
        </button>
        <button
          className={`tab-button ${activeTab === "userRoles" ? "active" : ""}`}
          onClick={() => setActiveTab("userRoles")}
        >
          <FaUserShield style={{ marginRight: "6px" }} /> User Roles
        </button>
      </div>

      <motion.div
        key={activeTab}
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.4 }}
      >
        {renderContent()}
      </motion.div>
    </div>
  );
};

export default AdminDashboard;
