import { useEffect, useState, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../api/axios";
import { useAuth0 } from "@auth0/auth0-react";
import { useLanguage } from "../hooks/useLanguage";
import "./Appointments.css";
import { toast } from "react-toastify";

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

// Add a helper for checking UUID format
const isUuidFormat = (str: string): boolean =>
  /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i.test(str);

const Appointments = () => {
  const { translations } = useLanguage(); // ✅ Get translations from context
  const [appointments, setAppointments] = useState<Appointment[]>([]);
  const { getAccessTokenSilently } = useAuth0();
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  const fetchAllAppointments = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);

      const token = await getAccessTokenSilently();
      const response = await axiosInstance.get<Appointment[]>("/appointments", {
        headers: { Authorization: `Bearer ${token}` },
      });

      const fetchedAppointments = response.data;

      const updatedAppointments = await Promise.all(
        fetchedAppointments.map(async (apt) => {
          if (!apt.services) return { ...apt, serviceTitles: "" };

          const serviceTokens = apt.services.split(",");
          const titles: string[] = [];

          for (const token of serviceTokens) {
            const trimmedToken = token.trim();
            if (isUuidFormat(trimmedToken)) {
              try {
                const serviceRes = await axiosInstance.get(
                  `/services/${trimmedToken}`,
                  {
                    headers: { Authorization: `Bearer ${token}` },
                  }
                );
                titles.push(
                  serviceRes.data.title ||
                    translations.appointments?.messages?.error ||
                    "Unknown Service"
                );
              } catch (err) {
                console.error("Error fetching service:", err);
                titles.push(
                  translations.appointments?.messages?.error ||
                    "Unknown Service"
                );
              }
            } else {
              titles.push(trimmedToken);
            }
          }
          return { ...apt, serviceTitles: titles.join(", ") };
        })
      );

      setAppointments(updatedAppointments);
    } catch (err) {
      console.error("Error fetching appointments:", err);
      setError(
        translations.appointments?.messages?.error ||
          "An unexpected error occurred while fetching appointments."
      );
    } finally {
      setLoading(false);
    }
  }, [translations.appointments?.messages?.error, getAccessTokenSilently]);

  const deleteAppointment = async (appointmentId: string | undefined) => {
    if (!appointmentId) {
      toast.error(
        translations.appointments?.messages?.delete_error ||
          "Invalid appointment ID"
      );
      return;
    }

    const confirmDelete = window.confirm(
      `${translations.appointments?.messages?.delete_confirm?.replace("{id}", appointmentId) || `Are you sure you want to delete the appointment with ID: ${appointmentId}?`}`
    );

    if (!confirmDelete) return;

    try {
      await axiosInstance.delete(`/appointments/${appointmentId}`);
      setAppointments((prevAppointments) =>
        prevAppointments.filter(
          (appointment) => appointment.appointmentId !== appointmentId
        )
      );
      toast.success(
        translations.appointments?.messages?.delete_success ||
          "Appointment deleted successfully."
      );
    } catch (err) {
      console.error("Error deleting appointment:", err);
      toast.error(
        translations.appointments?.messages?.delete_error ||
          "An unexpected error occurred while deleting the appointment."
      );
    }
  };

  const downloadPdf = async () => {
    try {
      const response = await axiosInstance.get("/appointments/pdf", {
        responseType: "blob",
      });

      const url = window.URL.createObjectURL(
        new Blob([response.data], { type: "application/pdf" })
      );
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", "appointments.pdf");
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (err) {
      console.error("Error downloading PDF:", err);
      toast.error(
        translations.appointments?.messages?.pdf_download_error ||
          "An error occurred while downloading the PDF."
      );
    }
  };

  // ✅ No more ESLint warning: useEffect now properly includes fetchAllAppointments
  useEffect(() => {
    fetchAllAppointments();
  }, [fetchAllAppointments]);

  if (loading) {
    return (
      <div className="loading-message">
        {translations.appointments?.messages?.loading ||
          "Loading appointments..."}
      </div>
    );
  }

  if (error) {
    return <div className="error-message">{error}</div>;
  }

  return (
    <div className="appointments-page">
      <h1 className="appointments-title">
        {translations.appointments?.title || "Appointments Page"}
      </h1>
      <div className="appointments-actions">
        <button
          className="add-appointment-button"
          onClick={() => navigate("/appointments/add")}
        >
          {translations.appointments?.actions?.add || "Add Appointment"}
        </button>
        <button className="download-pdf-button" onClick={downloadPdf}>
          {translations.appointments?.actions?.download_pdf || "Download PDF"}
        </button>
      </div>
      <table className="appointments-table">
        <thead>
          <tr>
            <th>
              {translations.appointments?.table?.appointment_id ||
                "Appointment ID"}
            </th>
            <th>
              {translations.appointments?.table?.customer_id || "Customer ID"}
            </th>
            <th>
              {translations.appointments?.table?.first_name ||
                "Customer First Name"}
            </th>
            <th>
              {translations.appointments?.table?.last_name ||
                "Customer Last Name"}
            </th>
            <th>{translations.appointments?.table?.date || "Date"}</th>
            <th>{translations.appointments?.table?.services || "Services"}</th>
            <th>{translations.appointments?.table?.status || "Status"}</th>
            <th>{translations.appointments?.table?.comments || "Comments"}</th>
            <th>{translations.appointments?.table?.actions || "Actions"}</th>
          </tr>
        </thead>
        <tbody>
          {appointments.map((appointment, index) => (
            <tr key={appointment.appointmentId || index}>
              <td>{appointment.appointmentId || "N/A"}</td>
              <td>{appointment.customerId || "N/A"}</td>
              <td>{appointment.customerFirstName || "N/A"}</td>
              <td>{appointment.customerLastName || "N/A"}</td>
              <td>{appointment.appointmentDate || "N/A"}</td>
              <td>{appointment.serviceTitles || "N/A"}</td>
              <td>{appointment.status || "N/A"}</td>
              <td>{appointment.comments || "N/A"}</td>
              <td className="actions">
                <button
                  className="edit-button"
                  onClick={() =>
                    navigate(`/appointments/edit/${appointment.appointmentId}`)
                  }
                >
                  {translations.appointments?.table?.edit || "Edit"}
                </button>
                <button
                  className="delete-button"
                  onClick={() => deleteAppointment(appointment.appointmentId)}
                >
                  {translations.appointments?.table?.delete || "Delete"}
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
