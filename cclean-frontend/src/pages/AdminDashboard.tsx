import React from 'react';
import EmployeeCarousel from '../components/dashboards/EmployeeCarousel';
import Sidebar from '../components/dashboards/Sidebar';

const AdminDashboard = () => {
  return (
    <div className="flex">
      {/* Sidebar */}
      <Sidebar />

      {/* Main Content */}
      <div className="ml-64 p-6 w-full">
        <h1 className="text-xl font-bold mb-4">Admin Dashboard</h1>
        <EmployeeCarousel />
      </div>
    </div>
  );
};

export default AdminDashboard;
