import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../api/axios';
import './Appointments.css';

interface Appointment {
  appointmentId?: string;
  customerId?: string;
  customerFirstName?: string;
  customerLastName?: string;
  appointmentDate?: string;
  services?: string;
  status?: string;
  comments?: string;
}

const Appointments = () => {
  const [appointments, setAppointments] = useState<Appointment[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  const fetchAllAppointments = async () => {
    try {
      setLoading(true);
      setError(null);
      const response = await axiosInstance.get<Appointment[]>('/appointments');
      setAppointments(response.data);
    } catch (err) {
      setError(
        err instanceof Error
          ? err.message
          : 'An unexpected error occurred while fetching appointments.'
      );
    } finally {
      setLoading(false);
    }
  };

  const deleteAppointment = async (appointmentId: string | undefined) => {
    if (!appointmentId) {
      alert('Invalid appointment ID');
      return;
    }

    const confirmDelete = window.confirm(
      `Are you sure you want to delete the appointment with ID: ${appointmentId}?`
    );

    if (!confirmDelete) return;

    try {
      await axiosInstance.delete(`/appointments/${appointmentId}`);
      setAppointments((prevAppointments) =>
        prevAppointments.filter((appointment) => appointment.appointmentId !== appointmentId)
      );
      window.alert('Appointment deleted successfully.');
    } catch (err) {
      alert(
        err instanceof Error
          ? err.message
          : 'An unexpected error occurred while deleting the appointment.'
      );
    }
  };

  useEffect(() => {
    fetchAllAppointments();
  }, []);

  if (loading) {
    return <div className="loading-message">Loading appointments...</div>;
  }

  if (error) {
    return <div className="error-message">Error: {error}</div>;
  }

  return (
    <div className="appointments-page">
      <h1 className="appointments-title">Appointments Page</h1>
      <button
        className="add-appointment-button"
        onClick={() => navigate('/appointments/add')}
      >
        Add Appointment
      </button>
      <table className="appointments-table">
        <thead>
          <tr>
            <th>Appointment ID</th>
            <th>Customer ID</th>
            <th>Customer First Name</th>
            <th>Customer Last Name</th>
            <th>Date</th>
            <th>Services</th>
            <th>Status</th>
            <th>Comments</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {appointments.map((appointment, index) => (
            <tr key={appointment.appointmentId || index}>
              <td>{appointment.appointmentId || 'N/A'}</td>
              <td>{appointment.customerId || 'N/A'}</td>
              <td>{appointment.customerFirstName || 'N/A'}</td>
              <td>{appointment.customerLastName || 'N/A'}</td>
              <td>{appointment.appointmentDate || 'N/A'}</td>
              <td>{appointment.services || 'N/A'}</td>
              <td>{appointment.status || 'N/A'}</td>
              <td>{appointment.comments || 'N/A'}</td>
              <td className="actions">
                <button
                  className="edit-button"
                  onClick={() => navigate(`/appointments/edit/${appointment.appointmentId}`)}
                >
                  Edit
                </button>
                <button
                  className="delete-button"
                  onClick={() => deleteAppointment(appointment.appointmentId)}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Appointments;
