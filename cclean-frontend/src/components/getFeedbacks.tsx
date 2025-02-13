import { useAxiosWithAuth } from "../api/axios"; // Use the authenticated instance

export const useFeedbacks = () => {
  const axiosInstance = useAxiosWithAuth();

  const getFeedbacks = async () => {
    try {
      const response = await axiosInstance.get("/feedbacks");
      return response.data;
    } catch (error) {
      console.error("❌ Error fetching feedbacks:", error);
      throw error;
    }
  };

  return { getFeedbacks };
};
