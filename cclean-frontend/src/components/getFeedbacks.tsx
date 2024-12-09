import axios from 'axios';

const API_URL = 'http://localhost:8080/api/v1/feedbacks';

export const getFeedbacks = async () => {
  try {
    const response = await axios.get(API_URL);
    return response.data;
  } catch (error) {
    console.error('Error fetching feedbacks:', error);
    throw error;
  }
};
