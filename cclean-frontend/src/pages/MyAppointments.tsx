import React, { useState } from 'react';
import axiosInstance from '../api/axios';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import './Appointments.css';

interface Appointment {
  appointmentId: string;
  customerId: string;
  customerFirstName?: string;
  customerLastName?: string;
  appointmentDate: string;
  services: string;      // e.g. "id1,id2" or "Some Service"
  serviceTitles?: string; // We'll store the final display string here
  status: string;
  comments?: string;
}

// We'll treat "pending" or "confirmed" as "upcoming".
function isUpcoming(status: string | undefined) {
  return status === 'pending' || status === 'confirmed';
}

// Check if a string is a valid 36-char UUID
function isUuidFormat(str: string): boolean {
  return /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i.test(str);
}

const MyAppointments: React.FC = () => {
  const [customerIdInput, setCustomerIdInput] = useState('');
  const [appointments, setAppointments] = useState<Appointment[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [hasSearched, setHasSearched] = useState(false);
  const navigate = useNavigate();

  // Helper to transform "services" -> "serviceTitles"
  const transformAppointments = async (rawAppointments: Appointment[]): Promise<Appointment[]> => {
    const transformed = await Promise.all(
      rawAppointments.map(async (apt) => {
        if (!apt.services) {
          return { ...apt, serviceTitles: '' };
        }

        const tokens = apt.services.split(',').map(t => t.trim());
        const titles: string[] = [];

        for (const token of tokens) {
          if (isUuidFormat(token)) {
            // It's a valid UUID, fetch the service name
            try {
              const serviceRes = await axiosInstance.get(`/services/${token}`);
              titles.push(serviceRes.data.title || 'Unknown Service');
            } catch {
              titles.push('Unknown Service');
            }
          } else {
            // Not a UUID, treat as plain text
            titles.push(token);
          }
        }

        return { ...apt, serviceTitles: titles.join(', ') };
      })
    );
    return transformed;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setAppointments([]);
    setHasSearched(true);

    if (!customerIdInput.trim()) {
      setError('Customer ID is required');
      return;
    }

    try {
      const response = await axiosInstance.get<Appointment[]>(
        `/appointments/by-customer/${customerIdInput.trim()}`
      );
      // Transform the services into display titles
      const updated = await transformAppointments(response.data);
      setAppointments(updated);
    } catch (err) {
      if (err instanceof Error) {
        setError(err.message);
      } else {
        setError('Failed to load appointments. Please try again.');
      }
    }
  };

  // Cancel an upcoming appointment
  const handleCancel = async (appointmentId: string) => {
    const confirm = window.confirm('Are you sure you want to cancel this appointment?');
    if (!confirm) return;

    try {
      // We'll do a PUT to /appointments/customer/:id with { status: 'cancelled' }
      // or if you're okay with the same endpoint as admin for status changes, that's fine:
      await axiosInstance.put(`/appointments/customer/${appointmentId}`, { status: 'cancelled' });
      // Update in local state
      const updated = appointments.map((apt) =>
        apt.appointmentId === appointmentId ? { ...apt, status: 'cancelled' } : apt
      );
      setAppointments(updated);
      toast.success('Appointment cancelled successfully.');
    } catch (err) {
      console.error('Error canceling appointment:', err);
      toast.error('Failed to cancel appointment.');
    }
  };

  // Reschedule an upcoming appointment (customer path)
  const handleReschedule = (appointmentId: string) => {
    // This goes to your new "CustomerEdit" route, e.g.:
    navigate(`/my-appointments/edit/${appointmentId}`);
  };

  return (
    <div className="my-appointments-container">
      <h1 className="my-appointments-title">My Appointments</h1>

      <form onSubmit={handleSubmit} className="my-appointments-form">
        <label>Enter Customer ID: </label>
        <input
          type="text"
          value={customerIdInput}
          onChange={(e) => setCustomerIdInput(e.target.value)}
        />
        <button type="submit">View Appointments</button>
      </form>

      {error && <p className="my-appointments-error">{error}</p>}

      {hasSearched && !error && appointments.length === 0 && (
        <p className="my-appointments-no-results">No appointments found for that customer ID.</p>
      )}

      {appointments.length > 0 && (
        <table className="my-appointments-table">
          <thead>
            <tr>
              <th>Appointment ID</th>
              <th>Date/Time</th>
              <th>Services</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {appointments.map((apt) => (
              <tr key={apt.appointmentId}>
                <td>{apt.appointmentId}</td>
                <td>{apt.appointmentDate}</td>
                <td>{apt.serviceTitles || apt.services}</td>
                <td>{apt.status}</td>
                <td>
                  {isUpcoming(apt.status) && (
                    <>
                      <button onClick={() => handleCancel(apt.appointmentId)}>Cancel</button>
                      <button onClick={() => handleReschedule(apt.appointmentId)}>Reschedule</button>
                    </>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default MyAppointments;
