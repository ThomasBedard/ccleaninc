import React from 'react';
import './Customers.css';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';

interface CustomerProps {
  customerId: string;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  companyName?: string;
  onDelete: (customerId: string) => void;
}

const Customer: React.FC<CustomerProps> = ({
  customerId,
  firstName,
  lastName,
  email,
  phoneNumber,
  companyName,
  onDelete,
}) => {
  const navigate = useNavigate();

  return (
    <motion.div
  className="customer-card"
  whileHover={{ scale: 1.05, boxShadow: '0 8px 16px rgba(0, 0, 0, 0.15)' }}
  transition={{ duration: 0.3 }}
>
  <p>
    <strong>Name:</strong> {firstName} {lastName}
  </p>
  <p>
    <strong>Email:</strong> {email}
  </p>
  <p>
    <strong>Phone:</strong> {phoneNumber}
  </p>
  {companyName && (
    <p>
      <strong>Company:</strong> {companyName}
    </p>
  )}
  <div className="actions">
    <button
      className="edit-button"
      onClick={() => navigate(`/edit-customer/${customerId}`)}
    >
      Edit
    </button>
    <button className="delete-button" onClick={() => onDelete(customerId)}>
      Delete
    </button>
  </div>
</motion.div>
  );
};

export default Customer;
