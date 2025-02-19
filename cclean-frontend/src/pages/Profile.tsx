import React, { useEffect, useState } from "react";
import { useAuth0 } from "@auth0/auth0-react";
import { useAxiosWithAuth } from "../api/axios";
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

interface EmployeeResponse {
  employeeId: string;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  role: string;
  isActive: boolean;
}

const Profile: React.FC = () => {
  const { user, isAuthenticated, isLoading } = useAuth0();
  const axiosWithAuth = useAxiosWithAuth();

  // Customer states
  const [customerData, setCustomerData] = useState<CustomerResponse | null>(null);
  const [wantsCustomerProfile, setWantsCustomerProfile] = useState<boolean | null>(null);
  const [showCustomerForm, setShowCustomerForm] = useState(false);

  // Employee states
  const [employeeData, setEmployeeData] = useState<EmployeeResponse | null>(null);
  const [wantsEmployeeProfile, setWantsEmployeeProfile] = useState<boolean | null>(null);
  const [showEmployeeForm, setShowEmployeeForm] = useState(false);

  // 1) Fetch existing Customer Profile (if any)
  useEffect(() => {
    if (!isLoading && isAuthenticated && user?.email) {
      axiosWithAuth
        .get(`/customers/byEmail?email=${encodeURIComponent(user.email)}`)
        .then((res) => {
          setCustomerData(res.data);
          setWantsCustomerProfile(true); // We have a profile
        })
        .catch((err) => {
          if (err.response?.status === 404) {
            // No customer profile
            setCustomerData(null);
            setWantsCustomerProfile(null); // Show prompt
          } else {
            console.error("Error fetching customer:", err);
          }
        });
    }
  }, [isLoading, isAuthenticated, user?.email, axiosWithAuth]);

  // 2) Fetch existing Employee Profile (if any)
  useEffect(() => {
    if (!isLoading && isAuthenticated && user?.email) {
      axiosWithAuth
        .get<EmployeeResponse>("/employees/profile")
        .then((res) => {
          setEmployeeData(res.data);
          setWantsEmployeeProfile(true); // We have a profile
        })
        .catch((err) => {
          if (err.response?.status === 404) {
            // No employee profile
            setEmployeeData(null);
            setWantsEmployeeProfile(null); // Show prompt
          } else {
            console.error("Error fetching employee:", err);
          }
        });
    }
  }, [isLoading, isAuthenticated, user?.email, axiosWithAuth]);

  if (isLoading) {
    return <div className="profile-loading">Loading...</div>;
  }

  if (!isAuthenticated || !user) {
    return <div className="profile-container">Please log in to see your profile.</div>;
  }

  return (
    <div className="profile-container">
      <h2>{user.name}</h2>
      <p>{user.email}</p>

      {/* ----- Customer Profile Section ----- */}
      <CustomerProfileSection
        userEmail={user.email}
        wantsProfile={wantsCustomerProfile}
        setWantsProfile={setWantsCustomerProfile}
        customerData={customerData}
        setCustomerData={setCustomerData}
        showForm={showCustomerForm}
        setShowForm={setShowCustomerForm}
      />

      {/* ----- Employee Profile Section ----- */}
      <EmployeeProfileSection
        userEmail={user.email}
        wantsProfile={wantsEmployeeProfile}
        setWantsProfile={setWantsEmployeeProfile}
        employeeData={employeeData}
        setEmployeeData={setEmployeeData}
        showForm={showEmployeeForm}
        setShowForm={setShowEmployeeForm}
      />
    </div>
  );
};

export default Profile;

//
// CUSTOMER PROFILE SECTION
//
interface CustomerSectionProps {
  userEmail: string;
  wantsProfile: boolean | null;
  setWantsProfile: React.Dispatch<React.SetStateAction<boolean | null>>;
  customerData: CustomerResponse | null;
  setCustomerData: React.Dispatch<React.SetStateAction<CustomerResponse | null>>;
  showForm: boolean;
  setShowForm: React.Dispatch<React.SetStateAction<boolean>>;
}

