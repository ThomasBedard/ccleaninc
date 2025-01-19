import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { Calendar } from "rsuite";
import "rsuite/dist/rsuite.min.css";

const times = [
  "09:00",
  "09:30",
  "10:00",
  "10:30",
  "11:00",
  "11:30",
  "12:00",
  "12:30",
  "13:00",
  "13:30",
  "14:00",
  "14:30",
  "15:00",
  "15:30",
  "16:00",
  "16:30",
  "17:00",
  "17:30",
  "18:00",
  "18:30",
  "19:00",
  "19:30",
  "20:00",
  "20:30",
];

const SelectDateTimePage: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { selectedServiceIds } = location.state as {
    selectedServiceIds: string[];
  };

  const [selectedDate, setSelectedDate] = useState<Date | null>(null);
  const [selectedTime, setSelectedTime] = useState<string>("");

  const handleDateChange = (date: Date) => setSelectedDate(date);
  const handleTimeSelect = (time: string) => setSelectedTime(time);

  const handleNext = () => {
    if (!selectedDate || !selectedTime) {
      alert("Please select both a date and a time.");
      return;
    }

    const year = selectedDate.getFullYear();
    const month = String(selectedDate.getMonth() + 1).padStart(2, "0");
    const day = String(selectedDate.getDate()).padStart(2, "0");
    const dateString = `${year}-${month}-${day}T${selectedTime}`;

    navigate("/checkout", {
      state: { selectedServiceIds, appointmentDate: dateString },
    });
  };

  return (
    <div style={{ textAlign: "center", marginTop: "20px" }}>
      <h1>Select Date &amp; Time</h1>
      <div
        style={{
          display: "inline-block",
          padding: "10px",
          boxShadow: "0 2px 8px rgba(0,0,0,0.1)",
          borderRadius: "8px",
        }}
      >
        <Calendar
          onSelect={handleDateChange}
          bordered
          style={{ width: "300px" }}
        />
        {selectedDate && (
          <div style={{ marginTop: "10px" }}>
            Selected Date: {selectedDate.toLocaleDateString("en-US")}
          </div>
        )}
      </div>

      <div style={{ marginTop: "20px" }}>
        <h2>Select Time</h2>
        <div
          style={{
            display: "flex",
            flexWrap: "wrap",
            justifyContent: "center",
            gap: "8px",
          }}
        >
          {times.map((time) => (
            <button
              key={time}
              onClick={() => handleTimeSelect(time)}
              style={{
                padding: "8px 12px",
                borderRadius: "5px",
                border: "1px solid #ccc",
                backgroundColor: selectedTime === time ? "#007BFF" : "#fff",
                color: selectedTime === time ? "#fff" : "#000",
                cursor: "pointer",
                minWidth: "60px",
              }}
            >
              {time}
            </button>
          ))}
        </div>
      </div>

      <button
        onClick={handleNext}
        style={{
          marginTop: "20px",
          padding: "10px 20px",
          backgroundColor: "#28a745",
          color: "#fff",
          border: "none",
          borderRadius: "5px",
          cursor: "pointer",
          fontSize: "16px",
        }}
      >
        Next
      </button>
    </div>
  );
};

export default SelectDateTimePage;
