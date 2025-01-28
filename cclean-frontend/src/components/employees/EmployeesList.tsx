// EmployeesList.tsx
import React, { useEffect, useState } from "react";
import { useAuth0 } from "@auth0/auth0-react";
import axiosInstance from "../../api/axios";
import Employee from "./Employee";
import "./Employees.css";

// From feat/CCICC-78-UI_Overhaul
import { toast } from "react-toastify";
import { motion } from "framer-motion";

interface EmployeeData {
  employeeId: string;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  role: string;
  isActive: boolean;
}

const EmployeesList: React.FC = () => {
  const { isAuthenticated, isLoading, getAccessTokenSilently } = useAuth0();
  const [employees, setEmployees] = useState<EmployeeData[]>([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [filteredEmployees, setFilteredEmployees] = useState<EmployeeData[]>([]);

  useEffect(() => {
    const fetchEmployees = async () => {
      try {
        // If Auth0 hasn't finished loading, wait
        if (isLoading) {
          return;
        }

        // If not logged in, alert user
        if (!isAuthenticated) {
          return alert("You are not authenticated.");
        }

        // Fetch token
        const token = await getAccessTokenSilently();
        console.log("Token fetched:", token); // For debugging

        // Attach token to Axios instance
        axiosInstance.defaults.headers.common["Authorization"] = `Bearer ${token}`;

        // Fetch employees
        const response = await axiosInstance.get<EmployeeData[]>("/employees");
        setEmployees(response.data);
        setFilteredEmployees(response.data);
      } catch (error) {
        console.error("Error fetching employees:", error);
        // Show both toast and alert so we don't lose any method
        toast.error("Failed to fetch employees.");
        alert("Failed to fetch employees.");
      }
    };

    fetchEmployees();
  }, [isAuthenticated, isLoading, getAccessTokenSilently]);

  const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
    const term = e.target.value.toLowerCase();
    setSearchTerm(term);

    const filtered = employees.filter(
      (emp) =>
        `${emp.firstName} ${emp.lastName}`.toLowerCase().includes(term) ||
        emp.email.toLowerCase().includes(term) ||
        emp.phoneNumber.includes(term)
    );
    setFilteredEmployees(filtered);
  };

  const handleDelete = async (employeeId: string) => {
    if (!window.confirm("Are you sure you want to delete this employee?")) {
      return;
    }
    try {
      await axiosInstance.delete(`/employees/${employeeId}`);

      // Update local state
      setEmployees((prev) => prev.filter((emp) => emp.employeeId !== employeeId));
      setFilteredEmployees((prev) =>
        prev.filter((emp) => emp.employeeId !== employeeId)
      );

      // Show both toast and alert
      toast.success("Employee deleted successfully.");
      alert("Employee deleted successfully.");
    } catch (error) {
      console.error("Error deleting employee:", error);
      toast.error("Failed to delete employee.");
      alert("Failed to delete employee.");
    }
  };

  return (
    <div className="employees-container">
      <h1>Employees</h1>

      <div className="search-bar">
        <input
          type="text"
          placeholder="Search employees by name, email, or phone..."
          value={searchTerm}
          onChange={handleSearch}
        />
      </div>

      <button
        className="add-button"
        onClick={() => (window.location.href = "/add-employee")}
      >
        Add Employee
      </button>

      {/* Fade in using Framer Motion */}
      <motion.div
        className="employees-list"
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ duration: 0.8 }}
      >
        {filteredEmployees.length > 0 ? (
          filteredEmployees.map((emp) => (
            <Employee
              key={emp.employeeId}
              employeeId={emp.employeeId}
              firstName={emp.firstName}
              lastName={emp.lastName}
              email={emp.email}
              phoneNumber={emp.phoneNumber}
              role={emp.role}
              isActive={emp.isActive}
              onDelete={handleDelete}
            />
          ))
        ) : (
          <p>No employees found.</p>
        )}
      </motion.div>
    </div>
  );
};

export default EmployeesList;
