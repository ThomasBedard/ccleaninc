import { Navigate, useLocation, Outlet } from "react-router-dom";
import { useAuth0 } from "@auth0/auth0-react";
import { jwtDecode } from "jwt-decode";
import { useEffect, useState } from "react";

interface DecodedToken {
  permissions: string[];
}

const PrivateRoute = ({
  requiredPermissions,
}: {
  requiredPermissions: string[];
}) => {
  const { isAuthenticated, getAccessTokenSilently } = useAuth0();
  const [permissions, setPermissions] = useState<string[]>([]);
  const location = useLocation();
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    const fetchPermissions = async () => {
      try {
        if (!isAuthenticated) return;
        const token = await getAccessTokenSilently();
        const decodedToken: DecodedToken = jwtDecode<DecodedToken>(token);
        setPermissions(decodedToken.permissions || []);
      } catch (error) {
        console.error("Error fetching permissions:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchPermissions();
  }, [isAuthenticated, getAccessTokenSilently]);

  // Wait for token to be fetched
  if (loading) {
    return <div>Loading...</div>;
  }

  // User is not authenticated → Redirect to login
  if (!isAuthenticated) {
    return <Navigate to="/" state={{ from: location }} replace />;
  }

  // Check if user has at least one of the required permissions
  const hasRequiredPermission = requiredPermissions.some((perm) =>
    permissions.includes(perm)
  );

  // User does not have the required permission → Redirect back to previous page or home
  if (!hasRequiredPermission) {
    return <Navigate to={location.state?.from || "/"} replace />;
  }

  // User has at least one required permission → Allow access
  return <Outlet />;
};

export default PrivateRoute;
