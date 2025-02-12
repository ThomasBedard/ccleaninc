import { jwtDecode } from "jwt-decode";

// Define the expected JWT structure
interface DecodedToken {
  email: string;
  sub?: string; // Auth0 user ID
}

// Extract email from the Auth0 token
export const extractEmailFromToken = (token: string): string | null => {
  try {
    const decoded: DecodedToken = jwtDecode(token);
    return decoded.email || null;
  } catch (error) {
    console.error("❌ Failed to decode JWT token:", error);
    return null;
  }
};

