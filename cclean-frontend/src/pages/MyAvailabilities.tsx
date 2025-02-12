// import React, { useState } from 'react';
// import axiosInstance from '../api/axios';
// import { useNavigate } from 'react-router-dom';
// import './MyAvailabilities.css';

// interface Availability {
//   availabilityId: string;
//   employeeId: string;
//   employeeFirstName: string;
//   employeeLastName: string;
//   availableDate: string;
//   shift: string;
//   comments?: string;
// }

// const MyAvailabilities: React.FC = () => {
//   const [employeeIdInput, setEmployeeIdInput] = useState('');
//   const [availabilities, setAvailabilities] = useState<Availability[]>([]);
//   const [error, setError] = useState<string | null>(null);
//   const [hasSearched, setHasSearched] = useState(false);
//   const navigate = useNavigate();

//   const handleSubmit = async (e: React.FormEvent) => {
//     e.preventDefault();
//     setError(null);
//     setAvailabilities([]);
//     setHasSearched(true);

//     if (!employeeIdInput.trim()) {
//       setError('Employee ID is required');
//       return;
//     }

//     try {
//       const response = await axiosInstance.get<Availability[]>(
//         `/availabilities/employee/${employeeIdInput.trim()}`
//       );
//       setAvailabilities(response.data);
//     } catch (err) {
//       if (err instanceof Error) {
//         setError(err.message);
//       } else {
//         setError('Failed to load availabilities. Please try again.');
//       }
//     }
//   };

//   const handleDelete = async (availabilityId: string) => {
//     const confirm = window.confirm('Are you sure you want to delete this availability?');
//     if (!confirm) return;

//     try {
//       await axiosInstance.delete(`/availabilities/${availabilityId}`);
//       setAvailabilities(availabilities.filter((avail) => avail.availabilityId !== availabilityId));
//       alert('Availability deleted successfully.');
//     } catch (err) {
//       console.error('Error deleting availability:', err);
//       alert('Failed to delete availability.');
//     }
//   };

//   const handleEdit = (availabilityId: string) => {
//     navigate(`/my-availabilities/edit/${availabilityId}`);
//   };

//   const handleAddAvailability = () => {
//     navigate('/add-availability');
//   };

//   return (
//     <div className="my-availabilities-container">
//       <h1 className="my-availabilities-title">My Availabilities</h1>

//       <form onSubmit={handleSubmit} className="my-availabilities-form">
//         <label>Enter Employee ID: </label>
//         <input
//           type="text"
//           value={employeeIdInput}
//           onChange={(e) => setEmployeeIdInput(e.target.value)}
//         />
//         <button type="submit">View Availabilities</button>
//       </form>

//       {error && <p className="my-availabilities-error">{error}</p>}

//       {hasSearched && !error && availabilities.length === 0 && (
//         <p className="my-availabilities-no-results">No availabilities found for that employee ID.</p>
//       )}

//       {availabilities.length > 0 && (
//         <>
//           <table className="my-availabilities-table">
//             <thead>
//               <tr>
//                 <th>Availability ID</th>
//                 <th>Employee Name</th>
//                 <th>Date/Time</th>
//                 <th>Shift</th>
//                 <th>Comments</th>
//                 <th>Actions</th>
//               </tr>
//             </thead>
//             <tbody>
//               {availabilities.map((avail) => (
//                 <tr key={avail.availabilityId}>
//                   <td>{avail.availabilityId}</td>
//                   <td>{`${avail.employeeFirstName} ${avail.employeeLastName}`}</td>
//                   <td>{avail.availableDate}</td>
//                   <td>{avail.shift}</td>
//                   <td>{avail.comments || 'N/A'}</td>
//                   <td>
//                     <button onClick={() => handleDelete(avail.availabilityId)}>Delete</button>
//                     <button onClick={() => handleEdit(avail.availabilityId)}>Edit</button>
//                   </td>
//                 </tr>
//               ))}
//             </tbody>
//           </table>

//           <button
//             onClick={handleAddAvailability}
//             style={{
//               marginTop: '20px',
//               padding: '10px 20px',
//               backgroundColor: '#007BFF',
//               color: '#fff',
//               border: 'none',
//               borderRadius: '5px',
//               cursor: 'pointer',
//               fontSize: '16px',
//             }}
//           >
//             Add Schedule Availabilities
//           </button>
//         </>
//       )}
//     </div>
//   );
// };

// export default MyAvailabilities;

// import React, { useEffect, useState } from 'react';
// import { useAuth0 } from "@auth0/auth0-react";
// import axiosInstance from '../api/axios';
// import { useNavigate } from 'react-router-dom';
// import './MyAvailabilities.css';

// interface Availability {
//   availabilityId: string;
//   employeeFirstName: string;
//   employeeLastName: string;
//   availableDate: string;
//   shift: string;
//   comments?: string;
// }

// const MyAvailabilities: React.FC = () => {
//   const { getAccessTokenSilently } = useAuth0();
//   const [availabilities, setAvailabilities] = useState<Availability[]>([]);
//   const [error, setError] = useState<string | null>(null);
//   const [loading, setLoading] = useState<boolean>(true);
//   const navigate = useNavigate();

