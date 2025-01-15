// src/pages/SelectDatePage.tsx
import React, { useState } from 'react';
import { Calendar } from 'rsuite';
import 'rsuite/dist/rsuite.min.css';
import { useNavigate } from 'react-router-dom';

const SelectDatePage: React.FC = () => {
  const [selectedDate, setSelectedDate] = useState<Date | null>(null);
  const navigate = useNavigate();

  const handleDateSelect = (date: Date) => {
    setSelectedDate(date);
  };

  const proceedToTimeSelection = () => {
    if (!selectedDate) {
      alert('Please select a date to continue.');
      return;
    }

    localStorage.setItem('selectedDate', selectedDate.toISOString().split('T')[0]);
    navigate('/select-time');
  };

  return (
    <div style={{ textAlign: 'center', marginTop: '20px' }}>
      <h1>Select a Date for Your Services</h1>
      <Calendar onSelect={handleDateSelect} bordered />
      {selectedDate && (
        <div style={{ marginTop: '10px' }}>
          Selected Date: {selectedDate.toLocaleDateString('en-US')}
        </div>
      )}
      <button onClick={proceedToTimeSelection} style={{ marginTop: '20px' }}>
        Proceed to Time Selection
      </button>
    </div>
  );
};

export default SelectDatePage;
