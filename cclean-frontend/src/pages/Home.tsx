import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../api/axios';
import './Home.css';

interface Feedback {
  feedback_id: string;
  content: string;
  user_id: string;
  stars: number;
  status: string;
}

const Home = () => {
  const [feedbacks, setFeedbacks] = useState<Feedback[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const navigate = useNavigate();

  useEffect(() => {
    const fetchFeedbacks = async () => {
      try {
        const response = await axiosInstance.get<Feedback[]>('/feedbacks');
        setFeedbacks(response.data);
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
        {loading ? (
          <p className="loading-text">Loading feedbacks...</p>
        ) : error ? (
          <p className="error-text">{error}</p>
        ) : feedbacks.length > 0 ? (
          <div className="feedback-grid">
            {feedbacks.map((feedback) => (
              <div key={feedback.feedback_id} className="feedback-card">
                <p className="feedback-content">"{feedback.content}"</p>
                <div className="star-rating-container">
                  {renderStars(feedback.stars)}
                  <span className="rating-text">{feedback.stars}/5</span>
                </div>
                <p className="feedback-user">User ID: {feedback.user_id}</p>
                <p
                  className={`feedback-status ${
                    feedback.status === 'VISIBLE' ? 'visible' : 'invisible'
                  }`}
                >
                  Status: {feedback.status}
                </p>
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
