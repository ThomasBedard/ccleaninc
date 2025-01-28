import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import axiosInstance from '../api/axios';
import './AppointmentsEditForm.css'; 
import { toast } from 'react-toastify';

const CustomerAppointmentsEditForm = () => {
  const [appointment, setAppointment] = useState({
    customerFirstName: '',
    customerLastName: '',
    appointmentDate: '',
    services: '',
    comments: '',
    status: 'pending',
  });

  const navigate = useNavigate();
  const { appointmentId } = useParams<{ appointmentId: string }>();

  useEffect(() => {
    const fetchAppointment = async () => {
      try {
        const response = await axiosInstance.get(`/appointments/${appointmentId}`);
        setAppointment(response.data);
      } catch (err) {
        console.error(err); // <-- Using the error here
        toast.error('Error loading appointment.');
      }
    };
    fetchAppointment();
  }, [appointmentId]);
  

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setAppointment((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await axiosInstance.put(`/appointments/customer/${appointmentId}`, appointment);
      toast.success('Appointment updated successfully (Customer).');
      navigate('/my-appointments');
    } catch (err) {
      console.error(err); // <-- Using the error here
      toast.error('Failed to update appointment for customer.');
    }
  };

  return (
    <div className="appointments-edit-page">
      <div className="appointments-edit-form">
        <h1>Edit My Appointment</h1>
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
          />
          <select name="status" value={appointment.status} onChange={handleChange} required>
            <option value="pending">pending</option>
            <option value="confirmed">confirmed</option>
            <option value="cancelled">cancelled</option>
            <option value="completed">completed</option>
          </select>
          <button type="submit">Update Appointment</button>
        </form>
      </div>
    </div>
  );
};

export default CustomerAppointmentsEditForm;
