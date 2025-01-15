// src/pages/ConfirmBookingPage.tsx
import React, { useState, useEffect } from 'react';
import axiosInstance from '../api/axios';
import { useNavigate } from 'react-router-dom';

const ConfirmBookingPage: React.FC = () => {
  const [selectedServices, setSelectedServices] = useState<string[]>([]);
  const [selectedDate, setSelectedDate] = useState<string>('');
  const [selectedTime, setSelectedTime] = useState<string>('');
  const navigate = useNavigate();

  useEffect(() => {
    const services = JSON.parse(localStorage.getItem('selectedServices') || '[]');
    const date = localStorage.getItem('selectedDate') || '';
    const time = localStorage.getItem('selectedTime') || '';
    setSelectedServices(services);
    setSelectedDate(date);
    setSelectedTime(time);
  }, []);

  const confirmBooking = async () => {
    const customerId = prompt('Enter your Customer ID to confirm booking:');

    if (!customerId) {
      alert('Customer ID is required.');
      return;
    }

    try {
      await axiosInstance.post('/api/v1/booking/confirm', null, {
        params: { customerId }
      });
      alert('Booking confirmed!');
      localStorage.clear();
      navigate('/appointments');
    } catch (error) {
      alert('Failed to confirm booking. Please try again.');
    }
  };

  return (
    <div style={{ textAlign: 'center', marginTop: '20px' }}>
      <h1>Confirm Your Booking</h1>
      <h3>Selected Services:</h3>
      <ul>
        {selectedServices.map((serviceId, index) => (
          <li key={index}>{serviceId}</li>
        ))}
      </ul>
      <h3>Date:</h3>
      <p>{selectedDate}</p>
      <h3>Time:</h3>
      <p>{selectedTime}</p>
      <button onClick={confirmBooking}>Confirm Booking</button>
    </div>
  );
};

export default ConfirmBookingPage;
