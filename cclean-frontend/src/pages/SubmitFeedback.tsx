import React, { useState } from "react";
import { useAxiosWithAuth } from "../api/axios"; // Use the custom hook
import { useNavigate } from "react-router-dom";
import "./SubmitFeedback.css";
import { toast } from "react-toastify";
import axios, { AxiosError } from "axios";

const SubmitFeedback: React.FC = () => {
  const axiosAuth = useAxiosWithAuth(); // Get the axios instance with the auth token attached
  const [stars, setStars] = useState<number>(0);
  const [content, setContent] = useState("");
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    if (stars < 1 || stars > 5) {
      setError("Stars must be between 1 and 5.");
      return;
    }

    if (!content.trim()) {
      setError("Feedback content is required.");
      return;
    }

    try {
      // Using the axios instance from useAxiosWithAuth so that the token is attached
      await axiosAuth.post("/feedbacks", {
        stars: stars,
        content: content,
      });

      toast.success(
        "Feedback submitted successfully! It will be visible once an admin publishes it."
      );
      alert(
        "Feedback submitted successfully! It will be visible once an admin publishes it."
      );
      navigate("/");
    } catch (err) {
      if (axios.isAxiosError(err)) {
        const error = err as AxiosError;
        if (error.response && error.response.status === 400) {
          setError("Invalid input. Please try again.");
        } else {
          setError("Failed to submit feedback. Please try again later.");
        }
      } else {
        setError("Failed to submit feedback. Please try again later.");
      }
    }
  };

  return (
    <div className="feedback-form-container">
      <h1>Submit Feedback</h1>
      {error && <p className="error-message">{error}</p>}
      <form onSubmit={handleSubmit}>
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
        <button type="submit" className="submit-feedback-button">
          Submit
        </button>
      </form>
    </div>
  );
};

export default SubmitFeedback;