const CustomerProfileSection: React.FC<CustomerSectionProps> = ({
  userEmail,
  wantsProfile,
  setWantsProfile,
  customerData,
  setCustomerData,
  showForm,
  setShowForm,
}) => {
  const axiosWithAuth = useAxiosWithAuth();
  const [isEditing, setIsEditing] = useState(false);

  // Prompts
  const handleYes = () => {
    setWantsProfile(true);
    setShowForm(true);
  };
  const handleNo = () => {
    setWantsProfile(false);
  };

  if (wantsProfile === null) {
    return (
      <div className="profile-prompt">
        <p>Would you like to set up a Customer Profile?</p>
        <button onClick={handleYes}>Yes</button>
        <button onClick={handleNo}>No</button>
      </div>
    );
  }

  if (wantsProfile === false) {
    return (
      <div className="profile-opt-out">
        <p>You have opted out of creating a customer profile.</p>
        <button onClick={() => setWantsProfile(true)}>Create Profile Anyway</button>
      </div>
    );
  }

  // No profile => show "No profile" message or creation form
  if (!customerData && !showForm) {
    return (
      <div className="profile-info">
        <p>No customer profile found yet. Click "Yes" to fill out a form.</p>
      </div>
    );
  }
  // Creating new
  if (!customerData && showForm) {
    return (
      <CustomerProfileForm
        setCustomerData={setCustomerData}
        setShowForm={setShowForm}
        isEditing={false}
        existingCustomer={null}
      />
    );
  }

  // We do have a profile => show read-only or edit form
  if (customerData && !isEditing) {
    return (
      <div className="profile-info">
        <h3>Customer Profile</h3>
        <p>
          <strong>Customer ID:</strong> {customerData.customerId}
        </p>
        <p>
          <strong>First Name:</strong> {customerData.firstName}
        </p>
        <p>
          <strong>Last Name:</strong> {customerData.lastName}
        </p>
        <p>
          <strong>Company:</strong> {customerData.companyName}
        </p>
        <p>
          <strong>Phone:</strong> {customerData.phoneNumber}
        </p>
        <p>
          <strong>Address:</strong> {customerData.address}
        </p>
        <button onClick={() => setIsEditing(true)}>Edit Profile</button>
      </div>
    );
  }

  // If we have a profile and isEditing
  if (customerData && isEditing) {
    return (
      <CustomerProfileForm
        setCustomerData={setCustomerData}
        setShowForm={setShowForm}
        isEditing={true}
        existingCustomer={customerData}
        onDoneEditing={() => setIsEditing(false)}
      />
    );
  }

  return null;
};

