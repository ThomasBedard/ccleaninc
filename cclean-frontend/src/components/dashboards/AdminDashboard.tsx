import React from 'react';
// import Sidebar from '../components/dashboards/Sidebar';
import Employees from '../../pages/Employees';
import CustomersList from '../customers/CustomersList';
import './AdminDashboard.css';

const AdminDashboard: React.FC = () => {
  return (
    <div className="admin-dashboard">
      {/* Sidebar can be uncommented and styled if needed */}
      {/* <Sidebar /> */}
      <div className="main-content">
        <h1 className="dashboard-title">Admin Dashboard</h1>
        
        {/* Employees Section */}
        <section className="employees-section">
          <h2>Employees</h2>
          <Employees />
        </section>

        {/* Customers Section */}
        <section className="customers-section">
          <h2>Customers</h2>
          <CustomersList />
        </section>
      </div>
    </div>
  );
};

export default AdminDashboard;
