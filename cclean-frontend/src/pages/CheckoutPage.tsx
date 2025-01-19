import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import axiosInstance from "../api/axios";

interface Service {
  serviceId: string;
  title: string;
  description: string;
  pricing: number;
  isAvailable: boolean;
}

const CheckoutPage: React.FC = () => {
  const location = useLocation();
  const { selectedServiceIds, appointmentDate } = location.state as {
    selectedServiceIds: string[];
    appointmentDate: string;
  };

  const [services, setServices] = useState<Service[]>([]);
  const [customerId, setCustomerId] = useState("");
  const [comments, setComments] = useState("");
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
        console.error("Error fetching services:", err);
      }
    };

    if (selectedServiceIds?.length) {
      fetchServices();
    }
  }, [selectedServiceIds]);

  const checkCustomerExists = async (id: string): Promise<boolean> => {
    try {
      const res = await axiosInstance.get(`/customers/${id}`);
      return res.status === 200;
    } catch {
      console.error("Error verifying customer.");
      setErrorMessage("Error verifying the customer. Please try again.");
      return false;
    }
  };

  const handleSubmit = async () => {
    setSuccessMessage("");
    setErrorMessage("");

    if (!customerId.trim()) {
      setErrorMessage("Please enter a valid customer ID.");
      return;
    }

    const customerExists = await checkCustomerExists(customerId.trim());
    if (!customerExists) {
      setErrorMessage("Invalid customer ID. That customer does not exist.");
      return;
    }

    if (!appointmentDate) {
      setErrorMessage("No appointment date selected.");
      return;
    }

    try {
      const servicesString = selectedServiceIds.join(",");

      const payload = {
        customerId: customerId.trim(),
        appointmentDate,
        services: servicesString,
        comments,
        status: "pending",
      };

      const response = await axiosInstance.post(
        "/appointments/with-customerid",
        payload
      );
      if (response.status === 201) {
        setSuccessMessage(
          `Appointment created successfully! Appointment ID: ${response.data.appointmentId}`
        );
      } else {
        setErrorMessage("Failed to create appointment. Please try again.");
      }
    } catch (err) {
      console.error("Error creating appointment:", err);
      setErrorMessage("Error creating appointment. Please try again.");
    }
  };

  return (
    <div
      style={{
        padding: "40px",
        maxWidth: "600px",
        margin: "0 auto",
        boxShadow: "0 2px 10px rgba(0,0,0,0.1)",
        borderRadius: "10px",
        backgroundColor: "#f9f9f9",
      }}
    >
      <h1 style={{ textAlign: "center", marginBottom: "20px" }}>
        Confirm Your Appointment
      </h1>
      <div style={{ marginBottom: "20px" }}>
        <h2>Selected Services</h2>
        {services.map((svc) => (
          <div
            key={svc.serviceId}
            style={{ padding: "10px 0", borderBottom: "1px solid #ddd" }}
          >
            <strong>{svc.title}</strong> - ${svc.pricing.toFixed(2)}
          </div>
        ))}
        {services.length === 0 && (
          <p>
            No service details available. (Check your IDs or backend calls.)
          </p>
        )}
      </div>

      <div style={{ marginBottom: "20px" }}>
        <h3>Date & Time:</h3>
        <p>{appointmentDate}</p>
      </div>

      <div style={{ marginBottom: "20px" }}>
        <label>Customer ID:</label>
        <input
          type="text"
          value={customerId}
          onChange={(e) => setCustomerId(e.target.value)}
          style={{
            width: "100%",
            padding: "8px",
            borderRadius: "5px",
            border: "1px solid #ccc",
          }}
          placeholder="Enter your Customer ID"
        />
      </div>

      <div style={{ marginBottom: "20px" }}>
        <label>Comments:</label>
        <textarea
          value={comments}
          onChange={(e) => setComments(e.target.value)}
          style={{
            width: "100%",
            padding: "8px",
            borderRadius: "5px",
            border: "1px solid #ccc",
          }}
          placeholder="Additional comments (optional)"
        />
      </div>

      {errorMessage && (
        <p style={{ color: "red", textAlign: "center" }}>{errorMessage}</p>
      )}
      {successMessage && (
        <p style={{ color: "green", textAlign: "center" }}>{successMessage}</p>
      )}

      <button
        onClick={handleSubmit}
        style={{
          width: "100%",
          padding: "10px",
          backgroundColor: "#28a745",
          color: "#fff",
          border: "none",
          borderRadius: "5px",
          cursor: "pointer",
          fontSize: "16px",
        }}
      >
        Confirm Appointment
      </button>
    </div>
  );
};

export default CheckoutPage;
