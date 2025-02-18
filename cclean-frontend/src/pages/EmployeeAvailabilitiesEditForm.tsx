import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useAxiosWithAuth } from '../api/axios'; // <-- Use the custom hook
import './EmployeeAvailabilitiesEditForm.css';

const EmployeeAvailabilitiesEditForm = () => {
  const axiosWithAuth = useAxiosWithAuth(); // <-- Interceptor-based instance
  const navigate = useNavigate();
  const { availabilityId } = useParams<{ availabilityId: string }>();

  const [availability, setAvailability] = useState({
    employeeFirstName: '',
    employeeLastName: '',
    availableDate: '',
    shift: 'MORNING',
    comments: '',
  });

  // Fetch existing availability
  useEffect(() => {
    const fetchAvailability = async () => {
      try {
        // For fetching an existing record, your controller might or might not require a JWT
        // If you do, then this is the correct approach:
        const response = await axiosWithAuth.get(`/availabilities/${availabilityId}`);
        setAvailability(response.data);
      } catch (err) {
        console.error('Error loading availability:', err);
        alert('Error loading availability.');
      }
    };
    if (availabilityId) fetchAvailability();
  }, [availabilityId, axiosWithAuth]);

  // Submit updated availability
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      // Use the JWT-based update endpoint
      await axiosWithAuth.put(`/availabilities/my-availabilities/${availabilityId}`, availability);
      alert('Availability updated successfully.');
      navigate('/my-availabilities');
    } catch (err) {
      console.error('Failed to update availability:', err);
      alert('Failed to update availability.');
    }
  };

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setAvailability((prev) => ({ ...prev, [name]: value }));
  };

  return (
    <div className="availabilities-edit-page">
      <div className="availabilities-edit-form">
        <h1>Edit My Availability</h1>
        <form onSubmit={handleSubmit}>
          {/* Display employee name as read-only */}
          <div className="read-only-field">
            <label>Employee Name:</label>
            <span>
              {availability.employeeFirstName} {availability.employeeLastName}
            </span>
          </div>

          <label htmlFor="availableDate">Available Date:</label>
          <input
            type="datetime-local"
            name="availableDate"
            placeholder="Available Date"
            value={availability.availableDate}
            onChange={handleChange}
            required
          />

          <label htmlFor="shift">Select Shift:</label>
          <select name="shift" value={availability.shift} onChange={handleChange} required>
            <option value="MORNING">Morning</option>
            <option value="EVENING">Evening</option>
            <option value="NIGHT">Night</option>
          </select>

          <label htmlFor="comments">Comments (Optional):</label>
          <textarea
            name="comments"
            placeholder="Comments (Optional)"
            value={availability.comments}
            onChange={handleChange}
          />

          <button type="submit">Update Availability</button>
        </form>
      </div>
    </div>
  );
};

export default EmployeeAvailabilitiesEditForm;
