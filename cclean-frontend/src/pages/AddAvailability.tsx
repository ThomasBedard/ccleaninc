import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth0 } from '@auth0/auth0-react';
import { useAxiosWithAuth } from '../api/axios';  // <-- Use the custom hook here
import './AddAvailability.css';

const shifts = ['Morning', 'Evening', 'Night'];

const AddAvailability = () => {
  const { user } = useAuth0();
  const axiosWithAuth = useAxiosWithAuth(); // <-- Interceptor-based instance
  const navigate = useNavigate();

  const [availability, setAvailability] = useState({
    availableDate: '',
    shift: '',
    comments: '',
    employeeFirstName: '',
    employeeLastName: '',
  });

  // Prefill employee details from the Auth0 user object (optional, for display)
  useEffect(() => {
    if (user) {
      setAvailability((prev) => ({
        ...prev,
        employeeFirstName: user.given_name || '',
        employeeLastName: user.family_name || '',
      }));
    }
  }, [user]);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setAvailability((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleDateChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const selectedDate = e.target.value;
    const dateWithTime = `${selectedDate}T00:00`;
    setAvailability((prevState) => ({
      ...prevState,
      availableDate: dateWithTime,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!availability.availableDate || !availability.shift) {
      alert('Please fill in all required fields.');
      return;
    }

    try {
      // Use the JWT-based endpoint that derives employee details from the token
      await axiosWithAuth.post('/availabilities/my-availabilities', availability);
      alert('Availability added successfully!');
      navigate('/my-availabilities');
    } catch (err) {
      console.error('Failed to add availability:', err);
      alert(
        err instanceof Error
          ? err.message
          : 'An unexpected error occurred while adding the availability.'
      );
    }
  };

  return (
    <div className="add-availability-container">
      <h1 className="add-availability-title">Add Availability</h1>
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
          type="date"
          name="availableDate"
          id="availableDate"
          onChange={handleDateChange}
          className="add-availability-input"
          required
        />

        <label htmlFor="shift">Select Shift:</label>
        <select
          name="shift"
          id="shift"
          value={availability.shift}
          onChange={handleChange}
          className="add-availability-dropdown"
          required
        >
          <option value="">Select Shift</option>
          {shifts.map((shift) => (
            <option key={shift} value={shift.toUpperCase()}>
              {shift}
            </option>
          ))}
        </select>

        <label htmlFor="comments">Comments (Optional):</label>
        <textarea
          name="comments"
          id="comments"
          placeholder="Add any additional details..."
          value={availability.comments}
          onChange={handleChange}
          className="add-availability-textarea"
        ></textarea>

        <button type="submit" className="add-availability-button">
          Add Availability
        </button>
      </form>
    </div>
  );
};

export default AddAvailability;
