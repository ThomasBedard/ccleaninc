import React, { useState, useEffect } from 'react';
import axiosInstance from '../../api/axios';
import './Customers.css';
import { toast } from 'react-toastify';

interface Customer {
  customerId: string;
  firstName: string | null;
  lastName: string | null;
  email: string | null;
  phoneNumber: string | null;
  companyName?: string | null;
}

const CustomersList: React.FC = () => {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [filteredCustomers, setFilteredCustomers] = useState<Customer[]>([]);

  useEffect(() => {
    const fetchCustomers = async () => {
      try {
        const response = await axiosInstance.get<Customer[]>('/customers');
        setCustomers(response.data);
        setFilteredCustomers(response.data);
      } catch (error) {
        console.error('Error fetching customers:', error);
        toast.error('Failed to fetch customers.');
      }
    };

    fetchCustomers();
  }, []);

  const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
    const term = e.target.value.toLowerCase();
    setSearchTerm(term);

    const filtered = customers.filter((customer) =>
      `${customer.firstName ?? ''} ${customer.lastName ?? ''}`
        .toLowerCase()
        .includes(term) ||
      (customer.email ?? '').toLowerCase().includes(term) ||
      (customer.phoneNumber ?? '').includes(term)
    );

    setFilteredCustomers(filtered);
  };

  const handleDeleteCustomer = async (customerId: string) => {
    if (!window.confirm('Are you sure you want to delete this customer?')) {
      return;
    }

    try {
      await axiosInstance.delete(`/customers/${customerId}`);
      setCustomers((prev) => prev.filter((customer) => customer.customerId !== customerId));
      setFilteredCustomers((prev) => prev.filter((customer) => customer.customerId !== customerId));
      toast.success('Customer deleted successfully.');
    } catch (error) {
      console.error('Error deleting customer:', error);
      toast.error('Failed to delete customer.');
    }
  };

  return (
    <div className="customers-container">
      <h1>Customers</h1>
      <div className="search-bar">
        <input
          type="text"
          placeholder="Search customers by name, email, or phone..."
          value={searchTerm}
          onChange={handleSearch}
        />
      </div>
      <button
        className="add-button"
        onClick={() => (window.location.href = '/add-customer')}
      >
        Add Customer
      </button>
      <div className="customers-list">
        {filteredCustomers.length > 0 ? (
          filteredCustomers.map((customer) => (
            <div key={customer.customerId} className="customer-card">
              {customer.firstName && customer.lastName && (
                <p>
                  {customer.firstName} {customer.lastName}
                </p>
              )}
              {customer.email && <p>Email: {customer.email}</p>}
              {customer.phoneNumber && <p>Phone: {customer.phoneNumber}</p>}
              {customer.companyName && <p>Company: {customer.companyName}</p>}
              <button
                className="edit-button"
                onClick={() => (window.location.href = `/edit-customer/${customer.customerId}`)}
              >
                Edit
              </button>
              <button
                className="delete-button"
                onClick={() => handleDeleteCustomer(customer.customerId)}
              >
                Delete
              </button>
            </div>
          ))
        ) : (
          <p>No customers found.</p>
        )}
      </div>
    </div>
  );
};

export default CustomersList;
