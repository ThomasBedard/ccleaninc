import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Employees.css';

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
    <div className="employee-card">
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
          Edit
        </button>
        <button
          className="delete-button"
          onClick={handleDeleteClick}
        >
          Delete
        </button>
      </div>
    </div>
  );
};

export default Employee;
