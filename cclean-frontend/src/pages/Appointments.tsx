import { useEffect, useState } from 'react';
import axiosInstance from '../api/axios';
import './Appointments.css';

interface Appointment {
  appointmentId?: string;
  customerId?: string;
  appointmentDate?: string;
  services?: string;
  status?: string;
  comments?: string;
}

const Appointments = () => {
  const [appointments, setAppointments] = useState<Appointment[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

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
      <table className="appointments-table">
        <thead>
          <tr>
            <th>Appointment ID</th>
            <th>Customer ID</th>
            <th>Date</th>
            <th>Services</th>
            <th>Status</th>
            <th>Comments</th>
          </tr>
        </thead>
        <tbody>
          {appointments.map((appointment, index) => (
            <tr key={appointment.appointmentId || index}>
              <td>{appointment.appointmentId || 'N/A'}</td>
              <td>{appointment.customerId || 'N/A'}</td>
              <td>{appointment.appointmentDate || 'N/A'}</td>
              <td>{appointment.services || 'N/A'}</td>
              <td>{appointment.status || 'N/A'}</td>
              <td>{appointment.comments || 'N/A'}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Appointments;
