import React, { useState } from 'react';
import { Calendar } from 'rsuite';
import 'rsuite/dist/rsuite.min.css';

const SimpleCalendar: React.FC = () => {
  const [selectedDate, setSelectedDate] = useState<Date | null>(null);

  const handleDateChange = (date: Date) => {
    setSelectedDate(date);
  };

  return (
    <div style={{ display: 'flex', justifyContent: 'center', marginTop: '20px' }}>
      <div style={{ width: '400px' }}> {/* Set a fixed width for better layout */}
        <Calendar onSelect={handleDateChange} bordered />
        {selectedDate && (
          <div style={{ marginTop: '10px', textAlign: 'center' }}>
            Selected Date: {selectedDate.toLocaleDateString('en-US')}
          </div>
        )}
      </div>
    </div>
  );
};

export default SimpleCalendar;
