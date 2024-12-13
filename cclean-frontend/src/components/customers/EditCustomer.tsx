import React, { useState, useEffect } from 'react';
import axiosInstance from '../../api/axios';
import { useParams, useNavigate } from 'react-router-dom';
import './Customers.css';

const EditCustomer: React.FC = () => {
  const { customerId } = useParams<{ customerId: string }>();
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [companyName, setCompanyName] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchCustomer = async () => {
      try {
        const response = await axiosInstance.get(`/customers/${customerId}`);
        const customer = response.data;
        setFirstName(customer.firstName);
        setLastName(customer.lastName);
        setEmail(customer.email);
        setPhoneNumber(customer.phoneNumber);
        setCompanyName(customer.companyName || '');
      } catch (error) {
        console.error('Error fetching customer details:', error);
        alert('Failed to fetch customer details.');
      }
    };

    fetchCustomer();
  }, [customerId]);

  const handleEditCustomer = async () => {
    try {
      await axiosInstance.put(`/customers/${customerId}`, {
        firstName,
        lastName,
        email,
        phoneNumber,
        companyName,
      });
      alert('Customer updated successfully!');
      navigate('/admin-dashboard');
    } catch (error) {
      console.error('Error updating customer:', error);
      alert('Failed to update customer.');
    }
  };

  return (
    <div className="form-container">
      <h1>Edit Customer</h1>
      <form
        onSubmit={(e) => {
          e.preventDefault();
          handleEditCustomer();
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
        <button type="submit" className="submit-button">Save Changes</button>
      </form>
    </div>
  );
};

export default EditCustomer;
