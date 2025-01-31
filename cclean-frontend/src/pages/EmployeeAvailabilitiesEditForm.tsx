import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import axiosInstance from '../api/axios';
import './EmployeeAvailabilitiesEditForm.css';

const EmployeeAvailabilitiesEditForm = () => {
  const [availability, setAvailability] = useState({
    employeeFirstName: '',
    employeeLastName: '',
    availableDate: '',
    shift: 'MORNING',
    comments: '',
  });

  const navigate = useNavigate();
  const { availabilityId } = useParams<{ availabilityId: string }>();

  useEffect(() => {
    const fetchAvailability = async () => {
      try {
        const response = await axiosInstance.get(`/availabilities/${availabilityId}`);
        setAvailability(response.data);
      } catch (err) {
        console.error(err);
        alert('Error loading availability.');
      }
    };
    fetchAvailability();
  }, [availabilityId]);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setAvailability((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await axiosInstance.put(`/availabilities/${availabilityId}`, availability);
      alert('Availability updated successfully.');
      navigate('/my-availabilities');
    } catch (err) {
      console.error(err);
      alert('Failed to update availability.');
    }
  };

  return (
    <div className="availabilities-edit-page">
      <div className="availabilities-edit-form">
        <h1>Edit My Availability</h1>
        <form onSubmit={handleSubmit}>
          <input
            type="text"
            name="employeeFirstName"
            placeholder="Employee First Name"
            value={availability.employeeFirstName}
            onChange={handleChange}
            required
          />
          <input
            type="text"
            name="employeeLastName"
            placeholder="Employee Last Name"
            value={availability.employeeLastName}
            onChange={handleChange}
            required
          />
          <input
            type="datetime-local"
            name="availableDate"
            placeholder="Available Date"
            value={availability.availableDate}
            onChange={handleChange}
            required
          />
          <select name="shift" value={availability.shift} onChange={handleChange} required>
            <option value="MORNING">Morning</option>
            <option value="EVENING">Evening</option>
            <option value="NIGHT">Night</option>
          </select>
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
