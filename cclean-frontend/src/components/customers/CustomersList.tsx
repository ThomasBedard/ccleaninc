import React, { useEffect, useState } from 'react';
import axiosInstance from '../../api/axios';
import './Customers.css';

interface Customer {
  customerId: string;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  companyName?: string;
}

const CustomersList: React.FC = () => {
  const [customers, setCustomers] = useState<Customer[]>([]);

  const fetchCustomers = async () => {
    try {
      const response = await axiosInstance.get<Customer[]>('/customers');
      setCustomers(response.data);
    } catch (error) {
      console.error('Error fetching customers:', error);
      alert('Failed to fetch customers.');
    }
  };

  const handleDeleteCustomer = async (customerId: string) => {
    if (!window.confirm('Are you sure you want to delete this customer?')) return;

    try {
      await axiosInstance.delete(`/customers/${customerId}`);
      setCustomers((prev) => prev.filter((customer) => customer.customerId !== customerId));
      alert('Customer deleted successfully.');
    } catch (error) {
      console.error('Error deleting customer:', error);
      alert('Failed to delete customer.');
    }
  };

  useEffect(() => {
    fetchCustomers();
  }, []);

  return (
    <div className="customers-container">
      <h1>Customers</h1>
      <button className="add-button" onClick={() => (window.location.href = '/add-customer')}>
        Add Customer
      </button>
      <div className="customers-list">
        {customers.map((customer) => (
          <div key={customer.customerId}>
            <p>{customer.firstName} {customer.lastName}</p>
            <p>Email: {customer.email}</p>
            <p>Phone: {customer.phoneNumber}</p>
            {customer.companyName && <p>Company: {customer.companyName}</p>}
            <button onClick={() => handleDeleteCustomer(customer.customerId)}>Delete</button>
            <button onClick={() => (window.location.href = `/edit-customer/${customer.customerId}`)}>Edit</button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default CustomersList;
