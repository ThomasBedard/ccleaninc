import { jwtDecode } from "jwt-decode";

interface DecodedToken {
  "https://ccleaninc.com/email"?: string;
  sub?: string; 
  // [key: string]: any;
  [key: string]: unknown; 
}

export const extractEmailFromToken = (token: string): string | null => {
  try {
    const decoded: DecodedToken = jwtDecode(token);
    console.log("Decoded token:", decoded); 

    // Return the custom claim
    return decoded["https://ccleaninc.com/email"] || null;
  } catch (error) {
    console.error("❌ Failed to decode JWT token:", error);
    return null;
  }
};
