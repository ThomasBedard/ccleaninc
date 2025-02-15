import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { useAxiosWithAuth } from "../api/axios";
import { useAuth0 } from "@auth0/auth0-react";
import { useLanguage } from "../hooks/useLanguage";

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
  const { translations } = useLanguage();
  const location = useLocation();
  const { getAccessTokenSilently, user } = useAuth0();
  
  const { selectedServiceIds, appointmentDate } = location.state as {
    selectedServiceIds: string[];
    appointmentDate: string;
  };

  const [services, setServices] = useState<Service[]>([]);
  const [customerFirstName, setCustomerFirstName] = useState("");
  const [customerLastName, setCustomerLastName] = useState("");
  const [comments, setComments] = useState("");
  const [loading, setLoading] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  useEffect(() => {
    const fetchCustomerDetails = async () => {
      try {
        const token = await getAccessTokenSilently();
        
        if (!user || !user.email) {
          throw new Error("No email found in Auth0 user object.");
        }
    
        const userEmail = user.email;
    
        const res = await axiosInstance.get(`/customers/byEmail?email=${encodeURIComponent(userEmail)}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
    
        if (res.status === 200) {
          setCustomerFirstName(res.data.firstName);
          setCustomerLastName(res.data.lastName);
        }
      } catch (error) {
        console.error("Error fetching customer details:", error);
        setErrorMessage("Error fetching customer details.");
      }
    };

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

    fetchCustomerDetails();
    if (selectedServiceIds?.length) {
      fetchServices();
    }
  }, [getAccessTokenSilently, axiosInstance, translations, selectedServiceIds, user]);

  const handleSubmit = async () => {
    setSuccessMessage("");
    setErrorMessage("");
    setLoading(true);

    if (!customerFirstName.trim() || !customerLastName.trim()) {
      setErrorMessage(translations.checkout?.error?.invalid_fields || "Please enter valid details.");
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
        customerFirstName: customerFirstName.trim(),
        customerLastName: customerLastName.trim(),
        appointmentDate: formattedDate,
        services: servicesString,
        comments,
        status: "pending",
      };

      const response = await axiosInstance.post("/appointments", payload, {
        headers: { "Content-Type": "application/json" },
      });

      if (response.status === 200 || response.status === 201) {
        setSuccessMessage(translations.checkout?.success?.appointment_created || "Appointment created successfully!");
        
        // Navigate immediately with the new appointment data
        navigate("/my-appointments", { 
          state: { 
            newAppointment: response.data,
            timestamp: new Date().getTime()
          } 
        });
      }
    } catch (error) {
      setErrorMessage(translations.checkout?.error?.appointment_creation || "Error creating appointment.");
    } finally {
      setLoading(false);
    }
  };

  const totalPrice = services.reduce((sum, service) => sum + service.pricing, 0);

  return (
    <div style={{ padding: "40px", maxWidth: "600px", margin: "0 auto", boxShadow: "0 2px 10px rgba(0,0,0,0.1)", borderRadius: "10px", backgroundColor: "#f9f9f9" }}>
      <h1 style={{ textAlign: "center", marginBottom: "20px" }}>
        {translations.checkout?.title || "Confirm Your Appointment"}
      </h1>

      <div style={{ marginBottom: "20px" }}>
        <h2>{translations.checkout?.selected_services || "Selected Services"}</h2>
        {services.length > 0 ? (
          <>
            {services.map((svc) => (
              <div key={svc.serviceId} style={{ padding: "10px 0", borderBottom: "1px solid #ddd" }}>
                <strong>{svc.title}</strong> - ${svc.pricing.toFixed(2)}
              </div>
            ))}
            <div style={{ marginTop: "10px", textAlign: "right" }}>
              <strong>Total: ${totalPrice.toFixed(2)}</strong>
            </div>
          </>
        ) : (
          <p>{translations.checkout?.no_services || "No service details available."}</p>
        )}
      </div>

      <div style={{ marginBottom: "20px" }}>
        <h3>{translations.checkout?.date_time || "Date & Time"}:</h3>
        <p>{new Date(appointmentDate).toLocaleString()}</p>
      </div>

      <div style={{ marginBottom: "20px" }}>
        <label>{translations.checkout?.first_name || "First Name"}:</label>
        <input 
          type="text" 
          value={customerFirstName} 
          readOnly 
          style={{ 
            width: "100%", 
            padding: "8px", 
            borderRadius: "5px", 
            border: "1px solid #ccc", 
            backgroundColor: "#f2f2f2" 
          }} 
        />
      </div>

      <div style={{ marginBottom: "20px" }}>
        <label>{translations.checkout?.last_name || "Last Name"}:</label>
        <input 
          type="text" 
          value={customerLastName} 
          readOnly 
          style={{ 
            width: "100%", 
            padding: "8px", 
            borderRadius: "5px", 
            border: "1px solid #ccc", 
            backgroundColor: "#f2f2f2" 
          }} 
        />
      </div>

      <div style={{ marginBottom: "20px" }}>
        <label>{translations.checkout?.comments || "Comments"}:</label>
        <textarea 
          value={comments} 
          onChange={(e) => setComments(e.target.value)} 
          style={{ 
            width: "100%", 
            padding: "8px", 
            borderRadius: "5px", 
            border: "1px solid #ccc",
            minHeight: "100px"
          }} 
          placeholder={translations.checkout?.comments_placeholder || "Additional comments (optional)"} 
        />
      </div>

      {errorMessage && (
        <p style={{ color: "red", textAlign: "center", padding: "10px", backgroundColor: "#ffe6e6", borderRadius: "5px" }}>
          {errorMessage}
        </p>
      )}
      
      {successMessage && (
        <p style={{ color: "green", textAlign: "center", padding: "10px", backgroundColor: "#e6ffe6", borderRadius: "5px" }}>
          {successMessage}
        </p>
      )}

      <button 
        onClick={handleSubmit} 
        disabled={loading} 
        style={{ 
          width: "100%", 
          padding: "12px", 
          backgroundColor: loading ? "#ccc" : "#28a745", 
          color: "#fff", 
          border: "none", 
          borderRadius: "5px", 
          cursor: loading ? "not-allowed" : "pointer", 
          fontSize: "16px",
          transition: "background-color 0.3s ease"
        }}
      >
        {loading ? (
          translations.checkout?.confirming || "Confirming..."
        ) : (
          translations.checkout?.confirm_button || "Confirm Appointment"
        )}
      </button>
    </div>
  );
};

export default CheckoutPage;