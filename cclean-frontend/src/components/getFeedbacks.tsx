import axiosInstance from "../api/axios";

export const getFeedbacks = async () => {
  try {
    const response = await axiosInstance.get("/feedbacks"); // No need to specify the full URL
    return response.data;
  } catch (error) {
    console.error("Error fetching feedbacks:", error);
    throw error;
  }
};
