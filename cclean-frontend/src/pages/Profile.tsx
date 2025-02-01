import React, { useEffect, useState } from "react";
import { useAuth0 } from "@auth0/auth0-react";
import axiosInstance from "../api/axios";
import "./Profile.css";

interface CustomerResponse {
  customerId: string;
  firstName: string | null;
  lastName: string | null;
  companyName: string | null;
  email: string | null;
  phoneNumber: string | null;
  address: string | null;
}

const Profile: React.FC = () => {
  const { user, isAuthenticated, isLoading } = useAuth0();

  // If null, we haven't prompted. If true, user wants a customer profile. If false, user doesn't.
  const [wantsProfile, setWantsProfile] = useState<boolean | null>(null);

  // The DB record if they do want a profile
  const [customerData, setCustomerData] = useState<CustomerResponse | null>(null);

  // Local form fields for editing
  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    phoneNumber: "",
    companyName: "",
    address: "",
  });

  const [isEditing, setIsEditing] = useState(false);

  // If user wants a profile, and we haven't loaded it yet, fetch from backend
  useEffect(() => {
    if (!isLoading && isAuthenticated && user?.email && wantsProfile === true && !customerData) {
      axiosInstance
        .get(`/customers/byEmail?email=${encodeURIComponent(user.email)}`)
        .then((res) => {
          const foundCustomer = res.data as CustomerResponse;
          setCustomerData(foundCustomer);
          setForm({
            firstName: foundCustomer.firstName || "",
            lastName: foundCustomer.lastName || "",
            phoneNumber: foundCustomer.phoneNumber || "",
            companyName: foundCustomer.companyName || "",
            address: foundCustomer.address || "",
          });
        })
        .catch((err) => {
          console.error("Error fetching/creating customer:", err);
        });
    }
  }, [isLoading, isAuthenticated, user?.email, wantsProfile, customerData]);

  // Handler for all input fields
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  // Save to backend
  const handleSave = () => {
    if (!customerData) return;

    const updatedCustomer = {
      ...customerData,
      firstName: form.firstName || null,
      lastName: form.lastName || null,
      companyName: form.companyName || null,
      phoneNumber: form.phoneNumber || null,
      address: form.address || null,
    };

    axiosInstance
      .put(`/customers/${customerData.customerId}`, updatedCustomer)
      .then((res) => {
        alert("Profile updated!");
        setCustomerData(res.data);
        setForm({
          firstName: res.data.firstName || "",
          lastName: res.data.lastName || "",
          phoneNumber: res.data.phoneNumber || "",
          companyName: res.data.companyName || "",
          address: res.data.address || "",
        });
        setIsEditing(false);
      })
      .catch((err) => {
        console.error("Error updating profile:", err);
        alert("Error updating profile");
      });
  };

  if (isLoading) {
    return <div className="profile-loading">Loading ...</div>;
  }

  if (!isAuthenticated || !user) {
    return <div>Please log in to see your profile.</div>;
  }

  // If user hasn't decided if they want a profile or not, show the prompt
  if (wantsProfile === null) {
    return (
      <div className="profile-container">
        <div className="profile-image-wrapper">
          <img className="profile-image" src={user.picture} alt={user.name} />
        </div>
        <h2 className="profile-name">{user.name}</h2>
        <p className="profile-email">{user.email}</p>
        <p>Would you like to set up a Customer Profile?</p>
        <button onClick={() => setWantsProfile(true)}>Yes</button>
        <button onClick={() => setWantsProfile(false)}>No</button>
      </div>
    );
  }

  // If user said "No," we show just their basic Auth0 data
  if (wantsProfile === false) {
    return (
      <div className="profile-container">
        <div className="profile-image-wrapper">
          <img className="profile-image" src={user.picture} alt={user.name} />
        </div>
        <h2 className="profile-name">{user.name}</h2>
        <p className="profile-email">{user.email}</p>
        <p>You have opted out of creating a customer profile.</p>
        <button onClick={() => setWantsProfile(true)}>Create Profile Anyway</button>
      </div>
    );
  }

  // If user said "Yes," but we haven't fetched or created a record yet, wait
  if (wantsProfile === true && !customerData) {
    return <div>Loading your customer profile...</div>;
  }

  // Otherwise, user said "Yes" and we have a record
  return (
    <div className="profile-container">
      <div className="profile-image-wrapper">
        <img className="profile-image" src={user.picture} alt={user.name} />
      </div>
      <h2 className="profile-name">{user.name}</h2>
      <p className="profile-email">{user.email}</p>

      {/* Show read‐only fields if not editing */}
      {!isEditing && (
        <>
          <p><strong>Customer ID:</strong> {customerData?.customerId}</p>
          <p><strong>First Name:</strong> {customerData?.firstName || ""}</p>
          <p><strong>Last Name:</strong> {customerData?.lastName || ""}</p>
          <p><strong>Phone:</strong> {customerData?.phoneNumber || ""}</p>
          <p><strong>Company:</strong> {customerData?.companyName || ""}</p>
          <p><strong>Address:</strong> {customerData?.address || ""}</p>
          <button onClick={() => setIsEditing(true)}>Edit Profile</button>
        </>
      )}

      {/* Show input fields if editing */}
      {isEditing && (
        <>
          <div style={{ margin: "0.5rem 0" }}>
            <label>First Name: </label>
            <input
              name="firstName"
              value={form.firstName}
              onChange={handleChange}
            />
          </div>
          <div style={{ margin: "0.5rem 0" }}>
            <label>Last Name: </label>
            <input
              name="lastName"
              value={form.lastName}
              onChange={handleChange}
            />
          </div>
          <div style={{ margin: "0.5rem 0" }}>
            <label>Phone: </label>
            <input
              name="phoneNumber"
              value={form.phoneNumber}
              onChange={handleChange}
            />
          </div>
          <div style={{ margin: "0.5rem 0" }}>
            <label>Company: </label>
            <input
              name="companyName"
              value={form.companyName}
              onChange={handleChange}
            />
          </div>
          <div style={{ margin: "0.5rem 0" }}>
            <label>Address: </label>
            <input
              name="address"
              value={form.address}
              onChange={handleChange}
            />
          </div>

          <button onClick={handleSave} style={{ marginRight: "1rem" }}>
            Save Profile
          </button>
          <button onClick={() => setIsEditing(false)}>Cancel</button>
        </>
      )}
    </div>
  );
};

export default Profile;
