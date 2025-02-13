// import axios from "axios";
// import { useAuth0 } from "@auth0/auth0-react";
// import { useEffect, useState } from "react";

// // 🔄 Dynamically set the API base URL
// const API_BASE_URL =
//   import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api/v1";

// const axiosInstance = axios.create({
//   baseURL: API_BASE_URL,
//   timeout: 10000,
//   headers: {
//     "Content-Type": "application/json",
//   },
// });

// // ✅ Custom hook to use Axios with Auth0 authentication
// export const useAxiosWithAuth = () => {
//   const { getAccessTokenSilently, isAuthenticated, loginWithRedirect } =
//     useAuth0();
//   const [token, setToken] = useState<string | null>(null);

//   useEffect(() => {
//     const fetchToken = async () => {
//       try {
//         if (!isAuthenticated) {
//           console.warn("❌ User is not authenticated. Redirecting to login...");
//           await loginWithRedirect();
//           return;
//         }

//         const fetchedToken = await getAccessTokenSilently();
//         console.log("🔑 Auth0 Token Retrieved:", fetchedToken);
//         setToken(fetchedToken);
//       } catch (error) {
//         console.error("❌ Error fetching Auth0 token:", error);
//       }
//     };

//     fetchToken();
//   }, [isAuthenticated, getAccessTokenSilently, loginWithRedirect]);

//   useEffect(() => {
//     if (token) {
//       console.log("📡 Attaching Token to Requests...");
//       axiosInstance.interceptors.request.use(
//         (config) => {
//           config.headers.Authorization = `Bearer ${token}`;
//           console.log(
//             "📡 Sending Request with Token:",
//             config.headers.Authorization
//           );
//           return config;
//         },
//         (error) => Promise.reject(error)
//       );
//     }
//   }, [token]);

//   return axiosInstance;
// };

// export default axiosInstance;


import axios from "axios";
import { useAuth0 } from "@auth0/auth0-react";
import { useEffect, useState } from "react";
import { extractEmailFromToken } from "./authUtils";

// 🔄 Dynamically set the API base URL
const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api/v1";

const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
  },
});

// ✅ Custom hook to use Axios with Auth0 authentication
export const useAxiosWithAuth = () => {
  const { getAccessTokenSilently, isAuthenticated, loginWithRedirect } =
    useAuth0();
  const [token, setToken] = useState<string | null>(null);
  const [userEmail, setUserEmail] = useState<string | null>(null);

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

        // Extract user email from token
        const email = extractEmailFromToken(fetchedToken);
        setUserEmail(email);
        console.log("📧 Extracted User Email:", email);
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
          if (userEmail) {
            config.headers["X-User-Email"] = userEmail; // Custom header for backend
          }
          console.log("📡 Sending Request with Token:", config.headers.Authorization);
          return config;
        },
        (error) => Promise.reject(error)
      );
    }
  }, [token, userEmail]);

  return axiosInstance;
};

export default axiosInstance;

