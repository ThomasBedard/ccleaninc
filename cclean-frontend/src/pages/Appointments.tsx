import { useEffect, useState, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../api/axios';
import { useLanguage } from '../hooks/useLanguage';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import './Appointments.css';

interface Appointment {
  appointmentId?: string;
  customerId?: string;
  customerFirstName?: string;
  customerLastName?: string;
  appointmentDate?: string;
  services?: string;
  serviceTitles?: string;
  status?: string;
  comments?: string;
}

const Appointments = () => {
  const { translations } = useLanguage();
  const [appointments, setAppointments] = useState<Appointment[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();
  
  const [currentPage, setCurrentPage] = useState<number>(0);
  const [totalPages, setTotalPages] = useState<number>(1);
  const pageSize = 6; // ✅ Each page contains 6 appointments

  // ✅ Fetch paginated appointments
  const fetchAllAppointments = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
  
      const response = await axiosInstance.get(`/appointments/paged`, {
        params: { page: currentPage, size: pageSize },
      });

      console.log("API Response:", response.data); // Debugging log

      if (response.data && response.data.content) {
        setAppointments(response.data.content);
        setTotalPages(Math.min(response.data.totalPages, 99)); // ✅ Limit to max 99 pages
      } else {
        setAppointments([]);
        setTotalPages(1);
      }
    } catch (err) {
      console.error("Error fetching appointments:", err);
      setError("An error occurred while fetching appointments.");
    } finally {
      setLoading(false);
    }
  }, [currentPage]);

  useEffect(() => {
    fetchAllAppointments();
  }, [fetchAllAppointments]);

  // ✅ Delete an appointment and update pagination
  const deleteAppointment = async (appointmentId: string | undefined) => {
    if (!appointmentId) {
      toast.error('Invalid appointment ID');
      return;
    }

    const confirmDelete = window.confirm(`Are you sure you want to delete the appointment with ID: ${appointmentId}?`);
    if (!confirmDelete) return;

    try {
      await axiosInstance.delete(`/appointments/${appointmentId}`);
      toast.success('Appointment deleted successfully.');

      // ✅ Fetch updated appointments after deletion
      fetchAllAppointments();
    } catch (err) {
      console.error("Error deleting appointment:", err);
      toast.error('An unexpected error occurred while deleting the appointment.');
    }
  };

  // ✅ Download PDF function
  const downloadPdf = async () => {
    try {
      const response = await axiosInstance.get('/appointments/pdf', {
        responseType: 'blob',
      });

      const url = window.URL.createObjectURL(new Blob([response.data], { type: 'application/pdf' }));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', 'appointments.pdf');
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (err) {
      console.error("Error downloading PDF:", err);
      toast.error('An error occurred while downloading the PDF.');
    }
  };

  if (loading) return <div className="loading-message">Loading appointments...</div>;
  if (error) return <div className="error-message">{error}</div>;

  return (
    <div className="appointments-page">
      <h1 className="appointments-title">{translations.appointments?.title || 'Appointments Page'}</h1>
      
      <div className="appointments-actions">
        <button className="add-appointment-button" onClick={() => navigate('/appointments/add')}>
          {translations.appointments?.actions?.add || 'Add Appointment'}
        </button>
        
        {/* ✅ Re-added the Download PDF button */}
        <button className="download-pdf-button" onClick={downloadPdf}>
          {translations.appointments?.actions?.download_pdf || 'Download PDF'}
        </button>
      </div>

      <table className="appointments-table">
        <thead>
          <tr>
            <th>Appointment ID</th>
            <th>Customer ID</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Date</th>
            <th>Services</th>
            <th>Status</th>
            <th>Comments</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {appointments.length > 0 ? (
            appointments.map((appointment, index) => (
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
                  <button className="edit-button" onClick={() => navigate(`/appointments/edit/${appointment.appointmentId}`)}>
                    Edit
                  </button>
                  <button className="delete-button" onClick={() => deleteAppointment(appointment.appointmentId)}>
                    Delete
                  </button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan={9} className="no-data">No appointments available</td>
            </tr>
          )}
        </tbody>
      </table>

      {/* ✅ Pagination Controls */}
      <div className="pagination">
        <button 
          disabled={currentPage === 0} 
          onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 0))}
        >
          ← Previous
        </button>
        <span> Page {currentPage + 1} of {totalPages} </span>
        <button 
          disabled={currentPage >= totalPages - 1} 
          onClick={() => setCurrentPage((prev) => Math.min(prev + 1, totalPages - 1))}
        >
          Next →
        </button>
      </div>
    </div>
  );
};

export default Appointments;
