import React, { useEffect, useState } from "react";
import axiosInstance from "../../api/axios";
import "./AdminDashboard.css";

interface Schedule {
  scheduleId: string;
  employeeId: string;
  assignedDate: string;
  shift: string;
  status: string;
  comments?: string;
}

interface Employee {
  employeeId: string;
  firstName: string;
  lastName: string;
}

const EmployeeSchedule: React.FC = () => {
  const [schedules, setSchedules] = useState<Schedule[]>([]);
  const [employees, setEmployees] = useState<Employee[]>([]);
  const [searchTerm, setSearchTerm] = useState<string>("");
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchSchedules = async () => {
      try {
        const response = await axiosInstance.get("/schedules");
        setSchedules(response.data);
      } catch (err) {
        setError("Failed to fetch schedules. Please try again later." + err);
      }
    };

    const fetchEmployees = async () => {
      try {
        const response = await axiosInstance.get("/employees");
        setEmployees(response.data);
      } catch (err) {
        console.error("Error fetching employees:", err);
      }
    };

    fetchSchedules();
    fetchEmployees();
  }, []);

  const getEmployeeName = (employeeId: string) => {
    const employee = employees.find((emp) => emp.employeeId === employeeId);
    return employee ? `${employee.firstName} ${employee.lastName}` : "Unknown";
  };

  const filteredSchedules = schedules.filter((schedule) =>
    getEmployeeName(schedule.employeeId).toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="admin-dashboard">
      <h2>Employee Schedule</h2>

      <input
        type="text"
        placeholder="Search by employee name"
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        className="search-bar"
      />

      {error && <p className="error-message">{error}</p>}

      <table className="admin-table">
        <thead>
          <tr>
            <th>Employee Name</th>
            <th>Assigned Date</th>
            <th>Shift</th>
            <th>Status</th>
            <th>Comments</th>
          </tr>
        </thead>
        <tbody>
          {filteredSchedules.map((schedule) => (
            <tr key={schedule.scheduleId}>
              <td>{getEmployeeName(schedule.employeeId)}</td>
              <td>{new Date(schedule.assignedDate).toLocaleString()}</td>
              <td>{schedule.shift}</td>
              <td>{schedule.status}</td>
              <td>{schedule.comments || "-"}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default EmployeeSchedule;