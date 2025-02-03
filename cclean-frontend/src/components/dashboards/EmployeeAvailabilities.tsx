import { useEffect, useState } from 'react';
import axiosInstance from '../../api/axios';
import { useNavigate } from 'react-router-dom';
import './EmployeeAvailabilities.css';

interface Availability {
  availabilityId: string;
  employeeId: string;
  employeeFirstName: string;
  employeeLastName: string;
  availableDate: string;
  shift: string;
  comments?: string;
}

const EmployeeAvailabilities: React.FC = () => {
  const [availabilities, setAvailabilities] = useState<Availability[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchAvailabilities = async () => {
      try {
        const response = await axiosInstance.get('/availabilities');
        setAvailabilities(response.data);
      } catch (error) {
        console.error('Error fetching availabilities:', error);
      }
    };

    fetchAvailabilities();
  }, []);

  const handleAssignSchedule = (availability: Availability) => {
    navigate(`/assign-schedule/${availability.availabilityId}`, {
      state: {
        employeeId: availability.employeeId,
        availabilityId: availability.availabilityId,
        availableDate: availability.availableDate,
        shift: availability.shift,
      },
    });
  };  

  return (
    <div className="employee-availabilities-container">
      <h1>Employee Availabilities</h1>
      <table className="employee-availabilities-table">
        <thead>
          <tr>
            <th>Employee Name</th>
            <th>Available Date</th>
            <th>Shift</th>
            <th>Comments</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {availabilities.map((availability) => (
            <tr key={availability.availabilityId}>
              <td>{`${availability.employeeFirstName} ${availability.employeeLastName}`}</td>
              <td>{new Date(availability.availableDate).toLocaleString()}</td>
              <td>{availability.shift}</td>
              <td>{availability.comments || 'N/A'}</td>
              <td>
                <button
                  className="assign-schedule-button"
                  onClick={() => handleAssignSchedule(availability)}
                >
                  Assign Schedule
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default EmployeeAvailabilities;