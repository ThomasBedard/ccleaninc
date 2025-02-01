import axios from "axios";
import { useAuth0 } from "@auth0/auth0-react";
import { useEffect, useState } from "react";

// ✅ Create an Axios instance with default configuration
const axiosInstance = axios.create({
  baseURL: "http://localhost:8080/api/v1",
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
  },
});

// ✅ Custom hook to use Axios with Auth0 authentication
export const useAxiosWithAuth = () => {
  const { getAccessTokenSilently, isAuthenticated, loginWithRedirect } = useAuth0();
  const [token, setToken] = useState<string | null>(null);

  useEffect(() => {
    const fetchToken = async () => {
      try {
        if (!isAuthenticated) {
          console.warn("❌ User is not authenticated. Redirecting to login...");
          await loginWithRedirect();
          return;
        }

        const fetchedToken = await getAccessTokenSilently();
        console.log("🔑 Auth0 Token Retrieved:", fetchedToken);
        setToken(fetchedToken);
      } catch (error) {
        console.error("❌ Error fetching Auth0 token:", error);
      }
    };

    fetchToken();
  }, [isAuthenticated, getAccessTokenSilently, loginWithRedirect]);

  useEffect(() => {
    if (token) {
      console.log("📡 Attaching Token to Requests...");
      axiosInstance.interceptors.request.use(
        (config) => {
          config.headers.Authorization = `Bearer ${token}`;
          console.log("📡 Sending Request with Token:", config.headers.Authorization);
          return config;
        },
        (error) => Promise.reject(error)
      );
    }
  }, [token]);

  return axiosInstance;
};

export default axiosInstance;
