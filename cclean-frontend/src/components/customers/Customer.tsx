import React from 'react';
import './Customers.css';
import { useNavigate } from 'react-router-dom';

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
    <div className="customer-card">
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
        <button
          className="delete-button"
          onClick={() => onDelete(customerId)}
        >
          Delete
        </button>
      </div>
    </div>
  );
};

export default Customer;
