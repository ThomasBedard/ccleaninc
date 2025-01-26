import React, { useState } from 'react';
import axiosInstance from '../api/axios';
import { useNavigate } from 'react-router-dom';
import './MyAvailabilities.css';

interface Availability {
  availabilityId: string;
  employeeId: string;
  employeeFirstName: string;
  employeeLastName: string;
  availableDate: string;
  shift: string;
  comments?: string;
}

const MyAvailabilities: React.FC = () => {
  const [employeeIdInput, setEmployeeIdInput] = useState('');
  const [availabilities, setAvailabilities] = useState<Availability[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [hasSearched, setHasSearched] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setAvailabilities([]);
    setHasSearched(true);

    if (!employeeIdInput.trim()) {
      setError('Employee ID is required');
      return;
    }

    try {
      const response = await axiosInstance.get<Availability[]>(
        `/availabilities/employee/${employeeIdInput.trim()}`
      );
      setAvailabilities(response.data);
    } catch (err) {
      if (err instanceof Error) {
        setError(err.message);
      } else {
        setError('Failed to load availabilities. Please try again.');
      }
    }
  };

  const handleDelete = async (availabilityId: string) => {
    const confirm = window.confirm('Are you sure you want to delete this availability?');
    if (!confirm) return;

    try {
      await axiosInstance.delete(`/availabilities/${availabilityId}`);
      setAvailabilities(availabilities.filter((avail) => avail.availabilityId !== availabilityId));
      alert('Availability deleted successfully.');
    } catch (err) {
      console.error('Error deleting availability:', err);
      alert('Failed to delete availability.');
    }
  };

  const handleEdit = (availabilityId: string) => {
    navigate(`/my-availabilities/edit/${availabilityId}`);
  };

  return (
    <div className="my-availabilities-container">
      <h1 className="my-availabilities-title">My Availabilities</h1>

      <form onSubmit={handleSubmit} className="my-availabilities-form">
        <label>Enter Employee ID: </label>
        <input
          type="text"
          value={employeeIdInput}
          onChange={(e) => setEmployeeIdInput(e.target.value)}
        />
        <button type="submit">View Availabilities</button>
      </form>

      {error && <p className="my-availabilities-error">{error}</p>}

      {hasSearched && !error && availabilities.length === 0 && (
        <p className="my-availabilities-no-results">No availabilities found for that employee ID.</p>
      )}

      {availabilities.length > 0 && (
        <table className="my-availabilities-table">
          <thead>
            <tr>
              <th>Availability ID</th>
              <th>Employee Name</th>
              <th>Date/Time</th>
              <th>Shift</th>
              <th>Comments</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {availabilities.map((avail) => (
              <tr key={avail.availabilityId}>
                <td>{avail.availabilityId}</td>
                <td>{`${avail.employeeFirstName} ${avail.employeeLastName}`}</td>
                <td>{avail.availableDate}</td>
                <td>{avail.shift}</td>
                <td>{avail.comments || 'N/A'}</td>
                <td>
                  <button onClick={() => handleDelete(avail.availabilityId)}>Delete</button>
                  <button onClick={() => handleEdit(avail.availabilityId)}>Edit</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default MyAvailabilities;
