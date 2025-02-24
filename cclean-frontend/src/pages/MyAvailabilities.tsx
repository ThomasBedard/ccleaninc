import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth0 } from "@auth0/auth0-react";
import { extractEmailFromToken } from "../api/authUtils";
import { useLanguage } from "../hooks/useLanguage";
import { useAxiosWithAuth } from "../api/axios";
import "./MyAvailabilities.css";

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
  const { translations } = useLanguage();
  const navigate = useNavigate();
  const axiosWithAuth = useAxiosWithAuth();

  const [availabilities, setAvailabilities] = useState<Availability[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [userEmail, setUserEmail] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

  const fetchAvailabilities = async () => {
    try {
      setError(null);
      setLoading(true);
      // Get token and extract user's email
      const token = await getAccessTokenSilently();
      const email = extractEmailFromToken(token);
      if (!email) {
        setError(
          translations.myAvailabilities?.error?.no_email ||
            "User email not found in token."
        );
        return;
      }
      setUserEmail(email);

      // Fetch availabilities using the interceptor-based axios instance
      const response = await axiosWithAuth.get<Availability[]>(
        "/availabilities/my-availabilities"
      );
      setAvailabilities(response.data);
    } catch (err) {
      console.error("❌ Failed to load availabilities:", err);
      setError(
        translations.myAvailabilities?.error?.fetch_failed ||
          "Failed to load availabilities. Please try again."
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAvailabilities();
  }, [getAccessTokenSilently]);

  // Handler for deleting an availability
  const handleDelete = async (availabilityId: string) => {
    try {
      await axiosWithAuth.delete(
        `/availabilities/my-availabilities/${availabilityId}`
      );
      // Remove the deleted item from local state
      setAvailabilities((prev) =>
        prev.filter((avail) => avail.availabilityId !== availabilityId)
      );
      alert("Availability deleted successfully.");
    } catch (err) {
      console.error("Failed to delete availability:", err);
      alert("Failed to delete availability.");
    }
  };

  return (
    <div className="my-availabilities-container">
      <h2>{translations.myAvailabilities?.title || "My Availabilities"}</h2>

      {/* Show user's email if available */}
      {userEmail && (
        <p>
          {translations.myAvailabilities?.showing_for ||
            "Showing availabilities for:"}{" "}
          {userEmail}
        </p>
      )}

      {/* Loading indicator */}
      {loading && (
        <p>{translations.myAvailabilities?.loading || "Loading..."}</p>
      )}

      {/* Error message */}
      {error && <p style={{ color: "red" }}>{error}</p>}

      {/* Availabilities table */}
      {!loading && !error && availabilities.length > 0 && (
        <table className="my-availabilities-table">
          <thead>
            <tr>
              <th>
                {translations.myAvailabilities?.table?.availability_id ||
                  "Availability ID"}
              </th>
              <th>
                {translations.myAvailabilities?.table?.employee_name ||
                  "Employee Name"}
              </th>
              <th>
                {translations.myAvailabilities?.table?.date_time || "Date/Time"}
              </th>
              <th>{translations.myAvailabilities?.table?.shift || "Shift"}</th>
              <th>
                {translations.myAvailabilities?.table?.comments || "Comments"}
              </th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {availabilities.map((avail) => (
              <tr key={avail.availabilityId}>
                <td>{avail.availabilityId}</td>
                <td>
                  {avail.employeeFirstName} {avail.employeeLastName}
                </td>
                <td>{avail.availableDate}</td>
                <td>{avail.shift}</td>
                <td>{avail.comments || "N/A"}</td>
                <td>
                  <button
                    onClick={() =>
                      navigate(
                        `/my-availabilities/edit/${avail.availabilityId}`
                      )
                    }
                  >
                    Edit
                  </button>
                  <button onClick={() => handleDelete(avail.availabilityId)}>
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      {/* Message when no availabilities are found */}
      {!loading && !error && availabilities.length === 0 && (
        <p>
          {translations.myAvailabilities?.no_availabilities ||
            "No availabilities found."}
        </p>
      )}

      {/* Add Availability button */}
      <button onClick={() => navigate("/add-availability")}>
        Add Availability
      </button>
    </div>
  );
};

export default MyAvailabilities;
