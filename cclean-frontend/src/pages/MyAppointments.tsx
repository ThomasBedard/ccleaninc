import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import axiosInstance from "../api/axios";
import { useAuth0 } from "@auth0/auth0-react";
import { extractEmailFromToken } from "../api/authUtils";
import { useLanguage } from "../hooks/useLanguage"; // ✅ Import translation hook
import "./MyAppointments.css";

interface Appointment {
  appointmentId: string;
  appointmentDate: string;
  services: string;
  status: string;
  comments?: string;
  customerFirstName: string;
  customerLastName: string;
}

interface LocationState {
  newAppointment?: Appointment;
  timestamp?: number;
}

const MyAppointments = () => {
  const { getAccessTokenSilently } = useAuth0();
  const { translations } = useLanguage(); // ✅ Fetch translations
  const [appointments, setAppointments] = useState<Appointment[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [userEmail, setUserEmail] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [filterStatus, setFilterStatus] = useState<string>("all");
  
  const location = useLocation();
  const { newAppointment, timestamp } = (location.state as LocationState) || {};

  const fetchAppointments = async () => {
    try {
      setLoading(true);
      const token = await getAccessTokenSilently();
      const email = extractEmailFromToken(token);
      
      if (!email) {
        setError(translations.myAppointments?.error?.no_email || "User email not found in token.");
        return;
      }

      setUserEmail(email);

      const response = await axiosInstance.get("/appointments/my-appointments", {
        headers: { 
          Authorization: `Bearer ${token}`,
          'Cache-Control': 'no-cache',
          'Pragma': 'no-cache'
        },
      });

      let appointmentList = response.data;

      // ✅ Add new appointment to the list if it doesn't exist
      if (newAppointment && timestamp && Date.now() - timestamp < 1000) {
        const appointmentExists = appointmentList.some(
          (apt: Appointment) => apt.appointmentId === newAppointment.appointmentId
        );

        if (!appointmentExists) {
          appointmentList = [newAppointment, ...appointmentList];
        }
      }

      // ✅ Sort appointments by date (most recent first)
      appointmentList.sort((a: Appointment, b: Appointment) => 
        new Date(b.appointmentDate).getTime() - new Date(a.appointmentDate).getTime()
      );

      setAppointments(appointmentList);
    } catch (error) {
      console.error("❌ Failed to load appointments:", error);
      setError(translations.myAppointments?.error?.fetch_failed || "Failed to load appointments. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAppointments();
    
    // ✅ Refresh appointments every 30 seconds
    const intervalId = setInterval(fetchAppointments, 30000);
    
    // ✅ Cleanup interval on component unmount
    return () => clearInterval(intervalId);
  }, [getAccessTokenSilently, timestamp]);

  const filteredAppointments = appointments.filter(apt => 
    filterStatus === "all" || apt.status === filterStatus
  );

  const getStatusColor = (status: string) => {
    switch (status.toLowerCase()) {
      case 'confirmed':
        return 'status-confirmed';
      case 'pending':
        return 'status-pending';
      case 'cancelled':
        return 'status-cancelled';
      default:
        return 'status-default';
    }
  };

  return (
    <div className="my-appointments-container">
      <div className="my-appointments-card">
        <h2 className="my-appointments-title">
          {translations.myAppointments?.title || "My Appointments"}
        </h2>

        {loading ? (
          <div className="loader"></div>
        ) : error ? (
          <p className="my-appointments-error">{error}</p>
        ) : (
          <>
            {userEmail && (
              <p className="my-appointments-email">
                {translations.myAppointments?.showing_for || "Showing appointments for:"}{" "}
                <strong>{userEmail}</strong>
              </p>
            )}

            <div className="filter-section">
              <label htmlFor="status-filter">
                {translations.myAppointments?.filter?.label || "Filter by status:"}
              </label>
              <select 
                id="status-filter"
                value={filterStatus}
                onChange={(e) => setFilterStatus(e.target.value)}
                className="status-filter"
              >
                <option value="all">{translations.myAppointments?.filter?.all || "All"}</option>
                <option value="pending">{translations.myAppointments?.filter?.pending || "Pending"}</option>
                <option value="confirmed">{translations.myAppointments?.filter?.confirmed || "Confirmed"}</option>
                <option value="cancelled">{translations.myAppointments?.filter?.cancelled || "Cancelled"}</option>
              </select>
            </div>

            {filteredAppointments.length > 0 ? (
              <div className="appointments-list">
                {filteredAppointments.map((apt) => (
                  <div key={apt.appointmentId} className="appointment-item">
                    <div className="appointment-header">
                      <p className="appointment-name">
                        {apt.customerFirstName} {apt.customerLastName}
                      </p>
                      <span className={`appointment-status ${getStatusColor(apt.status)}`}>
                        {translations.myAppointments?.status?.[apt.status as keyof typeof translations.myAppointments.status] || 
                        apt.status.charAt(0).toUpperCase() + apt.status.slice(1)}
                      </span>

                    </div>
                    
                    <p className="appointment-service">{apt.services}</p>
                    <p className="appointment-date">
                      {new Date(apt.appointmentDate).toLocaleString()}
                    </p>
                    
                    {apt.comments && (
                      <p className="appointment-comments">
                        <strong>{translations.myAppointments?.comments || "Comments"}:</strong> {apt.comments}
                      </p>
                    )}
                  </div>
                ))}
              </div>
            ) : (
              <p className="no-appointments">
                {filterStatus === "all" 
                  ? translations.myAppointments?.no_appointments || "No appointments found."
                  : translations.myAppointments?.no_filtered_appointments?.replace("{status}", filterStatus) || `No ${filterStatus} appointments found.`}
              </p>
            )}
          </>
        )}
      </div>
    </div>
  );
};

export default MyAppointments;
