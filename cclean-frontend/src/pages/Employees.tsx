import { useEffect, useState } from 'react';
import { useAuth0 } from '@auth0/auth0-react';
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
  const { getAccessTokenSilently } = useAuth0();

  useEffect(() => {
    const fetchEmployees = async () => {
      try {
        const token = await getAccessTokenSilently();
        const response = await axios.get("http://localhost:8080/api/v1/employees", {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        setEmployees(response.data);
      } catch (err) {
        setError('Failed to fetch employees: ' + err);
      } finally {
        setLoading(false);
      }
    };
    fetchEmployees();
  }, [getAccessTokenSilently]);

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
    <div>
      <h2 className="text-lg font-bold mb-4">Employees</h2>
      <table className="w-full bg-white border-collapse border border-gray-300">
        <thead>
          <tr className="bg-gray-200">
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
              <td className="border border-gray-300 px-4 py-2">{`${employee.firstName} ${employee.lastName}`}</td>
              <td className="border border-gray-300 px-4 py-2">{employee.email}</td>
              <td className="border border-gray-300 px-4 py-2">{employee.phoneNumber}</td>
              <td className="border border-gray-300 px-4 py-2">{employee.role}</td>
              <td className="border border-gray-300 px-4 py-2">
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