//   useEffect(() => {
//     const fetchAvailabilities = async () => {
//       try {
//         setError(null);
//         setLoading(true);
//         const token = await getAccessTokenSilently(); // Get Auth0 Token

//         const response = await axiosInstance.get<Availability[]>('/availabilities/my-availabilities', {
//           headers: { Authorization: `Bearer ${token}` },
//         });

//         setAvailabilities(response.data);
//       } catch (err) {
//         setError('Failed to load availabilities. Please try again.');
//       } finally {
//         setLoading(false);
//       }
//     };

//     fetchAvailabilities();
//   }, [getAccessTokenSilently]);

//   const handleDelete = async (availabilityId: string) => {
//     const confirm = window.confirm('Are you sure you want to delete this availability?');
//     if (!confirm) return;

//     try {
//       await axiosInstance.delete(`/availabilities/${availabilityId}`);
//       setAvailabilities(availabilities.filter((avail) => avail.availabilityId !== availabilityId));
//       alert('Availability deleted successfully.');
//     } catch (err) {
//       console.error('Error deleting availability:', err);
//       alert('Failed to delete availability.');
//     }
//   };

//   const handleEdit = (availabilityId: string) => {
//     navigate(`/my-availabilities/edit/${availabilityId}`);
//   };

//   const handleAddAvailability = () => {
//     navigate('/add-availability');
//   };

//   return (
//     <div className="my-availabilities-container">
//       <h1 className="my-availabilities-title">My Availabilities</h1>

//       {loading && <p className="my-availabilities-loading">Loading...</p>}

//       {error && <p className="my-availabilities-error">{error}</p>}

//       {!loading && availabilities.length === 0 && (
//         <p className="my-availabilities-no-results">No availabilities found.</p>
//       )}

//       {availabilities.length > 0 && (
//         <>
//           <table className="my-availabilities-table">
//             <thead>
//               <tr>
//                 <th>Availability ID</th>
//                 <th>Employee Name</th>
//                 <th>Date/Time</th>
//                 <th>Shift</th>
//                 <th>Comments</th>
//                 <th>Actions</th>
//               </tr>
//             </thead>
//             <tbody>
//               {availabilities.map((avail) => (
//                 <tr key={avail.availabilityId}>
//                   <td>{avail.availabilityId}</td>
//                   <td>{`${avail.employeeFirstName} ${avail.employeeLastName}`}</td>
//                   <td>{avail.availableDate}</td>
//                   <td>{avail.shift}</td>
//                   <td>{avail.comments || 'N/A'}</td>
//                   <td>
//                     <button onClick={() => handleDelete(avail.availabilityId)}>Delete</button>
//                     <button onClick={() => handleEdit(avail.availabilityId)}>Edit</button>
//                   </td>
//                 </tr>
//               ))}
//             </tbody>
//           </table>

//           <button
//             onClick={handleAddAvailability}
//             style={{
//               marginTop: '20px',
//               padding: '10px 20px',
//               backgroundColor: '#007BFF',
//               color: '#fff',
//               border: 'none',
//               borderRadius: '5px',
//               cursor: 'pointer',
//               fontSize: '16px',
//             }}
//           >
//             Add Schedule Availabilities
//           </button>
//         </>
//       )}
//     </div>
//   );
// };

// export default MyAvailabilities;



import { useEffect, useState } from "react";
import axiosInstance from "../api/axios";
import { useAuth0 } from "@auth0/auth0-react";
import { extractEmailFromToken } from "../api/authUtils";

// Define the Availability interface
interface Availability {
  availabilityId: string;
  shift: string;
}

const MyAvailabilities = () => {
  const { getAccessTokenSilently } = useAuth0();
  const [availabilities, setAvailabilities] = useState<Availability[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [userEmail, setUserEmail] = useState<string | null>(null);

  useEffect(() => {
    const fetchAvailabilities = async () => {
      try {
        // Fetch token from Auth0
        const token = await getAccessTokenSilently();
        const email = extractEmailFromToken(token);

        if (!email) {
          setError("User email not found in token.");
          return;
        }

        setUserEmail(email); // Save user email to state

        // Fetch availabilities for the logged-in user's email
        const response = await axiosInstance.get(`/availabilities/${email}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        setAvailabilities(response.data);
      } catch (error) {
        console.error("❌ Failed to load availabilities:", error);
        setError("Failed to load availabilities. Please try again.");
      }
    };

    fetchAvailabilities();
  }, [getAccessTokenSilently]);

  return (
    <div>
      <h2>My Availabilities</h2>
      {userEmail && <p>Showing availabilities for: {userEmail}</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}
      <ul>
        {availabilities.length > 0 ? (
          availabilities.map((item: Availability) => (
            <li key={item.availabilityId}>{item.shift}</li>
          ))
        ) : (
          <p>No availabilities found.</p>
        )}
      </ul>
    </div>
  );
};

export default MyAvailabilities;
