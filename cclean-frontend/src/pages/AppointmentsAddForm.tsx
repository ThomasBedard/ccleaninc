import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../api/axios';
import './AppointmentsAddForm.css';

const AppointmentsAddForm = () => {
  const [appointment, setAppointment] = useState({
    customerFirstName: '',
    customerLastName: '',
    appointmentDate: '',
    services: '',
    comments: '',
    status: 'pending', // Default to lowercase "pending" to match backend expectations
  });

  const navigate = useNavigate();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setAppointment((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      await axiosInstance.post('/appointments', appointment);
      window.alert('Appointment added successfully.');
      navigate('/appointments'); // Redirect to the Appointments page
    } catch (err) {
      alert(
        err instanceof Error
          ? err.message
          : 'An unexpected error occurred while adding the appointment.'
      );
    }
  };

  return (
    <div className="appointments-add-form">
      <h1>Add Appointment</h1>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="customerFirstName"
          placeholder="Customer First Name"
          value={appointment.customerFirstName}
          onChange={handleChange}
          required
        />
        <input
          type="text"
          name="customerLastName"
          placeholder="Customer Last Name"
          value={appointment.customerLastName}
          onChange={handleChange}
          required
        />
        <input
          type="datetime-local"
          name="appointmentDate"
          placeholder="Appointment Date"
          value={appointment.appointmentDate}
          onChange={handleChange}
          required
        />
        <input
          type="text"
          name="services"
          placeholder="Services"
          value={appointment.services}
          onChange={handleChange}
          required
        />
        <textarea
          name="comments"
          placeholder="Comments (Optional)"
          value={appointment.comments}
          onChange={handleChange}
        ></textarea>
        <select
          name="status"
          value={appointment.status}
          onChange={handleChange}
          required
        >
          <option value="pending">Pending</option>
          <option value="confirmed">Confirmed</option>
          <option value="cancelled">Cancelled</option>
          <option value="completed">Completed</option>
        </select>
        <button type="submit">Add Appointment</button>
      </form>
    </div>
  );
};

export default AppointmentsAddForm;
