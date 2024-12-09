import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../api/axios';

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

  return (
    <div className="bg-gradient-to-b from-[#E4EDF5] to-[#F7F8FC] min-h-screen relative">
      <header className="flex flex-col lg:flex-row justify-between items-center py-20 px-10 lg:px-0">
        <div className="max-w-lg z-10 lg:pl-10">
          <h1 className="text-5xl font-bold text-gray-900">C CLEAN inc.</h1>
          <p className="mt-6 text-lg text-gray-600">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
            eiusmod tempor incididunt ut labore et dolore magna aliqua ut enim ad.
          </p>
          <button
            onClick={() => navigate('/about-us')}
            className="mt-6 px-6 py-3 bg-blue-500 text-white rounded-lg shadow-md hover:bg-blue-600"
          >
            Learn More
          </button>
        </div>
        <div className="absolute inset-y-0 right-0 w-full lg:w-1/2 flex justify-end">
          <img
            src="/images/cleaning-product.png"
            alt="Cleaning Product"
            className="h-full object-contain lg:object-cover"
          />
        </div>
      </header>

      {/* Feedback Section */}
      <section className="p-10">
        <h2 className="text-3xl font-bold mb-6">Customer Feedbacks</h2>
        {loading ? (
          <p>Loading feedbacks...</p>
        ) : error ? (
          <p className="text-red-500">{error}</p>
        ) : feedbacks.length > 0 ? (
          <ul className="space-y-4">
            {feedbacks.map((feedback) => (
              <li
                key={feedback.feedback_id}
                className="p-4 border rounded-lg shadow-sm"
              >
                <p className="text-lg font-semibold">{feedback.content}</p>
                <p className="text-sm text-gray-500">Rating: {feedback.stars}/5</p>
                <p className="text-sm text-gray-500">User ID: {feedback.user_id}</p>
                <p
                  className={`text-sm mt-2 ${
                    feedback.status === 'VISIBLE' ? 'text-green-600' : 'text-red-600'
                  }`}
                >
                  Status: {feedback.status}
                </p>
              </li>
            ))}
          </ul>
        ) : (
          <p>No feedback available at the moment.</p>
        )}
      </section>
    </div>
  );
};

export default Home;
