import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../api/axios';
import './Home.css';

interface Feedback {
  feedbackId: string;
  customerId: string;
  content: string;
  stars: number;
  status: string;
}

interface Customer {
  customerId: string;
  firstName?: string;
  lastName?: string;
  companyName?: string;
  email: string;
  phoneNumber: string;
}

const Home = () => {
  const [feedbacks, setFeedbacks] = useState<Feedback[]>([]);
  const [customerMap, setCustomerMap] = useState<Record<string, Customer>>({});
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  const fetchFeedbacks = async () => {
    try {
      const response = await axiosInstance.get<Feedback[]>('/feedbacks?status=VISIBLE');
      const feedbackData = response.data;
      setFeedbacks(feedbackData);

      // Fetch customer details for each unique customerId
      const uniqueCustomerIds = Array.from(new Set(feedbackData.map(f => f.customerId)));
      const customerPromises = uniqueCustomerIds.map(id => axiosInstance.get<Customer>(`/customers/${id}`));

      const customerResponses = await Promise.all(customerPromises);
      const customerInfo: Record<string, Customer> = {};
      customerResponses.forEach(c => {
        customerInfo[c.data.customerId] = c.data;
      });
      setCustomerMap(customerInfo);
    } catch (err) {
      setError(
        err instanceof Error
          ? err.message
          : 'Failed to fetch feedbacks. Please try again later.'
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchFeedbacks();
  }, []);

  const renderStars = (rating: number) => {
    const stars = [];
    for (let i = 1; i <= 5; i++) {
      stars.push(
        <span key={i} className={`star ${i <= rating ? 'filled' : ''}`}>
          ★
        </span>
      );
    }
    return <div className="star-container">{stars}</div>;
  };

  const getCustomerName = (customerId: string): string => {
    const customer = customerMap[customerId];
    if (!customer) return customerId;
    if (customer.firstName && customer.lastName) {
      return `${customer.firstName} ${customer.lastName}`;
    } else if (customer.companyName) {
      return customer.companyName;
    }
    return customerId;
  };

  return (
    <div className="home-container">
      <header className="home-header">
        <div className="home-header-content">
          <h1 className="home-title">C CLEAN inc.</h1>
          <p className="home-description">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
            eiusmod tempor incididunt ut labore et dolore magna aliqua ut enim ad.
          </p>
          <button
            onClick={() => navigate('/about-us')}
            className="home-learn-more-button"
          >
            Learn More
          </button>
        </div>

        <div className="home-image-container">
          <img
            src="/images/cleaning-product.png"
            alt="Cleaning Product"
          />
        </div>
      </header>

      <section className="feedback-section">
        <h2 className="feedback-section-title">Customer Feedbacks</h2>
        <button
          onClick={() => navigate('/submit-feedback')}
          className="home-submit-feedback-button"
        >
          Submit Feedback
        </button>
        {loading ? (
          <p className="loading-text">Loading feedbacks...</p>
        ) : error ? (
          <p className="error-text">{error}</p>
        ) : feedbacks.length > 0 ? (
          <div className="feedback-grid">
            {feedbacks.map((feedback) => (
              <div key={feedback.feedbackId} className="feedback-card">
                <p className="feedback-content">"{feedback.content}"</p>
                <div className="star-rating-container">
                  {renderStars(feedback.stars)}
                  <span className="rating-text">{feedback.stars}/5</span>
                </div>
                <p className="feedback-user">Customer: {getCustomerName(feedback.customerId)}</p>
                {/* Removed status display for homepage */}
              </div>
            ))}
          </div>
        ) : (
          <p className="loading-text">No feedback available at the moment.</p>
        )}
      </section>
    </div>
  );
};

export default Home;
