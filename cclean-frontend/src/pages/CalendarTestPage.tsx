import React from 'react';
import SimpleCalendar from '../components/SimpleCalendar';

const CalendarTestPage: React.FC = () => {
  return (
    <div>
      <h1 style={{ textAlign: 'center' }}>Calendar Test Page</h1>
      <SimpleCalendar />
    </div>
  );
};

export default CalendarTestPage;
