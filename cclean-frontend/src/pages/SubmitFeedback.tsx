import React, { useState } from 'react';
import axios, { AxiosError } from 'axios';
import axiosInstance from '../api/axios';
import { useNavigate } from 'react-router-dom';
import './SubmitFeedback.css';
import { toast } from 'react-toastify';

const SubmitFeedback: React.FC = () => {
  const [customerId, setCustomerId] = useState('');
  const [stars, setStars] = useState<number>(0);
  const [content, setContent] = useState('');
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    if (!customerId.trim()) {
      setError('Customer ID is required.');
      return;
    }

    if (stars < 1 || stars > 5) {
      setError('Stars must be between 1 and 5.');
      return;
    }

    if (!content.trim()) {
      setError('Feedback content is required.');
      return;
    }

    try {
      await axiosInstance.post('/feedbacks', {
        customerId: customerId, // Changed from userId to customerId
        stars: stars,
        content: content
      });
      toast.success('Feedback submitted successfully! It will be visible once an admin publishes it.');
      navigate('/');
    } catch (err) {
      if (axios.isAxiosError(err)) {
        const error = err as AxiosError;
        if (error.response && error.response.status === 400) {
          setError('Invalid customer ID. Please ensure this customer exists.');
        } else {
          setError('Failed to submit feedback. Please try again later.');
        }
      } else {
        setError('Failed to submit feedback. Please try again later.');
      }
    }
  };

  return (
    <div className="feedback-form-container">
      <h1>Submit Feedback</h1>
      {error && <p className="error-message">{error}</p>}
      <form onSubmit={handleSubmit}>
        <label>
          Customer ID:
          <input 
            type="text" 
            value={customerId} 
            onChange={(e) => setCustomerId(e.target.value)}
            required 
          />
        </label>
        <label>
          Stars (1-5):
          <input 
            type="number" 
            value={stars} 
            onChange={(e) => setStars(Number(e.target.value))} 
            min={1} 
            max={5}
            required 
          />
        </label>
        <label>
          Feedback:
          <textarea 
            value={content} 
            onChange={(e) => setContent(e.target.value)} 
            required 
          />
        </label>
        <button type="submit" className="submit-feedback-button">Submit</button>
      </form>
    </div>
  );
};

export default SubmitFeedback;
