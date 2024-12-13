import React from 'react';
import Employees from '../../pages/Employees';
import CustomersList from '../customers/CustomersList';
import AdminFeedbackList from './AdminFeedbackList';
import './AdminDashboard.css';

const AdminDashboard: React.FC = () => {
  return (
    <div className="admin-dashboard">
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
        
        {/* Feedback Section */}
        <section className="feedback-section">
          <h2>Feedback</h2>
          <AdminFeedbackList />
        </section>
      </div>
    </div>
  );
};

export default AdminDashboard;