//
// CUSTOMER FORM
//
interface CustomerFormProps {
  setCustomerData: React.Dispatch<React.SetStateAction<CustomerResponse | null>>;
  setShowForm: React.Dispatch<React.SetStateAction<boolean>>;
  isEditing: boolean;
  existingCustomer: CustomerResponse | null;
  onDoneEditing?: () => void;
}
const CustomerProfileForm: React.FC<CustomerFormProps> = ({
  setCustomerData,
  setShowForm,
  isEditing,
  existingCustomer,
  onDoneEditing,
}) => {
  const axiosWithAuth = useAxiosWithAuth();
  const [form, setForm] = useState({
    firstName: existingCustomer?.firstName || "",
    lastName: existingCustomer?.lastName || "",
    companyName: existingCustomer?.companyName || "",
    phoneNumber: existingCustomer?.phoneNumber || "",
    address: existingCustomer?.address || "",
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSave = async () => {
    try {
      if (!isEditing) {
        // Creating new
        const response = await axiosWithAuth.post<CustomerResponse>("/customers", {
          firstName: form.firstName,
          lastName: form.lastName,
          companyName: form.companyName,
          phoneNumber: form.phoneNumber,
          address: form.address,
        });
        setCustomerData(response.data);
      } else if (existingCustomer) {
        // Editing existing
        // For example, PUT /customers/{customerId}
        const response = await axiosWithAuth.put<CustomerResponse>(
          `/customers/${existingCustomer.customerId}`,
          {
            firstName: form.firstName,
            lastName: form.lastName,
            companyName: form.companyName,
            phoneNumber: form.phoneNumber,
            address: form.address,
            email: existingCustomer.email, // preserve existing email if needed
          }
        );
        setCustomerData(response.data);
        if (onDoneEditing) onDoneEditing();
      }
      setShowForm(false);
    } catch (err) {
      console.error(isEditing ? "Error updating customer" : "Error creating customer", err);
    }
  };

  const handleCancel = () => {
    if (isEditing && onDoneEditing) onDoneEditing();
    setShowForm(false);
  };

  return (
    <div className="profile-form">
      <h4>{isEditing ? "Edit Customer Profile" : "Create Customer Profile"}</h4>
      <div>
        <label>First Name:</label>
        <input name="firstName" value={form.firstName} onChange={handleChange} />
      </div>
      <div>
        <label>Last Name:</label>
        <input name="lastName" value={form.lastName} onChange={handleChange} />
      </div>
      <div>
        <label>Company:</label>
        <input name="companyName" value={form.companyName} onChange={handleChange} />
      </div>
      <div>
        <label>Phone:</label>
        <input name="phoneNumber" value={form.phoneNumber} onChange={handleChange} />
      </div>
      <div>
        <label>Address:</label>
        <input name="address" value={form.address} onChange={handleChange} />
      </div>
      <button onClick={handleSave}>Save</button>
      {isEditing && (
        <button onClick={handleCancel} style={{ marginLeft: "0.5rem" }}>
          Cancel
        </button>
      )}
    </div>
  );
};

//
// EMPLOYEE PROFILE SECTION
//
interface EmployeeSectionProps {
  userEmail: string;
  wantsProfile: boolean | null;
  setWantsProfile: React.Dispatch<React.SetStateAction<boolean | null>>;
  employeeData: EmployeeResponse | null;
  setEmployeeData: React.Dispatch<React.SetStateAction<EmployeeResponse | null>>;
  showForm: boolean;
  setShowForm: React.Dispatch<React.SetStateAction<boolean>>;
}

const EmployeeProfileSection: React.FC<EmployeeSectionProps> = ({
  userEmail,
  wantsProfile,
  setWantsProfile,
  employeeData,
  setEmployeeData,
  showForm,
  setShowForm,
}) => {
  const [isEditing, setIsEditing] = useState(false);

  const handleYes = () => {
    setWantsProfile(true);
    setShowForm(true); // show the creation form
  };

  const handleNo = () => {
    setWantsProfile(false);
  };

  if (wantsProfile === null) {
    return (
      <div className="profile-prompt">
        <p>Would you like to set up an Employee Profile?</p>
        <button onClick={handleYes}>Yes</button>
        <button onClick={handleNo}>No</button>
      </div>
    );
  }

  if (wantsProfile === false) {
    return (
      <div className="profile-opt-out">
        <p>You have opted out of creating an employee profile.</p>
        <button onClick={() => setWantsProfile(true)}>Create Profile Anyway</button>
      </div>
    );
  }

  // If wantsProfile === true but no data & user hasn't opened form
  if (!employeeData && !showForm) {
    return (
      <div className="profile-info">
        <p>No employee profile found yet. Click "Yes" to fill out a form.</p>
      </div>
    );
  }

  // If user wants to fill out a new employee profile
  if (!employeeData && showForm) {
    return (
      <EmployeeProfileForm
        setEmployeeData={setEmployeeData}
        setShowForm={setShowForm}
        isEditing={false}
        existingEmployee={null}
      />
    );
  }

  // If we do have employeeData but not editing
  if (employeeData && !isEditing) {
    return (
      <div className="employee-profile-section">
        <h3>Employee Profile</h3>
        <p>
          <strong>Employee ID:</strong> {employeeData.employeeId}
        </p>
        <p>
          <strong>First Name:</strong> {employeeData.firstName}
        </p>
        <p>
          <strong>Last Name:</strong> {employeeData.lastName}
        </p>
        <p>
          <strong>Email:</strong> {employeeData.email}
        </p>
        <p>
          <strong>Phone Number:</strong> {employeeData.phoneNumber}
        </p>
        <p>
          <strong>Role:</strong> {employeeData.role}
        </p>
        <button onClick={() => setIsEditing(true)}>Edit Profile</button>
      </div>
    );
  }

  // If employeeData && editing
  if (employeeData && isEditing) {
    return (
      <EmployeeProfileForm
        setEmployeeData={setEmployeeData}
        setShowForm={setShowForm}
        isEditing={true}
        existingEmployee={employeeData}
        onDoneEditing={() => setIsEditing(false)}
      />
    );
  }

  return null;
};

//
// EMPLOYEE FORM
//
interface EmployeeFormProps {
  setEmployeeData: React.Dispatch<React.SetStateAction<EmployeeResponse | null>>;
  setShowForm: React.Dispatch<React.SetStateAction<boolean>>;
  isEditing: boolean;
  existingEmployee: EmployeeResponse | null;
  onDoneEditing?: () => void;
}

const EmployeeProfileForm: React.FC<EmployeeFormProps> = ({
  setEmployeeData,
  setShowForm,
  isEditing,
  existingEmployee,
  onDoneEditing,
}) => {
  const [form, setForm] = useState({
    firstName: existingEmployee?.firstName || "",
    lastName: existingEmployee?.lastName || "",
    phoneNumber: existingEmployee?.phoneNumber || "",
    role: existingEmployee?.role || "employee",
  });
  const axiosWithAuth = useAxiosWithAuth();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSave = async () => {
    try {
      if (!isEditing) {
        // Create new
        const response = await axiosWithAuth.post<EmployeeResponse>("/employees/profile", {
          firstName: form.firstName,
          lastName: form.lastName,
          phoneNumber: form.phoneNumber,
          role: form.role,
          isActive: true,
        });
        setEmployeeData(response.data);
      } else if (existingEmployee) {
        // Update existing
        const response = await axiosWithAuth.put<EmployeeResponse>("/employees/profile", {
          firstName: form.firstName,
          lastName: form.lastName,
          phoneNumber: form.phoneNumber,
          role: form.role,
          isActive: existingEmployee.isActive,
        });
        setEmployeeData(response.data);
        if (onDoneEditing) onDoneEditing();
      }
      setShowForm(false);
    } catch (err) {
      console.error(isEditing ? "Error updating employee" : "Error creating employee", err);
    }
  };

  const handleCancel = () => {
    if (isEditing && onDoneEditing) onDoneEditing();
    setShowForm(false);
  };

  return (
    <div className="profile-form">
      <h4>{isEditing ? "Edit Employee Profile" : "Create Employee Profile"}</h4>
      <div>
        <label>First Name:</label>
        <input name="firstName" value={form.firstName} onChange={handleChange} />
      </div>
      <div>
        <label>Last Name:</label>
        <input name="lastName" value={form.lastName} onChange={handleChange} />
      </div>
      <div>
        <label>Phone Number:</label>
        <input name="phoneNumber" value={form.phoneNumber} onChange={handleChange} />
      </div>
      <div>
        <label>Role:</label>
        <input name="role" value={form.role} onChange={handleChange} />
      </div>
      <button onClick={handleSave}>Save</button>
      {isEditing && (
        <button onClick={handleCancel} style={{ marginLeft: "0.5rem" }}>
          Cancel
        </button>
      )}
    </div>
  );
};
