import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom"; // ✅ Import useNavigate
import { useAxiosWithAuth } from "../api/axios";

interface Service {
  serviceId: string;
  title: string;
  description: string;
  pricing: number;
  isAvailable: boolean;
}

const CheckoutPage: React.FC = () => {
  const axiosInstance = useAxiosWithAuth();
  const navigate = useNavigate(); // ✅ Initialize navigation hook
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
      } catch (err) {
        console.error("❌ Error fetching services:", err);
      }
    };

    if (selectedServiceIds?.length) {
      fetchServices();
    }
  }, [selectedServiceIds, axiosInstance]);

  const checkCustomerExists = async (id: string): Promise<boolean> => {
    try {
      const res = await axiosInstance.get(`/customers/${id}`);
      return res.status === 200;
    } catch {
      console.error("❌ Error verifying customer.");
      setErrorMessage("Error verifying the customer. Please try again.");
      return false;
    }
  };

  const handleSubmit = async () => {
    setSuccessMessage("");
    setErrorMessage("");
    setLoading(true);

    if (!customerId.trim() || !customerFirstName.trim() || !customerLastName.trim()) {
      setErrorMessage("⚠️ Please enter a valid Customer ID, First Name, and Last Name.");
      setLoading(false);
      return;
    }

    const customerExists = await checkCustomerExists(customerId.trim());
    if (!customerExists) {
      setErrorMessage("⚠️ Invalid Customer ID. That customer does not exist.");
      setLoading(false);
      return;
    }

    if (!appointmentDate) {
      setErrorMessage("⚠️ No appointment date selected.");
      setLoading(false);
      return;
    }

    try {
      // ✅ Convert service names into a single comma-separated string
      const servicesString = services.map((service) => service.title).join(", ");

      // ✅ Ensure appointmentDate is in correct format
      const formattedDate = appointmentDate.includes("T") ? appointmentDate : `${appointmentDate}T12:00`;

      const payload = {
        customerId: customerId.trim(),
        customerFirstName: customerFirstName.trim(),
        customerLastName: customerLastName.trim(),
        appointmentDate: formattedDate,
        services: servicesString, // ✅ Send as a single string
        comments,
        status: "pending", // ✅ Convert to lowercase as required by backend
      };

      console.log("📢 Sending payload:", payload);

      const response = await axiosInstance.post(
        `/appointments/customers/${customerId.trim()}`,
        payload,
        { headers: { "Content-Type": "application/json" } }
      );

      console.log("✅ Response:", response.data);

      if (response.status === 200 || response.status === 201) {
        setSuccessMessage("✅ Appointment created successfully! Redirecting...");
        
        // ✅ Redirect user to `/services` after 2 seconds
        setTimeout(() => {
          navigate("/services");
        }, 2000);
      } else {
        throw new Error("❌ Failed to create appointment.");
      }
    } catch (err) {
      console.error("❌ Error creating appointment:", err);
      setErrorMessage("❌ Error creating appointment. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: "40px", maxWidth: "600px", margin: "0 auto", boxShadow: "0 2px 10px rgba(0,0,0,0.1)", borderRadius: "10px", backgroundColor: "#f9f9f9" }}>
      <h1 style={{ textAlign: "center", marginBottom: "20px" }}>Confirm Your Appointment</h1>

      <div style={{ marginBottom: "20px" }}>
        <h2>Selected Services</h2>
        {services.map((svc) => (
          <div key={svc.serviceId} style={{ padding: "10px 0", borderBottom: "1px solid #ddd" }}>
            <strong>{svc.title}</strong> - ${svc.pricing.toFixed(2)}
          </div>
        ))}
        {services.length === 0 && <p>No service details available. (Check your IDs or backend calls.)</p>}
      </div>

      <div style={{ marginBottom: "20px" }}>
        <h3>Date & Time:</h3>
        <p>{appointmentDate}</p>
      </div>

      <div style={{ marginBottom: "20px" }}>
        <label>Customer ID:</label>
        <input type="text" value={customerId} onChange={(e) => setCustomerId(e.target.value)} style={{ width: "100%", padding: "8px", borderRadius: "5px", border: "1px solid #ccc" }} placeholder="Enter your Customer ID" />
      </div>

      <div style={{ marginBottom: "20px" }}>
        <label>First Name:</label>
        <input type="text" value={customerFirstName} onChange={(e) => setCustomerFirstName(e.target.value)} style={{ width: "100%", padding: "8px", borderRadius: "5px", border: "1px solid #ccc" }} placeholder="Enter your First Name" />
      </div>

      <div style={{ marginBottom: "20px" }}>
        <label>Last Name:</label>
        <input type="text" value={customerLastName} onChange={(e) => setCustomerLastName(e.target.value)} style={{ width: "100%", padding: "8px", borderRadius: "5px", border: "1px solid #ccc" }} placeholder="Enter your Last Name" />
      </div>

      <div style={{ marginBottom: "20px" }}>
        <label>Comments:</label>
        <textarea value={comments} onChange={(e) => setComments(e.target.value)} style={{ width: "100%", padding: "8px", borderRadius: "5px", border: "1px solid #ccc" }} placeholder="Additional comments (optional)" />
      </div>

      {errorMessage && <p style={{ color: "red", textAlign: "center" }}>{errorMessage}</p>}
      {successMessage && <p style={{ color: "green", textAlign: "center" }}>{successMessage}</p>}

      <button
        onClick={handleSubmit}
        disabled={loading}
        style={{
          width: "100%",
          padding: "10px",
          backgroundColor: loading ? "#ccc" : "#28a745",
          color: "#fff",
          border: "none",
          borderRadius: "5px",
          cursor: loading ? "not-allowed" : "pointer",
          fontSize: "16px",
        }}
      >
        {loading ? "Confirming..." : "Confirm Appointment"}
      </button>
    </div>
  );
};

export default CheckoutPage;
