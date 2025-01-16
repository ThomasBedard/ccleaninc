<<<<<<< HEAD
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
=======
import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Calendar } from 'rsuite';
import 'rsuite/dist/rsuite.min.css';

const times = [
  '09:00', '09:30', '10:00', '10:30', 
  '11:00', '11:30', '12:00', '12:30',
  '13:00', '13:30', '14:00', '14:30',
  '15:00', '15:30', '16:00', '16:30',
  '17:00', '17:30', '18:00', '18:30',
  '19:00', '19:30', '20:00', '20:30',
>>>>>>> 5876d42 (feat(CCICC-15): Implement multiple service selection flow)
];

const SelectDateTimePage: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
<<<<<<< HEAD
  const { selectedServiceIds } = location.state as {
    selectedServiceIds: string[];
  };

  const [selectedDate, setSelectedDate] = useState<Date | null>(null);
  const [selectedTime, setSelectedTime] = useState<string>("");
=======
  const { selectedServiceIds } = location.state as { selectedServiceIds: string[] };

  const [selectedDate, setSelectedDate] = useState<Date | null>(null);
  const [selectedTime, setSelectedTime] = useState<string>('');
>>>>>>> 5876d42 (feat(CCICC-15): Implement multiple service selection flow)

  const handleDateChange = (date: Date) => setSelectedDate(date);
  const handleTimeSelect = (time: string) => setSelectedTime(time);

  const handleNext = () => {
    if (!selectedDate || !selectedTime) {
<<<<<<< HEAD
      alert("Please select both a date and a time.");
=======
      alert('Please select both a date and a time.');
>>>>>>> 5876d42 (feat(CCICC-15): Implement multiple service selection flow)
      return;
    }

    const year = selectedDate.getFullYear();
<<<<<<< HEAD
    const month = String(selectedDate.getMonth() + 1).padStart(2, "0");
    const day = String(selectedDate.getDate()).padStart(2, "0");
    const dateString = `${year}-${month}-${day}T${selectedTime}`;

    navigate("/checkout", {
      state: { selectedServiceIds, appointmentDate: dateString },
=======
    const month = String(selectedDate.getMonth() + 1).padStart(2, '0');
    const day = String(selectedDate.getDate()).padStart(2, '0');
    const dateString = `${year}-${month}-${day}T${selectedTime}`;

    navigate('/checkout', {
      state: { selectedServiceIds, appointmentDate: dateString }
>>>>>>> 5876d42 (feat(CCICC-15): Implement multiple service selection flow)
    });
  };

  return (
<<<<<<< HEAD
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
=======
    <div style={{ textAlign: 'center', marginTop: '20px' }}>
      <h1>Select Date &amp; Time</h1>
      <div style={{ display: 'inline-block', padding: '10px', boxShadow: '0 2px 8px rgba(0,0,0,0.1)', borderRadius: '8px' }}>
        <Calendar onSelect={handleDateChange} bordered style={{ width: '300px' }} />
        {selectedDate && (
          <div style={{ marginTop: '10px' }}>
            Selected Date: {selectedDate.toLocaleDateString('en-US')}
>>>>>>> 5876d42 (feat(CCICC-15): Implement multiple service selection flow)
          </div>
        )}
      </div>

<<<<<<< HEAD
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
=======
      <div style={{ marginTop: '20px' }}>
        <h2>Select Time</h2>
        <div style={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'center', gap: '8px' }}>
>>>>>>> 5876d42 (feat(CCICC-15): Implement multiple service selection flow)
          {times.map((time) => (
            <button
              key={time}
              onClick={() => handleTimeSelect(time)}
              style={{
<<<<<<< HEAD
                padding: "8px 12px",
                borderRadius: "5px",
                border: "1px solid #ccc",
                backgroundColor: selectedTime === time ? "#007BFF" : "#fff",
                color: selectedTime === time ? "#fff" : "#000",
                cursor: "pointer",
                minWidth: "60px",
=======
                padding: '8px 12px',
                borderRadius: '5px',
                border: '1px solid #ccc',
                backgroundColor: selectedTime === time ? '#007BFF' : '#fff',
                color: selectedTime === time ? '#fff' : '#000',
                cursor: 'pointer',
                minWidth: '60px'
>>>>>>> 5876d42 (feat(CCICC-15): Implement multiple service selection flow)
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
<<<<<<< HEAD
          marginTop: "20px",
          padding: "10px 20px",
          backgroundColor: "#28a745",
          color: "#fff",
          border: "none",
          borderRadius: "5px",
          cursor: "pointer",
          fontSize: "16px",
=======
          marginTop: '20px',
          padding: '10px 20px',
          backgroundColor: '#28a745',
          color: '#fff',
          border: 'none',
          borderRadius: '5px',
          cursor: 'pointer',
          fontSize: '16px'
>>>>>>> 5876d42 (feat(CCICC-15): Implement multiple service selection flow)
        }}
      >
        Next
      </button>
    </div>
  );
};

<<<<<<< HEAD
export default SelectDateTimePage;
=======
export default SelectDateTimePage;
>>>>>>> 5876d42 (feat(CCICC-15): Implement multiple service selection flow)
