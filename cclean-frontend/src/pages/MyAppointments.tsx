import { useEffect, useState } from "react";
import axiosInstance from "../api/axios";
import { useAuth0 } from "@auth0/auth0-react";
import { extractEmailFromToken } from "../api/authUtils";
import "./MyAppointments.css"; 

interface Appointment {
  appointmentId: string;
  appointmentDate: string;
  services: string;
  status: string;
  comments?: string;
}

const MyAppointments = () => {
  const { getAccessTokenSilently } = useAuth0();
  const [appointments, setAppointments] = useState<Appointment[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [userEmail, setUserEmail] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    const fetchAppointments = async () => {
      try {
        const token = await getAccessTokenSilently();
        const email = extractEmailFromToken(token);
        if (!email) {
          setError("User email not found in token.");
          return;
        }

        setUserEmail(email);

        const response = await axiosInstance.get("/appointments/my-appointments", {
          headers: { Authorization: `Bearer ${token}` },
        });

        setAppointments(response.data);
      } catch (error) {
        console.error("❌ Failed to load appointments:", error);
        setError("Failed to load appointments. Please try again.");
      } finally {
        setLoading(false);
      }
    };

    fetchAppointments();
  }, [getAccessTokenSilently]);

  return (
    <div className="my-appointments-container">
      <div className="my-appointments-card">
        <h2 className="my-appointments-title">My Appointments</h2>

        {loading ? (
          <div className="loader"></div>
        ) : error ? (
          <p className="my-appointments-error">{error}</p>
        ) : (
          <>
            {userEmail && (
              <p className="my-appointments-email">
                Showing appointments for: <strong>{userEmail}</strong>
              </p>
            )}

            {appointments.length > 0 ? (
              <div>
                {appointments.map((apt) => (
                  <div key={apt.appointmentId} className="appointment-item">
                    <p className="appointment-service">{apt.services}</p>
                    <p className="appointment-date">{new Date(apt.appointmentDate).toLocaleString()}</p>
                    <p className={`appointment-status ${apt.status === 'pending' ? 'status-pending' : 'status-confirmed'}`}>
                      {apt.status.charAt(0).toUpperCase() + apt.status.slice(1)}
                    </p>
                  </div>
                ))}
              </div>
            ) : (
              <p className="no-appointments">No appointments found.</p>
            )}
          </>
        )}
      </div>
    </div>
  );
};

export default MyAppointments;
