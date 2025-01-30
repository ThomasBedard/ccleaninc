// Employee.tsx
import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Employees.css';
import { motion } from 'framer-motion';
import { FaTrash, FaEdit } from 'react-icons/fa';

interface EmployeeProps {
  employeeId: string;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  role: string;
  isActive: boolean;
  onDelete: (employeeId: string) => void;
}

const Employee: React.FC<EmployeeProps> = ({
  employeeId,
  firstName,
  lastName,
  email,
  phoneNumber,
  role,
  isActive,
  onDelete,
}) => {
  const navigate = useNavigate();

  const handleDeleteClick = () => {
    onDelete(employeeId);
  };

  return (
    <motion.div
      className="employee-card"
      whileHover={{ scale: 1.03, boxShadow: '0 6px 12px rgba(0,0,0,0.15)' }}
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
      <p>
        <strong>Role:</strong> {role}
      </p>
      <p>
        <strong>Status:</strong> {isActive ? 'Active' : 'Inactive'}
      </p>

      <div className="actions">
        <button
          className="edit-button"
          onClick={() => navigate(`/edit-employee/${employeeId}`)}
        >
          <FaEdit style={{ marginRight: '5px' }} />
          Edit
        </button>
        <button
          className="delete-button"
          onClick={handleDeleteClick}
        >
          <FaTrash style={{ marginRight: '5px' }} />
          Delete
        </button>
      </div>
    </motion.div>
  );
};

export default Employee;
