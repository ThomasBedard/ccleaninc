import { useEffect, useState } from "react";
import { useAuth0 } from "@auth0/auth0-react";
import axiosInstance from "../api/axios";
import { extractEmailFromToken } from "../api/authUtils";
import { useLanguage } from "../hooks/useLanguage"; // ✅ Import translation hook

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
  const { translations } = useLanguage(); // ✅ Fetch translations
  const [availabilities, setAvailabilities] = useState<Availability[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [userEmail, setUserEmail] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    const fetchAvailabilities = async () => {
      try {
        setError(null);
        setLoading(true);

        // ✅ Get token from Auth0
        const token = await getAccessTokenSilently();

        // ✅ Decode the user’s email from custom claim
        const email = extractEmailFromToken(token);
        if (!email) {
          setError(translations.myAvailabilities?.error?.no_email || "User email not found in token.");
          return;
        }
        setUserEmail(email);

        // ✅ Call the backend endpoint
        const response = await axiosInstance.get<Availability[]>(
          "/availabilities/my-availabilities",
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );

        setAvailabilities(response.data);
      } catch (err) {
        console.error("❌ Failed to load availabilities:", err);
        setError(translations.myAvailabilities?.error?.fetch_failed || "Failed to load availabilities. Please try again.");
      } finally {
        setLoading(false);
      }
    };

    fetchAvailabilities();
  }, [getAccessTokenSilently]);

  return (
    <div style={{ padding: "1rem" }}>
      <h2>{translations.myAvailabilities?.title || "My Availabilities"}</h2>

      {/* Show the user’s email if available */}
      {userEmail && (
        <p>
          {translations.myAvailabilities?.showing_for || "Showing availabilities for:"}{" "}
          {userEmail}
        </p>
      )}

      {/* Show loading indicator */}
      {loading && <p>{translations.myAvailabilities?.loading || "Loading..."}</p>}

      {/* Show error if there is one */}
      {error && <p style={{ color: "red" }}>{error}</p>}

      {/* If no error and we have availabilities, display them */}
      {!loading && !error && availabilities.length > 0 && (
        <table style={{ borderCollapse: "collapse", width: "100%" }}>
          <thead>
            <tr>
              <th style={cellStyle}>{translations.myAvailabilities?.table?.availability_id || "Availability ID"}</th>
              <th style={cellStyle}>{translations.myAvailabilities?.table?.employee_name || "Employee Name"}</th>
              <th style={cellStyle}>{translations.myAvailabilities?.table?.date_time || "Date/Time"}</th>
              <th style={cellStyle}>{translations.myAvailabilities?.table?.shift || "Shift"}</th>
              <th style={cellStyle}>{translations.myAvailabilities?.table?.comments || "Comments"}</th>
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
        <p>{translations.myAvailabilities?.no_availabilities || "No availabilities found."}</p>
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
