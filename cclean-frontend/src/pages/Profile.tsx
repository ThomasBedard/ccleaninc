import { useAuth0 } from "@auth0/auth0-react";
import './Profile.css'; 

const Profile = () => {
  const { user, isAuthenticated, isLoading } = useAuth0();

  if (isLoading) {
    return <div className="profile-loading">Loading ...</div>;
  }

  return (
    isAuthenticated && user && (
      <div className="profile-container">
        <div className="profile-image-wrapper">
          <img className="profile-image" src={user.picture} alt={user.name} />
        </div>
        <h2 className="profile-name">{user.name}</h2>
        <p className="profile-email">{user.email}</p>
      </div>
    )
  );
};

export default Profile;