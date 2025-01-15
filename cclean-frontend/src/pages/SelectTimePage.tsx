// src/pages/SelectTimePage.tsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const SelectTimePage: React.FC = () => {
  const [selectedTime, setSelectedTime] = useState<string>('');
  const navigate = useNavigate();

  const generateTimeSlots = () => {
    const slots: string[] = [];
    let start = 9;
    let end = 21;
    for (let hour = start; hour < end; hour++) {
      slots.push(`${hour}:00`);
      slots.push(`${hour}:30`);
    }
    return slots;
  };

  const proceedToConfirmation = () => {
    if (!selectedTime) {
      alert('Please select a time slot.');
      return;
    }

    localStorage.setItem('selectedTime', selectedTime);
    navigate('/confirm-booking');
  };

  return (
    <div style={{ textAlign: 'center', marginTop: '20px' }}>
      <h1>Select a Time Slot</h1>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: '10px', maxWidth: '600px', margin: 'auto' }}>
        {generateTimeSlots().map((time) => (
          <button
            key={time}
            onClick={() => setSelectedTime(time)}
            style={{
              padding: '10px',
              backgroundColor: selectedTime === time ? '#007BFF' : '#f0f0f0',
              color: selectedTime === time ? '#fff' : '#000',
              border: 'none',
              borderRadius: '4px',
              cursor: 'pointer'
            }}
          >
            {time}
          </button>
        ))}
      </div>
      <button onClick={proceedToConfirmation} style={{ marginTop: '20px' }}>
        Proceed to Confirmation
      </button>
    </div>
  );
};

export default SelectTimePage;
