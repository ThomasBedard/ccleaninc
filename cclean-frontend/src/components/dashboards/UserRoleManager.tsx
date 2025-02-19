import { useEffect, useState } from "react";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const AUTH0_DOMAIN = import.meta.env.VITE_AUTH0_DOMAIN;
const MANAGEMENT_API_TOKEN = import.meta.env.VITE_AUTH0_API_MANAGEMENT_TOKEN;
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
  // Map to store permissions for each user on demand.
  const [userPermissions, setUserPermissions] = useState<{
    [key: string]: Permission[] | null;
  }>({});
  const [loading, setLoading] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");

  const fetchUsers = async () => {
    setLoading(true);
    try {
      const response = await fetch(`https://${AUTH0_DOMAIN}/api/v2/users`, {
        headers: { Authorization: `Bearer ${MANAGEMENT_API_TOKEN}` },
      });
      const data: User[] = await response.json();
      setUsers(data);
    } catch (error) {
      console.error("Error fetching users:", error);
    } finally {
      setLoading(false);
    }
  };

  // Fetch permissions for a single user when needed.
  const fetchUserPermissions = async (userId: string): Promise<void> => {
    try {
      const response = await fetch(
        `https://${AUTH0_DOMAIN}/api/v2/users/${userId}/permissions`,
        {
          headers: {
            Authorization: `Bearer ${MANAGEMENT_API_TOKEN}`,
          },
        }
      );
      const perms: Permission[] = await response.json();
      // Save permissions (even if empty array) to indicate they were loaded.
      setUserPermissions((prev) => ({ ...prev, [userId]: perms }));
    } catch (error) {
      console.error(`Error fetching permissions for user ${userId}:`, error);
      toast.error("Failed to fetch permissions.");
      // Mark as loaded with empty array on error to prevent repeated attempts.
      setUserPermissions((prev) => ({ ...prev, [userId]: [] }));
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  // These functions are still using the roles endpoints.
  interface AssignRoleParams {
    userId: string;
    roleId: string;
  }
  const assignRole = async ({
    userId,
    roleId,
  }: AssignRoleParams): Promise<void> => {
    try {
      await fetch(`https://${AUTH0_DOMAIN}/api/v2/users/${userId}/roles`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${MANAGEMENT_API_TOKEN}`,
        },
        body: JSON.stringify({ roles: [roleId] }),
      });
      toast.success("Employee role assigned successfully!");
      fetchUsers(); // Optionally, refresh the user list.
    } catch (error) {
      console.error("Error assigning role:", error);
      toast.error("Failed to assign role.");
    }
  };

  interface RemoveRoleParams {
    userId: string;
    roleId: string;
  }
  const removeRole = async ({
    userId,
    roleId,
  }: RemoveRoleParams): Promise<void> => {
    try {
      await fetch(`https://${AUTH0_DOMAIN}/api/v2/users/${userId}/roles`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${MANAGEMENT_API_TOKEN}`,
        },
        body: JSON.stringify({ roles: [roleId] }),
      });
      toast.info("Employee role removed successfully!");
      fetchUsers();
    } catch (error) {
      console.error("Error removing role:", error);
      toast.error("Failed to remove role.");
    }
  };

  // Filter users based on the search term (case-insensitive)
  const filteredUsers = users.filter((user) =>
    user.email.toLowerCase().includes(searchTerm.toLowerCase())
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
                    <button
                      onClick={() =>
                        assignRole({
                          userId: user.user_id,
                          roleId: EMPLOYEE_ROLE_ID,
                        })
                      }
                    >
                      Assign Employee Permission
                    </button>
                    <button
                      onClick={() =>
                        removeRole({
                          userId: user.user_id,
                          roleId: EMPLOYEE_ROLE_ID,
                        })
                      }
                    >
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
