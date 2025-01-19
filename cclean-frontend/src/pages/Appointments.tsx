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
  services?: string;      // e.g. "id1,id2,id3" or "Residential Cleaning Service"
  serviceTitles?: string; // We'll add this property after we fetch names
  status?: string;
  comments?: string;
}

// Add a helper for checking UUID format
function isUuidFormat(str: string): boolean {
  return /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i.test(str);
}

const Appointments = () => {
  const [appointments, setAppointments] = useState<Appointment[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  // Fetch all appointments, then transform service IDs -> service titles
  const fetchAllAppointments = async () => {
    try {
      setLoading(true);
      setError(null);
      const response = await axiosInstance.get<Appointment[]>('/appointments');
      const fetchedAppointments = response.data;

      // Transform each appointment's 'services' from IDs to titles
      const updatedAppointments = await Promise.all(
        fetchedAppointments.map(async (apt) => {
          if (!apt.services) {
            return { ...apt, serviceTitles: '' };
          }

          // Example: "id1,id2" or "Residential Cleaning Service"
          const serviceTokens = apt.services.split(',');
          const titles: string[] = [];

          for (const token of serviceTokens) {
            const trimmedToken = token.trim();

            if (isUuidFormat(trimmedToken)) {
              // It's a valid UUID, so fetch from the /services endpoint
              try {
                const serviceRes = await axiosInstance.get(`/services/${trimmedToken}`);
                // If successful, push the service's title
                titles.push(serviceRes.data.title || 'Unknown Service');
              } catch {
                // If we fail to fetch by ID, treat it as unknown
                titles.push('Unknown Service');
              }
            } else {
              // It's NOT a UUID, so treat it as a literal service name
              titles.push(trimmedToken);
            }
          }

          // Join them into a single string for display
          return { ...apt, serviceTitles: titles.join(', ') };
        })
      );

      setAppointments(updatedAppointments);
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

  // Function to delete an appointment
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

  // Function to download the PDF
  const downloadPdf = async () => {
    try {
      const response = await axiosInstance.get('/appointments/pdf', {
        responseType: 'blob', // Important to receive the PDF as a blob
      });

      const url = window.URL.createObjectURL(new Blob([response.data], { type: 'application/pdf' }));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', 'appointments.pdf'); // Set the file name
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (err) {
      alert(
        err instanceof Error
          ? err.message
          : 'An error occurred while downloading the PDF.'
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
      <div className="appointments-actions">
        <button
          className="add-appointment-button"
          onClick={() => navigate('/appointments/add')}
        >
          Add Appointment
        </button>
        <button
          className="download-pdf-button"
          onClick={downloadPdf}
        >
          Download PDF
        </button>
      </div>
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
              <td>{appointment.serviceTitles || 'N/A'}</td>
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
