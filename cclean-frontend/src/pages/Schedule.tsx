import { useEffect, useState } from "react";
import axios from "axios";
import { useLanguage } from "../hooks/useLanguage"; // ✅ Import translation hook

type Schedule = {
  id: string;
  employeeId: string;
  serviceId: string;
  customerId: string;
  startTime: string;
  endTime: string;
  status: string;
  location: string;
};

const Schedule = () => {
  const { translations } = useLanguage(); // ✅ Fetch translations
  const [schedules, setSchedules] = useState<Schedule[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchSchedules = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/schedules");
        setSchedules(response.data);
      } catch {
        setError(translations.schedule?.error || "Failed to fetch schedules.");
      } finally {
        setLoading(false);
      }
    };
    fetchSchedules();
  }, []);

  if (loading) return <div>{translations.schedule?.loading || "Loading schedules..."}</div>;
  if (error) return <div className="text-red-500">{error}</div>;

  return (
    <div className="py-6">
      <h1 className="text-2xl font-bold text-gray-800 mb-4">
        {translations.schedule?.title || "Employee Schedules"}
      </h1>
      <table className="w-full border-collapse border border-gray-200">
        <thead>
          <tr className="bg-gray-100">
            <th className="border border-gray-300 px-4 py-2">
              {translations.schedule?.table?.employee_id || "Employee ID"}
            </th>
            <th className="border border-gray-300 px-4 py-2">
              {translations.schedule?.table?.service_id || "Service ID"}
            </th>
            <th className="border border-gray-300 px-4 py-2">
              {translations.schedule?.table?.customer_id || "Customer ID"}
            </th>
            <th className="border border-gray-300 px-4 py-2">
              {translations.schedule?.table?.start_time || "Start Time"}
            </th>
            <th className="border border-gray-300 px-4 py-2">
              {translations.schedule?.table?.end_time || "End Time"}
            </th>
            <th className="border border-gray-300 px-4 py-2">
              {translations.schedule?.table?.status || "Status"}
            </th>
            <th className="border border-gray-300 px-4 py-2">
              {translations.schedule?.table?.location || "Location"}
            </th>
          </tr>
        </thead>
        <tbody>
          {schedules.map((schedule) => (
            <tr key={schedule.id} className="hover:bg-gray-50">
              <td className="border border-gray-300 px-4 py-2">
                {schedule.employeeId}
              </td>
              <td className="border border-gray-300 px-4 py-2">
                {schedule.serviceId}
              </td>
              <td className="border border-gray-300 px-4 py-2">
                {schedule.customerId}
              </td>
              <td className="border border-gray-300 px-4 py-2">
                {new Date(schedule.startTime).toLocaleString()}
              </td>
              <td className="border border-gray-300 px-4 py-2">
                {new Date(schedule.endTime).toLocaleString()}
              </td>
              <td className="border border-gray-300 px-4 py-2">
                {schedule.status}
              </td>
              <td className="border border-gray-300 px-4 py-2">
                {schedule.location}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Schedule;
