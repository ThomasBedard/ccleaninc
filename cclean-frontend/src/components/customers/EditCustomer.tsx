import React, { useState, useEffect } from 'react';
import axiosInstance from '../../api/axios';
import { useParams, useNavigate } from 'react-router-dom';
import './Customers.css';
import { toast } from 'react-toastify';

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
        toast.error('Failed to fetch customer details.');
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
      toast.success('Customer updated successfully!');
      navigate('/admin-dashboard');
    } catch (error) {
      console.error('Error updating customer:', error);
      toast.error('Failed to update customer.');
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
        <label>First Name:</label>
        <input type="text" value={firstName} onChange={(e) => setFirstName(e.target.value)} required />
        <label>Last Name:</label>
        <input type="text" value={lastName} onChange={(e) => setLastName(e.target.value)} required />
        <label>Email:</label>
        <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
        <label>Phone Number:</label>
        <input type="tel" value={phoneNumber} onChange={(e) => setPhoneNumber(e.target.value)} required />
        <label>Company Name:</label>
        <input type="text" value={companyName} onChange={(e) => setCompanyName(e.target.value)} />
        <button type="submit" className="submit-button">Save Changes</button>
      </form>
    </div>
  );
};

export default EditCustomer;

