import React from 'react';
import Sidebar from '../components/dashboards/Sidebar';
import Employees from './Employees';

const AdminDashboard = () => {
  return (
    <div className="flex h-screen">
      <Sidebar />
      <div className="flex-1 p-6">
        <h1 className="text-3xl font-bold mb-4">Admin Dashboard</h1>
        <Employees />
      </div>
    </div>
  );
};

export default AdminDashboard;
