import React, { useState } from "react";
import "./AdminDashboard.css";
import EmployeesList from "../employees/EmployeesList";
import CustomersList from "../customers/CustomersList";
import Services from "../../pages/Services";
import AdminFeedbackList from "./AdminFeedbackList";
import Appointments from "../../pages/Appointments";

// Added from feat/CCICC-78-UI_Overhaul
import { motion } from "framer-motion";
import {
  FaUserTie,
  FaUsers,
  FaConciergeBell,
  FaCommentDots,
  FaCalendarCheck,
} from "react-icons/fa";

const AdminDashboard: React.FC = () => {
  const [activeTab, setActiveTab] = useState<string>("employees");

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
          <FaUserTie style={{ marginRight: "6px" }} />
          Employees
        </button>
        <button
          className={`tab-button ${activeTab === "customers" ? "active" : ""}`}
          onClick={() => setActiveTab("customers")}
        >
          <FaUsers style={{ marginRight: "6px" }} />
          Customers
        </button>
        <button
          className={`tab-button ${activeTab === "services" ? "active" : ""}`}
          onClick={() => setActiveTab("services")}
        >
          <FaConciergeBell style={{ marginRight: "6px" }} />
          Services
        </button>
        <button
          className={`tab-button ${activeTab === "feedbacks" ? "active" : ""}`}
          onClick={() => setActiveTab("feedbacks")}
        >
          <FaCommentDots style={{ marginRight: "6px" }} />
          Feedbacks
        </button>
        <button
          className={`tab-button ${
            activeTab === "appointments" ? "active" : ""
          }`}
          onClick={() => setActiveTab("appointments")}
        >
          <FaCalendarCheck style={{ marginRight: "6px" }} />
          Appointments
        </button>
      </div>

      {/* Simple fade-in animation when switching tabs */}
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
