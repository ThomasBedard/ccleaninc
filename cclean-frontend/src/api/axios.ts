import axios from "axios";
import { useAuth0 } from "@auth0/auth0-react";
import { useEffect } from "react";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8080/api/v1",
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
  },
});

export const useAxiosWithAuth = () => {
  const { getAccessTokenSilently, isAuthenticated, loginWithRedirect } =
    useAuth0();

  useEffect(() => {
    const attachToken = async () => {
      try {
        const token = await getAccessTokenSilently();
        console.log("Token fetched:", token); // Debugging log

        axiosInstance.interceptors.request.use(
          (config) => {
            config.headers.Authorization = `Bearer ${token}`;
            return config;
          },
          (error) => Promise.reject(error)
        );
      } catch (error) {
        console.error("Error fetching token:", error);
      }
    };

    attachToken();
  }, [isAuthenticated, getAccessTokenSilently, loginWithRedirect]);

  return axiosInstance;
};

export default axiosInstance;
