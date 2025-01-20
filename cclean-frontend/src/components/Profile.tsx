import { useAuth0 } from "@auth0/auth0-react";
import "./Profile.css"; // Link to the external CSS file

const Profile = () => {
  const { user, isAuthenticated, isLoading } = useAuth0();

  if (isLoading) {
    return (
      <div className="profile-loading">
        <div className="loading-text">Loading...</div>
      </div>
    );
  }

  return (
    isAuthenticated && (
      <div className="profile-container">
        {user && (
          <div className="profile-card">
            <img src={user.picture} alt={user.name} className="profile-image" />
            <h2 className="profile-name">{user.name}</h2>
            <p className="profile-email">{user.email}</p>
            <button className="profile-button">Edit Profile</button>
          </div>
        )}
      </div>
    )
  );
};

export default Profile;
