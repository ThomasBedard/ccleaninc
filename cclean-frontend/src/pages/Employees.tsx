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
    <div className="min-h-screen p-8 bg-gray-50">
      <h1 className="text-3xl font-bold mb-6">Employees</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {employees.map((employee) => (
          <div
            key={employee.employeeId}
            className="bg-white shadow-md rounded-md p-6 hover:shadow-lg"
          >
            <h2 className="text-xl font-semibold mb-2">{`${employee.firstName} ${employee.lastName}`}</h2>
            <p className="text-gray-700 mb-4">Email: {employee.email}</p>
            <p className="text-gray-700 mb-4">Phone: {employee.phoneNumber}</p>
            <p className="text-gray-800 font-medium">Role: {employee.role}</p>
            <p className={`text-sm mt-2 ${employee.isActive ? 'text-green-600' : 'text-red-600'}`}>
              {employee.isActive ? 'Active' : 'Inactive'}
            </p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Employees;
