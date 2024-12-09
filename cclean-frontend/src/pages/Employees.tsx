// import { useEffect, useState } from 'react';
// import axiosInstance from '../api/axios';
// import axios from 'axios';

// interface Employee {
//   employeeId: string;
//   firstName: string;
//   lastName: string;
//   email: string;
//   phoneNumber: string;
//   role: string;
//   isActive: boolean;
// }

// const Employees = () => {
//   const [employees, setEmployees] = useState<Employee[]>([]);
//   const [loading, setLoading] = useState<boolean>(true);
//   const [error, setError] = useState<string | null>(null);

//   useEffect(() => {
//     const fetchEmployees = async () => {
//       try {
//         const response = await axiosInstance.get<Employee[]>('/employees');
//         setEmployees(response.data);
//       } catch (err) {
//         if (axios.isAxiosError(err)) {
//           setError(err.response?.data?.message || 'Failed to fetch employees. Please try again later.');
//         } else {
//           setError('An unexpected error occurred.');
//         }
//       } finally {
//         setLoading(false);
//       }
//     };

//     fetchEmployees();
//   }, []);

//   if (loading) {
//     return (
//       <div className="min-h-screen flex items-center justify-center">
//         <p>Loading...</p>
//       </div>
//     );
//   }

//   if (error) {
//     return (
//       <div className="min-h-screen flex items-center justify-center">
//         <p className="text-red-500">{error}</p>
//       </div>
//     );
//   }

//   return (
//     <div className="min-h-screen p-8 bg-gray-50">
//       <h1 className="text-3xl font-bold mb-6">Employees</h1>
//       <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
//         {employees.map((employee) => (
//           <div
//             key={employee.employeeId}
//             className="bg-white shadow-md rounded-md p-6 hover:shadow-lg"
//           >
//             <h2 className="text-xl font-semibold mb-2">{`${employee.firstName} ${employee.lastName}`}</h2>
//             <p className="text-gray-700 mb-4">Email: {employee.email}</p>
//             <p className="text-gray-700 mb-4">Phone: {employee.phoneNumber}</p>
//             <p className="text-gray-800 font-medium">Role: {employee.role}</p>
//             <p className={`text-sm mt-2 ${employee.isActive ? 'text-green-600' : 'text-red-600'}`}>
//               {employee.isActive ? 'Active' : 'Inactive'}
//             </p>
//           </div>
//         ))}
//       </div>
//     </div>
//   );
// };

// export default Employees;

import { useEffect, useState } from 'react';
import axiosInstance from '../api/axios';
import axios from 'axios';

interface Employee {
  employeeId: string;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  role: string;
  isActive: boolean;
}

const Employees = () => {
  const [employees, setEmployees] = useState<Employee[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchEmployees = async () => {
      try {
        const response = await axiosInstance.get<Employee[]>('/employees');
        setEmployees(response.data);
      } catch (err) {
        if (axios.isAxiosError(err)) {
          setError(err.response?.data?.message || 'Failed to fetch employees. Please try again later.');
        } else {
          setError('An unexpected error occurred.');
        }
      } finally {
        setLoading(false);
      }
    };

    fetchEmployees();
  }, []);

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <p>Loading...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <p className="text-red-500">{error}</p>
      </div>
    );
  }

  return (
    <div className="py-6">
      <h1 className="text-2xl font-bold text-gray-800 mb-4">Employees</h1>
      <table className="w-full border-collapse border border-gray-200">
        <thead>
          <tr className="bg-gray-100">
            <th className="border border-gray-300 px-4 py-2">Employee ID</th>
            <th className="border border-gray-300 px-4 py-2">Name</th>
            <th className="border border-gray-300 px-4 py-2">Email</th>
            <th className="border border-gray-300 px-4 py-2">Phone</th>
            <th className="border border-gray-300 px-4 py-2">Role</th>
            <th className="border border-gray-300 px-4 py-2">Status</th>
          </tr>
        </thead>
        <tbody>
          {employees.map((employee) => (
            <tr key={employee.employeeId} className="hover:bg-gray-50">
              <td className="border border-gray-300 px-4 py-2">{employee.employeeId}</td>
              <td className="border border-gray-300 px-4 py-2">
                {employee.firstName} {employee.lastName}
              </td>
              <td className="border border-gray-300 px-4 py-2">{employee.email}</td>
              <td className="border border-gray-300 px-4 py-2">{employee.phoneNumber}</td>
              <td className="border border-gray-300 px-4 py-2">{employee.role}</td>
              <td
                className={`border border-gray-300 px-4 py-2 ${
                  employee.isActive ? 'text-green-600' : 'text-red-600'
                }`}
              >
                {employee.isActive ? 'Active' : 'Inactive'}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Employees;

