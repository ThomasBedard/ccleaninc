import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { useAxiosWithAuth } from "../api/axios";
import { useLanguage } from "../hooks/useLanguage"; // ✅ Import translations

interface Service {
  serviceId: string;
  title: string;
  description: string;
  pricing: number;
  isAvailable: boolean;
}

const CheckoutPage: React.FC = () => {
  const axiosInstance = useAxiosWithAuth();
  const navigate = useNavigate();
  const { translations } = useLanguage(); // ✅ Get translations from context
  const location = useLocation();
  const { selectedServiceIds, appointmentDate } = location.state as {
    selectedServiceIds: string[];
    appointmentDate: string;
  };

  const [services, setServices] = useState<Service[]>([]);
  const [customerId, setCustomerId] = useState("");
  const [customerFirstName, setCustomerFirstName] = useState("");
  const [customerLastName, setCustomerLastName] = useState("");
  const [comments, setComments] = useState("");
  const [loading, setLoading] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  useEffect(() => {
    const fetchServices = async () => {
      try {
        const fetched: Service[] = [];
        for (const id of selectedServiceIds) {
          const res = await axiosInstance.get<Service>(`/services/${id}`);
          if (res.status === 200) {
            fetched.push(res.data);
          }
        }
        setServices(fetched);
      } catch {
        setErrorMessage(translations.checkout?.error?.fetch_services || "Error fetching services.");
      }
    };

    if (selectedServiceIds?.length) {
      fetchServices();
    }
  }, [selectedServiceIds, axiosInstance, translations]);

  const checkCustomerExists = async (id: string): Promise<boolean> => {
    try {
      const res = await axiosInstance.get(`/customers/${id}`);
      return res.status === 200;
    } catch {
      setErrorMessage(translations.checkout?.error?.invalid_customer || "Invalid Customer ID.");
      return false;
    }
  };

  const handleSubmit = async () => {
    setSuccessMessage("");
    setErrorMessage("");
    setLoading(true);

    if (!customerId.trim() || !customerFirstName.trim() || !customerLastName.trim()) {
      setErrorMessage(translations.checkout?.error?.invalid_fields || "Please enter valid details.");
      setLoading(false);
      return;
    }

    const customerExists = await checkCustomerExists(customerId.trim());
    if (!customerExists) {
      setLoading(false);
      return;
    }

    if (!appointmentDate) {
      setErrorMessage(translations.checkout?.error?.no_date || "No appointment date selected.");
      setLoading(false);
      return;
    }

    try {
      const servicesString = services.map((service) => service.title).join(", ");
      const formattedDate = appointmentDate.includes("T") ? appointmentDate : `${appointmentDate}T12:00`;

      const payload = {
        customerId: customerId.trim(),
        customerFirstName: customerFirstName.trim(),
        customerLastName: customerLastName.trim(),
        appointmentDate: formattedDate,
        services: servicesString,
        comments,
        status: "pending",
      };

      const response = await axiosInstance.post(
        `/appointments/customers/${customerId.trim()}`,
        payload,
        { headers: { "Content-Type": "application/json" } }
      );

      if (response.status === 200 || response.status === 201) {
        setSuccessMessage(translations.checkout?.success?.appointment_created || "Appointment created successfully!");
        setTimeout(() => navigate("/services"), 2000);
      } else {
        throw new Error();
      }
    } catch {
      setErrorMessage(translations.checkout?.error?.appointment_creation || "Error creating appointment.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: "40px", maxWidth: "600px", margin: "0 auto", boxShadow: "0 2px 10px rgba(0,0,0,0.1)", borderRadius: "10px", backgroundColor: "#f9f9f9" }}>
      <h1 style={{ textAlign: "center", marginBottom: "20px" }}>
        {translations.checkout?.title || "Confirm Your Appointment"}
      </h1>

      <div style={{ marginBottom: "20px" }}>
        <h2>{translations.checkout?.selected_services || "Selected Services"}</h2>
        {services.length > 0 ? (
          services.map((svc) => (
            <div key={svc.serviceId} style={{ padding: "10px 0", borderBottom: "1px solid #ddd" }}>
              <strong>{svc.title}</strong> - ${svc.pricing.toFixed(2)}
            </div>
          ))
        ) : (
          <p>{translations.checkout?.no_services || "No service details available."}</p>
        )}
      </div>

      <div style={{ marginBottom: "20px" }}>
        <h3>{translations.checkout?.date_time || "Date & Time"}:</h3>
        <p>{appointmentDate}</p>
      </div>

      <div style={{ marginBottom: "20px" }}>
        <label>{translations.checkout?.customer_id || "Customer ID"}:</label>
        <input type="text" value={customerId} onChange={(e) => setCustomerId(e.target.value)} style={{ width: "100%", padding: "8px", borderRadius: "5px", border: "1px solid #ccc" }} placeholder={translations.checkout?.customer_id || "Customer ID"} />
      </div>

      <div style={{ marginBottom: "20px" }}>
        <label>{translations.checkout?.first_name || "First Name"}:</label>
        <input type="text" value={customerFirstName} onChange={(e) => setCustomerFirstName(e.target.value)} style={{ width: "100%", padding: "8px", borderRadius: "5px", border: "1px solid #ccc" }} placeholder={translations.checkout?.first_name || "First Name"} />
      </div>

      <div style={{ marginBottom: "20px" }}>
        <label>{translations.checkout?.last_name || "Last Name"}:</label>
        <input type="text" value={customerLastName} onChange={(e) => setCustomerLastName(e.target.value)} style={{ width: "100%", padding: "8px", borderRadius: "5px", border: "1px solid #ccc" }} placeholder={translations.checkout?.last_name || "Last Name"} />
      </div>

      <div style={{ marginBottom: "20px" }}>
        <label>{translations.checkout?.comments || "Comments"}:</label>
        <textarea value={comments} onChange={(e) => setComments(e.target.value)} style={{ width: "100%", padding: "8px", borderRadius: "5px", border: "1px solid #ccc" }} placeholder={translations.checkout?.comments_placeholder || "Additional comments (optional)"} />
      </div>

      {errorMessage && <p style={{ color: "red", textAlign: "center" }}>{errorMessage}</p>}
      {successMessage && <p style={{ color: "green", textAlign: "center" }}>{successMessage}</p>}

      <button onClick={handleSubmit} disabled={loading} style={{ width: "100%", padding: "10px", backgroundColor: loading ? "#ccc" : "#28a745", color: "#fff", border: "none", borderRadius: "5px", cursor: loading ? "not-allowed" : "pointer", fontSize: "16px" }}>
        {loading ? translations.checkout?.confirming || "Confirming..." : translations.checkout?.confirm_button || "Confirm Appointment"}
      </button>
    </div>
  );
};

export default CheckoutPage;
