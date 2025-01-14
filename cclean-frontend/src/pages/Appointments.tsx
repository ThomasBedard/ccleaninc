import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../api/axios";
import "./Appointments.css";

interface Appointment {
  appointmentId?: string;
  customerId?: string;
  customerFirstName?: string;
  customerLastName?: string;
  appointmentDate?: string;
  services?: string; // e.g. "id1,id2,id3" or "Residential Cleaning Service"
  serviceTitles?: string; // We'll add this property after we fetch names
  status?: string;
  comments?: string;
}

// Add a helper for checking UUID format
function isUuidFormat(str: string): boolean {
  return /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i.test(
    str
  );
}

const Appointments = () => {
  return (
    <div className="min-h-screen flex items-center justify-center">
      <h1 className="text-3xl font-bold">Appointments Page</h1>
    </div>
  );
};

export default Appointments;
