import React, { useState } from 'react';
import axiosInstance from '../../api/axios';
import { useNavigate } from 'react-router-dom';
import './Employees.css';
import { toast } from 'react-toastify';

const CreateEmployee: React.FC = () => {
  const navigate = useNavigate();

  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [role, setRole] = useState('');
  const [isActive, setIsActive] = useState(true);

  const handleCreateEmployee = async () => {
    try {
      await axiosInstance.post('/employees', {
        firstName,
        lastName,
        email,
        phoneNumber,
        role,
        isActive,
      });
      toast.success('Employee created successfully!');
    navigate('/admin-dashboard');
  } catch (error) {
    console.error('Error creating employee:', error);
    toast.error('Failed to create employee.');
  }
  };

  return (
    <div className="form-container">
      <h1>Create New Employee</h1>
      <form
        onSubmit={(e) => {
          e.preventDefault();
          handleCreateEmployee();
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

        <button type="submit" className="submit-button">Create</button>
      </form>
    </div>
  );
};

export default CreateEmployee;
