import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { useAxiosWithAuth } from "../api/axios";
import { useAuth0 } from "@auth0/auth0-react";
import { useLanguage } from "../hooks/useLanguage";
import "./CheckoutPage.css";

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
  const { translations } = useLanguage(); // ✅ Get translations dynamically
  const location = useLocation();
  const { getAccessTokenSilently, user } = useAuth0();

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
  const [errorMessage, setErrorMessage] = useState("");

  useEffect(() => {
    const fetchCustomerDetails = async () => {
      try {
        const token = await getAccessTokenSilently();

        if (!user || !user.email) {
          throw new Error("No email found in Auth0 user object.");
        }

        const res = await axiosInstance.get(
          `/customers/byEmail?email=${encodeURIComponent(user.email)}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );

        if (res.status === 200) {
          setCustomerId(res.data.customerId);
          setCustomerFirstName(res.data.firstName || "");
          setCustomerLastName(res.data.lastName || "");
        }
      } catch (error) {
        console.error("Error fetching customer details:", error);
        setErrorMessage(translations.checkout?.error?.fetch_customer || "Error fetching customer details.");
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
        customerId,
        customerFirstName: customerFirstName.trim(),
        customerLastName: customerLastName.trim(),
        appointmentDate: formattedDate,
        services: servicesString,
        comments,
        status: "pending",
      };

      const response = await axiosInstance.post(`/appointments/customers/${customerId}`, payload, {
        headers: { "Content-Type": "application/json" },
      });

      if (response.status === 200 || response.status === 201) {
        alert(translations.checkout?.success?.appointment_created || "Your appointment has been booked! You will receive a confirmation email shortly.");
        
        navigate("/my-appointments", {
          state: {
            newAppointment: response.data,
            timestamp: new Date().getTime(),
          },
        });
      }
    } catch (error) {
      console.error("❌ Error creating appointment:", error);
      setErrorMessage(translations.checkout?.error?.appointment_creation || "Error creating appointment. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  const totalPrice = services.reduce((sum, service) => sum + service.pricing, 0);

  return (
    <div className="checkout-container">
      <h1>{translations.checkout?.title || "Confirm Your Appointment"}</h1>

      <div className="checkout-section">
        <h2>{translations.checkout?.selected_services || "Selected Services"}</h2>
        {services.length > 0 ? (
          <>
            {services.map((svc) => (
              <div key={svc.serviceId} className="checkout-service">
                <strong>{svc.title}</strong> - ${svc.pricing.toFixed(2)}
              </div>
            ))}
            <div className="checkout-total">
              <strong>{translations.checkout?.total || "Total"}: ${totalPrice.toFixed(2)}</strong>
            </div>
          </>
        ) : (
          <p>{translations.checkout?.no_services || "No service details available."}</p>
        )}
      </div>

      <div className="checkout-section">
        <h3>{translations.checkout?.date_time || "Date & Time"}:</h3>
        <p>{new Date(appointmentDate).toLocaleString()}</p>
      </div>

      <div className="checkout-section">
        <label>{translations.checkout?.first_name || "First Name"}:</label>
        <input type="text" value={customerFirstName} readOnly className="checkout-input read-only" />
      </div>

      <div className="checkout-section">
        <label>{translations.checkout?.last_name || "Last Name"}:</label>
        <input type="text" value={customerLastName} readOnly className="checkout-input read-only" />
      </div>

      <div className="checkout-section">
        <label>{translations.checkout?.comments || "Comments"}:</label>
        <textarea
          value={comments}
          onChange={(e) => setComments(e.target.value)}
          className="checkout-textarea"
          placeholder={translations.checkout?.comments_placeholder || "Additional comments (optional)"}
        />
      </div>

      {errorMessage && <p className="checkout-error">{errorMessage}</p>}

      <button onClick={handleSubmit} disabled={loading} className="checkout-button">
        {loading
          ? translations.checkout?.confirming || "Confirming..."
          : translations.checkout?.confirm_button || "Confirm Appointment"}
      </button>
    </div>
  );
};

export default CheckoutPage;
