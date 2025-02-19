import React, { useEffect, useState } from "react";
import { useAuth0 } from "@auth0/auth0-react";
import axiosInstance from "../api/axios";
import { useLanguage } from "../hooks/useLanguage"; // ✅ Import translation hook
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
  const { translations } = useLanguage(); // ✅ Fetch translations

  const [wantsProfile, setWantsProfile] = useState<boolean | null>(null);
  const [customerData, setCustomerData] = useState<CustomerResponse | null>(
    null
  );
  const [isEditing, setIsEditing] = useState(false);

  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    phoneNumber: "",
    companyName: "",
    address: "",
  });

  useEffect(() => {
    if (
      !isLoading &&
      isAuthenticated &&
      user?.email &&
      wantsProfile === true &&
      !customerData
    ) {
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
          console.error("Error fetching customer:", err);
        });
    }
  }, [isLoading, isAuthenticated, user?.email, wantsProfile, customerData]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;

    // ✅ Enforce phone number format (514-555-1234)
    if (name === "phoneNumber") {
      const formattedValue = value.replace(/[^\d-]/g, "");
      const cleaned = formattedValue.replace(/-/g, "");
      let formatted = "";

      if (cleaned.length > 0) formatted = cleaned.slice(0, 3);
      if (cleaned.length > 3) formatted += "-" + cleaned.slice(3, 6);
      if (cleaned.length > 6) formatted += "-" + cleaned.slice(6, 10);

      setForm((prev) => ({ ...prev, phoneNumber: formatted }));
      return;
    }

    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSave = () => {
    if (!customerData) return;

    // ✅ Validate required fields
    if (!form.firstName.trim()) {
      alert(
        translations.profile?.error?.first_name_required ||
          "First Name cannot be empty."
      );
      return;
    }
    if (!form.lastName.trim()) {
      alert(
        translations.profile?.error?.last_name_required ||
          "Last Name cannot be empty."
      );
      return;
    }
    if (
      !form.phoneNumber.trim() ||
      !/^\d{3}-\d{3}-\d{4}$/.test(form.phoneNumber)
    ) {
      alert(
        translations.profile?.error?.invalid_phone ||
          "Phone number must be in the format 514-555-2355."
      );
      return;
    }
    if (!form.address.trim()) {
      alert(
        translations.profile?.error?.address_required ||
          "Address cannot be empty."
      );
      return;
    }

    // ✅ Validate address format (Number + Street Name)
    const addressRegex = /^\d+\s[A-Za-z\s]+$/;
    if (!addressRegex.test(form.address)) {
      alert(
        translations.profile?.error?.invalid_address ||
          "Address must be in the format: 123 Main Street"
      );
      return;
    }

    const updatedCustomer = {
      ...customerData,
      firstName: form.firstName,
      lastName: form.lastName,
      companyName: form.companyName || null,
      phoneNumber: form.phoneNumber,
      address: form.address,
    };

    axiosInstance
      .put(`/customers/${customerData.customerId}`, updatedCustomer)
      .then((res) => {
        alert(
          translations.profile?.success?.update_success ||
            "Profile updated successfully!"
        );
        setCustomerData(res.data);
        setIsEditing(false);
      })
      .catch((err) => {
        console.error("Error updating profile:", err);
        alert(
          translations.profile?.error?.update_failed ||
            "Error updating profile."
        );
      });
  };

  if (isLoading) {
    return (
      <div className="profile-loading">
        {translations.profile?.loading || "Loading..."}
      </div>
    );
  }

  if (!isAuthenticated || !user) {
    return (
      <div>
        {translations.profile?.not_logged_in ||
          "Please log in to see your profile."}
      </div>
    );
  }

  if (wantsProfile === null) {
    return (
      <div className="profile-container">
        <h2>{user.name}</h2>
        <p>{user.email}</p>
        <p>
          {translations.profile?.prompt ||
            "Would you like to set up a Customer Profile?"}
        </p>
        <button onClick={() => setWantsProfile(true)}>
          {translations.profile?.yes || "Yes"}
        </button>
        <button onClick={() => setWantsProfile(false)}>
          {translations.profile?.no || "No"}
        </button>
      </div>
    );
  }

  if (wantsProfile === false) {
    return (
      <div className="profile-container">
        <h2>{user.name}</h2>
        <p>{user.email}</p>
        <p>
          {translations.profile?.opt_out ||
            "You have opted out of creating a customer profile."}
        </p>
        <button onClick={() => setWantsProfile(true)}>
          {translations.profile?.create_profile_anyway ||
            "Create Profile Anyway"}
        </button>
      </div>
    );
  }

  if (wantsProfile === true && !customerData) {
    return (
      <div>
        {translations.profile?.loading_profile ||
          "Loading your customer profile..."}
      </div>
    );
  }

  return (
    <div className="profile-container">
      <h2>{user.name}</h2>
      <p>{user.email}</p>

      {!isEditing ? (
        <>
          <p>
            <strong>
              {translations.profile?.customer_id || "Customer ID"}:
            </strong>{" "}
            {customerData?.customerId}
          </p>
          <p>
            <strong>{translations.profile?.first_name || "First Name"}:</strong>{" "}
            {customerData?.firstName || ""}
          </p>
          <p>
            <strong>{translations.profile?.last_name || "Last Name"}:</strong>{" "}
            {customerData?.lastName || ""}
          </p>
          <p>
            <strong>{translations.profile?.phone || "Phone"}:</strong>{" "}
            {customerData?.phoneNumber || ""}
          </p>
          <p>
            <strong>{translations.profile?.company || "Company"}:</strong>{" "}
            {customerData?.companyName || ""}
          </p>
          <p>
            <strong>{translations.profile?.address || "Address"}:</strong>{" "}
            {customerData?.address || ""}
          </p>
          <button onClick={() => setIsEditing(true)}>
            {translations.profile?.edit_profile || "Edit Profile"}
          </button>
        </>
      ) : (
        <>
          <label>{translations.profile?.first_name || "First Name"}:</label>
          <input
            name="firstName"
            value={form.firstName}
            onChange={handleChange}
          />

          <label>{translations.profile?.last_name || "Last Name"}:</label>
          <input
            name="lastName"
            value={form.lastName}
            onChange={handleChange}
          />

          <label>{translations.profile?.phone || "Phone"}:</label>
          <input
            name="phoneNumber"
            value={form.phoneNumber}
            onChange={handleChange}
          />

          <label>{translations.profile?.company || "Company"}:</label>
          <input
            name="companyName"
            value={form.companyName}
            onChange={handleChange}
          />

          <label>{translations.profile?.address || "Address"}:</label>
          <input name="address" value={form.address} onChange={handleChange} />

          <button onClick={handleSave}>
            {translations.profile?.save_profile || "Save Profile"}
          </button>
          <button onClick={() => setIsEditing(false)}>
            {translations.profile?.cancel || "Cancel"}
          </button>
        </>
      )}
    </div>
  );
};

export default Profile;
