import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../api/axios';
import './AddAvailability.css';

const shifts = ['Morning', 'Evening', 'Night'];

const AddAvailability = () => {
  const [availability, setAvailability] = useState({
    employeeId: '',
    employeeFirstName: '',
    employeeLastName: '',
    availableDate: '',
    shift: '',
    comments: '',
  });

  const navigate = useNavigate();

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
    const dateWithTime = `${selectedDate}T00:00`; // Append default time
    setAvailability((prevState) => ({
      ...prevState,
      availableDate: dateWithTime,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!availability.employeeId || !availability.employeeFirstName || !availability.employeeLastName) {
      alert('Please fill in all required fields.');
      return;
    }

    try {
      await axiosInstance.post('/availabilities', availability);
      alert('Availability added successfully!');
      navigate('/my-availabilities'); // Redirect to "My Availabilities" page
    } catch (err) {
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
        <label htmlFor="employeeId">Employee ID:</label>
        <input
          type="text"
          name="employeeId"
          id="employeeId"
          placeholder="Enter employee ID"
          value={availability.employeeId}
          onChange={handleChange}
          className="add-availability-input"
          required
        />

        <label htmlFor="employeeFirstName">Employee First Name:</label>
        <input
          type="text"
          name="employeeFirstName"
          id="employeeFirstName"
          placeholder="Enter first name"
          value={availability.employeeFirstName}
          onChange={handleChange}
          className="add-availability-input"
          required
        />

        <label htmlFor="employeeLastName">Employee Last Name:</label>
        <input
          type="text"
          name="employeeLastName"
          id="employeeLastName"
          placeholder="Enter last name"
          value={availability.employeeLastName}
          onChange={handleChange}
          className="add-availability-input"
          required
        />

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
