// import React from 'react';
// // import Sidebar from '../components/dashboards/Sidebar';
// import Employees from '../../pages/Employees';
// import CustomersList from '../customers/CustomersList';
// import './AdminDashboard.css';

// const AdminDashboard: React.FC = () => {
//   return (
//     <div className="admin-dashboard">
//       {/* Sidebar can be uncommented and styled if needed */}
//       {/* <Sidebar /> */}
//       <div className="main-content">
//         <h1 className="dashboard-title">Admin Dashboard</h1>
        
//         {/* Employees Section */}
//         <section className="employees-section">
//           <h2>Employees</h2>
//           <Employees />
//         </section>

//         {/* Customers Section */}
//         <section className="customers-section">
//           <h2>Customers</h2>
//           <CustomersList />
//         </section>
//       </div>
//     </div>
//   );
// };

// export default AdminDashboard;


import React, { useState } from 'react';


import './AdminDashboard.css'; 
import Employees from '../../pages/Employees';
import Services from '../../pages/Services';
import CustomersList from '../customers/CustomersList';

const AdminDashboard: React.FC = () => {
  const [activeTab, setActiveTab] = useState('employees'); // Default active tab

  return (
    <div className="admin-dashboard-container">
      <h1>Admin Dashboard</h1>

      {/* Tabs */}
      <div className="tabs-container">
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
      </div>

      {/* Tab Content */}
      <div className="tab-content">
        {activeTab === 'employees' && <Employees />}
        {activeTab === 'customers' && <CustomersList />}
        {activeTab === 'services' && <Services />}
      </div>
    </div>
  );
};

export default AdminDashboard;
