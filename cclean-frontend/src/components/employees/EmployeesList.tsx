// EmployeesList.tsx
import React, { useEffect, useState } from 'react';
import axiosInstance from '../../api/axios';
import Employee from './Employee';
import './Employees.css';
import { toast } from 'react-toastify';
import { motion } from 'framer-motion';

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
  const [employees, setEmployees] = useState<EmployeeData[]>([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [filteredEmployees, setFilteredEmployees] = useState<EmployeeData[]>([]);

  useEffect(() => {
    const fetchEmployees = async () => {
      try {
        const response = await axiosInstance.get<EmployeeData[]>('/employees');
        setEmployees(response.data);
        setFilteredEmployees(response.data);
      } catch (error) {
        console.error('Error fetching employees:', error);
        toast.error('Failed to fetch employees.');
      }
    };

    fetchEmployees();
  }, []);

  const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
    const term = e.target.value.toLowerCase();
    setSearchTerm(term);

    const filtered = employees.filter((emp) =>
      `${emp.firstName} ${emp.lastName}`.toLowerCase().includes(term) ||
      emp.email.toLowerCase().includes(term) ||
      emp.phoneNumber.includes(term)
    );

    setFilteredEmployees(filtered);
  };

  const handleDelete = async (employeeId: string) => {
    if (!window.confirm('Are you sure you want to delete this employee?')) {
      return;
    }
    try {
      await axiosInstance.delete(`/employees/${employeeId}`);
      setEmployees((prev) => prev.filter((emp) => emp.employeeId !== employeeId));
      setFilteredEmployees((prev) => prev.filter((emp) => emp.employeeId !== employeeId));
      toast.success('Employee deleted successfully.');
    } catch (error) {
      console.error('Error deleting employee:', error);
      toast.error('Failed to delete employee.');
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
        onClick={() => (window.location.href = '/add-employee')}
      >
        Add Employee
      </button>

      {/* Fade in the employees list using Framer Motion */}
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
