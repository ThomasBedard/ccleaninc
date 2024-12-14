import React, { useState } from 'react';
import './AdminDashboard.css';
import Employees from '../../pages/Employees';
import CustomersList from '../customers/CustomersList';
import Services from '../../pages/Services';
import AdminFeedbackList from './AdminFeedbackList';


const AdminDashboard: React.FC = () => {
  const [activeTab, setActiveTab] = useState<string>('employees');

  const renderContent = () => {
    switch (activeTab) {
      case 'employees':
        return <Employees />;
      case 'customers':
        return <CustomersList />;
      case 'services':
        return <Services />;
      case 'feedbacks':
        return <AdminFeedbackList />;
      default:
        return null;
    }
  };

  return (
    <div className="admin-dashboard">
      <h1>Admin Dashboard</h1>
      <div className="tab-navigation">
        <button
          className={`tab-button ${activeTab === 'employees' ? 'active' : ''}`}
          onClick={() => setActiveTab('employees')}
        >
          Employees
        </button>
        <button
          className={`tab-button ${activeTab === 'customers' ? 'active' : ''}`}
          onClick={() => setActiveTab('customers')}
        >
          Customers
        </button>
        <button
          className={`tab-button ${activeTab === 'services' ? 'active' : ''}`}
          onClick={() => setActiveTab('services')}
        >
          Services
        </button>
        <button
          className={`tab-button ${activeTab === 'feedbacks' ? 'active' : ''}`}
          onClick={() => setActiveTab('feedbacks')}
        >
          Feedbacks
        </button>
      </div>
      {renderContent()}
    </div>
  );
};

export default AdminDashboard;
