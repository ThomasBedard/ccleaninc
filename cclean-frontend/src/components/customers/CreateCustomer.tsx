import React, { useState } from 'react';
import axiosInstance from '../../api/axios';
import { useNavigate } from 'react-router-dom';
import './Customers.css';

const CreateCustomer: React.FC = () => {
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [companyName, setCompanyName] = useState('');
  const navigate = useNavigate();

  const handleCreateCustomer = async () => {
    try {
      await axiosInstance.post('/customers', {
        firstName,
        lastName,
        email,
        phoneNumber,
        companyName,
      });
      alert('Customer created successfully!');
      navigate('/admin-dashboard');
    } catch (error) {
      console.error('Error creating customer:', error);
      alert('Failed to create customer.');
    }
  };

  return (
    <div className="form-container">
      <h1>Create New Customer</h1>
      <form
        onSubmit={(e) => {
          e.preventDefault();
          handleCreateCustomer();
        }}
      >
        <label>
          First Name:
          <input type="text" value={firstName} onChange={(e) => setFirstName(e.target.value)} required />
        </label>
        <label>
          Last Name:
          <input type="text" value={lastName} onChange={(e) => setLastName(e.target.value)} required />
        </label>
        <label>
          Email:
          <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
        </label>
        <label>
          Phone Number:
          <input type="tel" value={phoneNumber} onChange={(e) => setPhoneNumber(e.target.value)} required />
        </label>
        <label>
          Company Name:
          <input type="text" value={companyName} onChange={(e) => setCompanyName(e.target.value)} />
        </label>
        <button type="submit" className="submit-button">Submit</button>
      </form>
    </div>
  );
};

export default CreateCustomer;
