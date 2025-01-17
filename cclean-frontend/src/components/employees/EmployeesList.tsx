import React, { useEffect, useState } from 'react';
import axiosInstance from '../../api/axios';
import Employee from './Employee';       
import './Employees.css';

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

  // Fetch employees on mount
  useEffect(() => {
    const fetchEmployees = async () => {
      try {
        const response = await axiosInstance.get<EmployeeData[]>('/employees');
        setEmployees(response.data);
        setFilteredEmployees(response.data);
      } catch (error) {
        console.error('Error fetching employees:', error);
        alert('Failed to fetch employees.');
      }
    };

    fetchEmployees();
  }, []);

  // Handle search input
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

  // Handle deleting an employee
  const handleDelete = async (employeeId: string) => {
    if (!window.confirm('Are you sure you want to delete this employee?')) {
      return;
    }
    try {
      await axiosInstance.delete(`/employees/${employeeId}`);
      // Update the local list
      setEmployees((prev) => prev.filter((emp) => emp.employeeId !== employeeId));
      setFilteredEmployees((prev) => prev.filter((emp) => emp.employeeId !== employeeId));
      alert('Employee deleted successfully.');
    } catch (error) {
      console.error('Error deleting employee:', error);
      alert('Failed to delete employee.');
    }
  };

  return (
    <div className="employees-container">
      <h1>Employees</h1>

      {/* Search bar */}
      <div className="search-bar">
        <input
          type="text"
          placeholder="Search employees by name, email, or phone..."
          value={searchTerm}
          onChange={handleSearch}
        />
      </div>

      {/* Button to add a new employee */}
      <button
        className="add-button"
        onClick={() => (window.location.href = '/add-employee')}
      >
        Add Employee
      </button>

      {/* Render the list of employees */}
      <div className="employees-list">
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
      </div>
    </div>
  );
};

export default EmployeesList;
