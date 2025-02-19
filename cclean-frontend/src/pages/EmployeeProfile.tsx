// import React, { useEffect, useState } from "react";
// import { useAuth0 } from "@auth0/auth0-react";
// import { useAxiosWithAuth } from "../api/axios";
// import { useNavigate } from "react-router-dom";
// import "./EmployeeProfile.css";

// interface EmployeeResponse {
//   employeeId: string;
//   firstName: string;
//   lastName: string;
//   email: string;
//   phoneNumber: string;
//   role: string;
//   isActive: boolean;
// }

// const EmployeeProfile: React.FC = () => {
//   const { user, isAuthenticated, isLoading } = useAuth0();
//   const axiosWithAuth = useAxiosWithAuth();
//   const navigate = useNavigate();

//   const [employeeData, setEmployeeData] = useState<EmployeeResponse | null>(null);
//   const [isEditing, setIsEditing] = useState(false);

//   const [form, setForm] = useState({
//     firstName: "",
//     lastName: "",
//     phoneNumber: "",
//   });

//   useEffect(() => {
//     // Once the user is authenticated, fetch or create their employee record.
//     const fetchOrCreateEmployee = async () => {
//       if (!isLoading && isAuthenticated && user?.email) {
//         try {
//           // Try to fetch employee by email
//           const response = await axiosWithAuth.get<EmployeeResponse>(
//             `/employees/byEmail?email=${encodeURIComponent(user.email)}`
//           );
//           const existingEmployee = response.data;
//           setEmployeeData(existingEmployee);
//           setForm({
//             firstName: existingEmployee.firstName,
//             lastName: existingEmployee.lastName,
//             phoneNumber: existingEmployee.phoneNumber,
//           });
//         } catch (error: any) {
//           // If not found (404), then create a new employee record
//           if (error.response && error.response.status === 404) {
//             try {
//               const newEmployeeReq = {
//                 firstName: user.given_name || "New",
//                 lastName: user.family_name || "Employee",
//                 email: user.email,
//                 phoneNumber: "",
//                 role: "employee", // Default role for new employees
//                 isActive: true,
//               };
//               const createResponse = await axiosWithAuth.post<EmployeeResponse>(
//                 "/employees",
//                 newEmployeeReq
//               );
//               const newEmployee = createResponse.data;
//               setEmployeeData(newEmployee);
//               setForm({
//                 firstName: newEmployee.firstName,
//                 lastName: newEmployee.lastName,
//                 phoneNumber: newEmployee.phoneNumber,
//               });
//             } catch (createError) {
//               console.error("Error creating employee:", createError);
//             }
//           } else {
//             console.error("Error fetching employee:", error);
//           }
//         }
//       }
//     };

//     fetchOrCreateEmployee();
//   }, [isLoading, isAuthenticated, user, axiosWithAuth]);

//   const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
//     const { name, value } = e.target;
//     setForm((prev) => ({ ...prev, [name]: value }));
//   };

//   const handleSave = async () => {
//     if (!employeeData) return;
//     if (!form.firstName.trim() || !form.lastName.trim()) {
//       alert("First and last name cannot be empty.");
//       return;
//     }

//     const updatedEmployee = {
//       ...employeeData,
//       firstName: form.firstName.trim(),
//       lastName: form.lastName.trim(),
//       phoneNumber: form.phoneNumber.trim(),
//     };

//     try {
//       const response = await axiosWithAuth.put<EmployeeResponse>(
//         `/employees/${employeeData.employeeId}`,
//         updatedEmployee
//       );
//       setEmployeeData(response.data);
//       setIsEditing(false);
//       alert("Profile updated successfully!");
//     } catch (err) {
//       console.error("Error updating employee profile:", err);
//       alert("Error updating employee profile.");
//     }
//   };

//   if (isLoading) {
//     return <div className="employee-profile">Loading...</div>;
//   }

//   if (!isAuthenticated || !user) {
//     return <div className="employee-profile">Please log in to view your profile.</div>;
//   }

//   if (!employeeData) {
//     return <div className="employee-profile">Loading or creating your employee profile...</div>;
//   }

//   return (
//     <div className="employee-profile">
//       <h2>Employee Profile</h2>
//       <p><strong>Email:</strong> {employeeData.email}</p>
//       {!isEditing ? (
//         <>
//           <p><strong>Employee ID:</strong> {employeeData.employeeId}</p>
//           <p><strong>First Name:</strong> {employeeData.firstName}</p>
//           <p><strong>Last Name:</strong> {employeeData.lastName}</p>
//           <p><strong>Phone Number:</strong> {employeeData.phoneNumber || "N/A"}</p>
//           <p><strong>Role:</strong> {employeeData.role}</p>
//           <button onClick={() => setIsEditing(true)}>Edit Profile</button>
//           <button onClick={() => navigate("/")}>Go Back</button>
//         </>
//       ) : (
//         <>
//           <div className="form-group">
//             <label>First Name:</label>
//             <input
//               name="firstName"
//               value={form.firstName}
//               onChange={handleChange}
//             />
//           </div>
//           <div className="form-group">
//             <label>Last Name:</label>
//             <input
//               name="lastName"
//               value={form.lastName}
//               onChange={handleChange}
//             />
//           </div>
//           <div className="form-group">
//             <label>Phone Number:</label>
//             <input
//               name="phoneNumber"
//               value={form.phoneNumber}
//               onChange={handleChange}
//               placeholder="e.g., 123-456-7890"
//             />
//           </div>
//           <button onClick={handleSave}>Save</button>
//           <button onClick={() => setIsEditing(false)}>Cancel</button>
//         </>
//       )}
//     </div>
//   );
// };

// export default EmployeeProfile;
