import { useEffect, useState } from "react";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;
const EMPLOYEE_ROLE_ID = "rol_rCHl1KWuvcWoyf9M";

interface User {
  user_id: string;
  email: string;
  app_metadata?: {
    roles?: string[];
  };
}

interface Permission {
  permission_name: string;
  resource_server_identifier: string;
}

const UserRoleManager = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [userPermissions, setUserPermissions] = useState<{
    [key: string]: Permission[] | null;
  }>({});
  const [loading, setLoading] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");

  /**
   * Fetch users from YOUR BACKEND endpoint.
   * Example: GET /auth0/users
   */
  const fetchUsers = async () => {
    setLoading(true);
    try {
      const response = await fetch(`${API_BASE_URL}/auth0/users`, {
        method: "GET",
      });
      if (!response.ok) {
        throw new Error(`Fetch failed with status ${response.status}`);
      }
      const data: User[] = await response.json();
      setUsers(data);
    } catch (error) {
      console.error("Error fetching users:", error);
      toast.error("Failed to load users");
    } finally {
      setLoading(false);
    }
  };

  /**
   * Fetch permissions for a single user from YOUR BACKEND endpoint.
   * Example: GET /auth0/users/{userId}/permissions
   */
  const fetchUserPermissions = async (userId: string): Promise<void> => {
    // encode the userId in the URL path:
    const encodedUserId = encodeURIComponent(userId);
    try {
      const response = await fetch(
        `${API_BASE_URL}/auth0/users/${encodedUserId}/permissions`,
        {
          method: "GET",
        }
      );
      if (!response.ok) {
        throw new Error(`Fetch failed with status ${response.status}`);
      }
      const perms: Permission[] = await response.json();
      setUserPermissions((prev) => ({ ...prev, [userId]: perms }));
    } catch (error) {
      console.error(`Error fetching permissions for user ${userId}:`, error);
      toast.error("Failed to fetch permissions.");
      setUserPermissions((prev) => ({ ...prev, [userId]: [] }));
    }
  };

  const assignEmployeeRole = async (userId: string): Promise<void> => {
    const encodedUserId = encodeURIComponent(userId);
    try {
      const response = await fetch(
        `${API_BASE_URL}/auth0/users/${encodedUserId}/roles`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ roleId: EMPLOYEE_ROLE_ID }),
        }
      );
      if (response.ok) {
        toast.success("Assigned Employee Role");
        await fetchUserPermissions(userId);
      } else {
        throw new Error(`Assign role failed: ${response.status}`);
      }
    } catch (err) {
      console.error("Error assigning role:", err);
    }
  };

  const removeEmployeeRole = async (userId: string): Promise<void> => {
    const encodedUserId = encodeURIComponent(userId);
    try {
      const response = await fetch(
        `${API_BASE_URL}/auth0/users/${encodedUserId}/roles`,
        {
          method: "DELETE",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ roleId: EMPLOYEE_ROLE_ID }),
        }
      );
      if (response.ok) {
        toast.success("Removed Employee Role");
        await fetchUserPermissions(userId);
      } else {
        throw new Error(`Remove role failed: ${response.status}`);
      }
    } catch (err) {
      console.error("Error removing role:", err);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  // Filter users based on the search term (case-insensitive)
  const filteredUsers = users.filter((user) =>
    user.email?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div>
      <h2>User Permission Manager</h2>
      <div style={{ marginBottom: "1rem" }}>
        <input
          type="text"
          placeholder="Search users by email..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          style={{ padding: "0.5rem", width: "300px" }}
        />
      </div>
      {loading && <p>Loading users...</p>}
      {!loading && filteredUsers.length === 0 && <p>No users found.</p>}
      {!loading && filteredUsers.length > 0 && (
        <table>
          <thead>
            <tr>
              <th>Email</th>
              <th>Permissions</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {filteredUsers.map((user) => {
              const permsData = userPermissions[user.user_id];
              let permsContent;
              if (permsData === undefined) {
                permsContent = (
                  <button onClick={() => fetchUserPermissions(user.user_id)}>
                    Load Permissions
                  </button>
                );
              } else if (permsData && permsData.length === 0) {
                permsContent = <span>No permissions assigned</span>;
              } else {
                permsContent = permsData
                  ? permsData.map((p) => p.permission_name).join(", ")
                  : "No permissions assigned";
              }
              return (
                <tr key={user.user_id}>
                  <td>{user.email}</td>
                  <td>{permsContent}</td>
                  <td style={{ display: "flex", gap: "0.5rem" }}>
                    <button onClick={() => assignEmployeeRole(user.user_id)}>
                      Assign Employee Permission
                    </button>
                    <button onClick={() => removeEmployeeRole(user.user_id)}>
                      Remove Employee Permission
                    </button>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      )}
      <ToastContainer />
    </div>
  );
};

export default UserRoleManager;
