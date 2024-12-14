import React, { useEffect, useState } from 'react';
import axiosInstance from '../../api/axios';
import './AdminFeedbackList.css';

interface Feedback {
  feedbackId: string;
  customerId: string;
  stars: number;
  content: string;
  status: string;
}

const AdminFeedbackList: React.FC = () => {
  const [feedbacks, setFeedbacks] = useState<Feedback[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchAllFeedbacks = async () => {
    try {
      const response = await axiosInstance.get<Feedback[]>('/feedbacks');
      setFeedbacks(response.data);
    } catch (err) {
      setError(
        err instanceof Error
          ? err.message
          : 'Failed to fetch feedback. Please try again later.'
      );
    } finally {
      setLoading(false);
    }
  };

  const toggleFeedbackVisibility = async (feedbackId: string, currentStatus: string) => {
    const newStatus = currentStatus === 'VISIBLE' ? 'INVISIBLE' : 'VISIBLE';
    try {
      await axiosInstance.patch(`/feedbacks/${feedbackId}/publish`, newStatus, {
        headers: { 'Content-Type': 'text/plain' }
      });
      // Update local state
      setFeedbacks((prevFeedbacks) =>
        prevFeedbacks.map((f) =>
          f.feedbackId === feedbackId ? { ...f, status: newStatus } : f
        )
      );
    } catch (error) {
      console.error('Error updating feedback visibility:', error);
      alert('Failed to update feedback visibility.');
    }
  };

  useEffect(() => {
    fetchAllFeedbacks();
  }, []);

  if (loading) return <p>Loading feedbacks...</p>;
  if (error) return <p className="error-text">{error}</p>;

  return (
    <div className="admin-feedback-container">
      <h3>All Feedback</h3>
      {feedbacks.length === 0 ? (
        <p>No feedback submissions found.</p>
      ) : (
        <table className="feedback-table">
          <thead>
            <tr>
            <th>Customer ID</th>
              <th>Stars</th>
              <th>Content</th>
              <th>Status</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {feedbacks.map((feedback) => (
              <tr key={feedback.feedbackId}>
                <td>{feedback.customerId}</td>
                <td>{feedback.stars}</td>
                <td>{feedback.content}</td>
                <td>{feedback.status}</td>
                <td>
                  <button
                    className="toggle-status-button"
                    onClick={() => toggleFeedbackVisibility(feedback.feedbackId, feedback.status)}
                  >
                    {feedback.status === 'VISIBLE' ? 'Make Invisible' : 'Make Visible'}
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default AdminFeedbackList;
