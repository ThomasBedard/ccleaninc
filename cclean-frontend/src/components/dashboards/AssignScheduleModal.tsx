import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import axiosInstance from '../../api/axios';
import './AssignScheduleModal.css';

const AssignScheduleModal: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const availability = location.state;

  const [assignedDate, setAssignedDate] = useState(
    availability?.availableDate || ''
  );
  const [shift, setShift] = useState(availability?.shift || 'MORNING');
  const [status, setStatus] = useState('PENDING');
  const [comments, setComments] = useState('');

  const handleAssign = async () => {
    const payload = {
      employeeId: availability?.employeeId,
      availabilityId: availability?.availabilityId,
      assignedDate: new Date(assignedDate).toISOString().slice(0, 16),
      shift: shift.toUpperCase(),
      status: status.toUpperCase(),
      comments: comments,
    };

    try {
      await axiosInstance.post('/schedules', payload);
      alert('Schedule assigned successfully!');
      navigate('/admin-dashboard', { state: { activeTab: 'employeeAvailabilities' } });
    } catch (error) {
      console.error('Failed to assign schedule:', error);
      alert('Failed to assign schedule. Please try again later.');
    }
  };

  const handleClose = () => {
    navigate(-1);
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <div className="modal-header">
          <h2>Assign Schedule</h2>
          <button className="close-button" onClick={handleClose}>&times;</button>
        </div>
        <div className="modal-form">
          <label>Assigned Date & Time:</label>
          <input
            type="datetime-local"
            value={assignedDate}
            onChange={(e) => setAssignedDate(e.target.value)}
          />

          <label>Shift:</label>
          <input
            type="text"
            value={shift}
            onChange={(e) => setShift(e.target.value)}
          />

          <label>Status:</label>
          <select value={status} onChange={(e) => setStatus(e.target.value)}>
            <option value="PENDING">Pending</option>
            <option value="APPROVED">Approved</option>
            <option value="REJECTED">Rejected</option>
          </select>

          <label>Comments (Optional):</label>
          <textarea
            value={comments}
            onChange={(e) => setComments(e.target.value)}
            placeholder="Add any comments here"
          />

          <div className="modal-buttons">
            <button className="assign-button" onClick={handleAssign}>Assign Schedule</button>
            <button className="cancel-button" onClick={handleClose}>Cancel</button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AssignScheduleModal;