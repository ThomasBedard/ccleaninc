import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
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
  const axiosWithAuth = useAxiosWithAuth(); // <-- Interceptor-based instance
  const [availabilities, setAvailabilities] = useState<Availability[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const navigate = useNavigate();

  const fetchAvailabilities = async () => {
    try {
      setError(null);
      setLoading(true);
      // Call the JWT-protected endpoint
      const response = await axiosWithAuth.get<Availability[]>("/availabilities/my-availabilities");
      setAvailabilities(response.data);
    } catch (err) {
      console.error("❌ Failed to load availabilities:", err);
      setError("Failed to load availabilities. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAvailabilities();
    // We only want to run once, so omit fetchAvailabilities from dependencies or wrap carefully
  }, []);

  // Handler for deleting an availability
  const handleDelete = async (availabilityId: string) => {
    try {
      await axiosWithAuth.delete(`/availabilities/my-availabilities/${availabilityId}`);
      // Remove the deleted item from local state
      setAvailabilities((prev) => prev.filter((avail) => avail.availabilityId !== availabilityId));
      alert("Availability deleted successfully.");
    } catch (err) {
      console.error("Failed to delete availability:", err);
      alert("Failed to delete availability.");
    }
  };

  return (
    <div className="my-availabilities-container">
      <h2>My Availabilities</h2>

      {/* Show loading indicator */}
      {loading && <p>Loading...</p>}

      {/* Show error if there is one */}
      {error && <p style={{ color: "red" }}>{error}</p>}

      {/* If availabilities are found, display them in a table with Edit and Delete buttons */}
      {!loading && !error && availabilities.length > 0 && (
        <table className="my-availabilities-table">
          <thead>
            <tr>
              <th>Availability ID</th>
              <th>Employee Name</th>
              <th>Date/Time</th>
              <th>Shift</th>
              <th>Comments</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {availabilities.map((avail) => (
              <tr key={avail.availabilityId}>
                <td>{avail.availabilityId}</td>
                <td>{avail.employeeFirstName} {avail.employeeLastName}</td>
                <td>{avail.availableDate}</td>
                <td>{avail.shift}</td>
                <td>{avail.comments || "N/A"}</td>
                <td>
                  {/* Edit button navigates to edit page */}
                  <button
                    onClick={() =>
                      navigate(`/my-availabilities/edit/${avail.availabilityId}`)
                    }
                  >
                    Edit
                  </button>
                  {/* Delete button calls the delete handler */}
                  <button onClick={() => handleDelete(avail.availabilityId)}>
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      {/* If no availabilities found */}
      {!loading && !error && availabilities.length === 0 && (
        <p>No availabilities found.</p>
      )}

      {/* Add Availability Button */}
      <button onClick={() => navigate("/add-availability")}>
        Add Availability
      </button>
    </div>
  );
};

export default MyAvailabilities;
