import { useEffect, useState } from "react";
import { useAuth0 } from "@auth0/auth0-react";
import axiosInstance from "../api/axios";
import { extractEmailFromToken } from "../api/authUtils";

interface Availability {
  id: number;
  availabilityId: string;
  employeeFirstName: string;
  employeeLastName: string;
  employeeId: string;
  availableDate: string;  
  shift: string;
  comments?: string;
}

const MyAvailabilities: React.FC = () => {
  const { getAccessTokenSilently } = useAuth0();
  const [availabilities, setAvailabilities] = useState<Availability[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [userEmail, setUserEmail] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    const fetchAvailabilities = async () => {
      try {
        setError(null);
        setLoading(true);

        // 1) Get token from Auth0
        const token = await getAccessTokenSilently();

        // 2) Decode the user’s email from custom claim
        const email = extractEmailFromToken(token);
        if (!email) {
          setError("User email not found in token.");
          return;
        }
        setUserEmail(email);

        // 3) Call the backend endpoint
        const response = await axiosInstance.get<Availability[]>(
          "/availabilities/my-availabilities",
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );

        setAvailabilities(response.data);
      } catch (err) {
        console.error("❌ Failed to load availabilities:", err);
        setError("Failed to load availabilities. Please try again.");
      } finally {
        setLoading(false);
      }
    };

    fetchAvailabilities();
  }, [getAccessTokenSilently]);

  return (
    <div style={{ padding: "1rem" }}>
      <h2>My Availabilities</h2>

      {/* Show the user’s email if available */}
      {userEmail && <p>Showing availabilities for: {userEmail}</p>}

      {/* Show loading indicator */}
      {loading && <p>Loading...</p>}

      {/* Show error if there is one */}
      {error && <p style={{ color: "red" }}>{error}</p>}

      {/* If no error and we have availabilities, display them */}
      {!loading && !error && availabilities.length > 0 && (
        <table style={{ borderCollapse: "collapse", width: "100%" }}>
          <thead>
            <tr>
              <th style={cellStyle}>Availability ID</th>
              <th style={cellStyle}>Employee Name</th>
              <th style={cellStyle}>Date/Time</th>
              <th style={cellStyle}>Shift</th>
              <th style={cellStyle}>Comments</th>
            </tr>
          </thead>
          <tbody>
            {availabilities.map((avail) => (
              <tr key={avail.availabilityId}>
                <td style={cellStyle}>{avail.availabilityId}</td>
                <td style={cellStyle}>
                  {avail.employeeFirstName} {avail.employeeLastName}
                </td>
                <td style={cellStyle}>{avail.availableDate}</td>
                <td style={cellStyle}>{avail.shift}</td>
                <td style={cellStyle}>{avail.comments || "N/A"}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      {/* If no availabilities found */}
      {!loading && !error && availabilities.length === 0 && (
        <p>No availabilities found.</p>
      )}
    </div>
  );
};

// Optional inline cell style for quick table formatting
const cellStyle: React.CSSProperties = {
  border: "1px solid #ccc",
  padding: "8px",
};

export default MyAvailabilities;
