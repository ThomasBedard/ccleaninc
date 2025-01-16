import React, { useState, useEffect } from 'react';
import axiosInstance from '../../api/axios';
import { useParams, useNavigate } from 'react-router-dom';
import './Employees.css';

const EditEmployee: React.FC = () => {
  const { employeeId } = useParams<{ employeeId: string }>();
  const navigate = useNavigate();

  const [firstName, setFirstName]     = useState('');
  const [lastName, setLastName]       = useState('');
  const [email, setEmail]             = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [role, setRole]               = useState('');
  const [isActive, setIsActive]       = useState(true);

  useEffect(() => {
    const fetchEmployee = async () => {
      try {
        const response = await axiosInstance.get(`/employees/${employeeId}`);
        const emp = response.data;
        setFirstName(emp.firstName);
        setLastName(emp.lastName);
        setEmail(emp.email);
        setPhoneNumber(emp.phoneNumber);
        setRole(emp.role);
        setIsActive(emp.isActive);
      } catch (error) {
        console.error('Error fetching employee details:', error);
        alert('Failed to fetch employee details.');
      }
    };

    if (employeeId) {
      fetchEmployee();
    }
  }, [employeeId]);

  const handleEditEmployee = async () => {
    try {
      await axiosInstance.put(`/employees/${employeeId}`, {
        firstName,
        lastName,
        email,
        phoneNumber,
        role,
        isActive,
      });
      alert('Employee updated successfully!');
      navigate('/admin-dashboard');
    } catch (error) {
      console.error('Error updating employee:', error);
      alert('Failed to update employee.');
    }
  };

  return (
    <div className="form-container">
      <h1>Edit Employee</h1>
      <form
        onSubmit={(e) => {
          e.preventDefault();
          handleEditEmployee();
        }}
      >
        <label>First Name:</label>
        <input
          type="text"
          value={firstName}
          onChange={(e) => setFirstName(e.target.value)}
          required
        />

        <label>Last Name:</label>
        <input
          type="text"
          value={lastName}
          onChange={(e) => setLastName(e.target.value)}
          required
        />

        <label>Email:</label>
        <input
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />

        <label>Phone Number:</label>
        <input
          type="tel"
          value={phoneNumber}
          onChange={(e) => setPhoneNumber(e.target.value)}
          required
        />

        <label>Role:</label>
        <input
          type="text"
          value={role}
          onChange={(e) => setRole(e.target.value)}
          required
        />

        <label>Active:</label>
        <input
          type="checkbox"
          checked={isActive}
          onChange={(e) => setIsActive(e.target.checked)}
        />

        <button type="submit" className="submit-button">Save Changes</button>
      </form>
    </div>
  );
};

export default EditEmployee;
