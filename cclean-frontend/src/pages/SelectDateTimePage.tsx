import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { Calendar } from "rsuite";
import { useLanguage } from "../hooks/useLanguage"; // ✅ Import translation hook
import { toast } from "react-toastify";
import "rsuite/dist/rsuite.min.css";
import "./SelectDateTimePage.css";

const times = [
  "09:00", "10:00", "11:00", "12:00",
  "13:00", "14:00", "15:00", "16:00",
  "17:00", "18:00", "19:00", "20:00"
];

const SelectDateTimePage: React.FC = () => {
  const { translations } = useLanguage(); // ✅ Get translations dynamically
  const location = useLocation();
  const navigate = useNavigate();
  const { selectedServiceIds } = location.state as { selectedServiceIds: string[] };

  const today = new Date();
  today.setHours(0, 0, 0, 0);

  // ✅ Ensure selectedDate is set when the page loads
  const [selectedDate, setSelectedDate] = useState<Date | null>(today);
  const [selectedTime, setSelectedTime] = useState<string>("");

  useEffect(() => {
    if (!selectedDate) {
      setSelectedDate(today); // ✅ Ensure the time slots always show on load
    }
  }, [selectedDate]);

  const handleDateChange = (date: Date) => {
    if (date < today) {
      toast.error(
        translations.select_date_time?.invalid_date || 
        "You cannot select a past date."
      );
      return;
    }
    setSelectedDate(date);
    setSelectedTime(""); // Reset selected time when date changes
  };

  const handleTimeSelect = (time: string) => {
    if (isTimeInPast(time)) {
      toast.error(
        translations.select_date_time?.invalid_time || 
        "You cannot select a past time."
      );
      return;
    }
    setSelectedTime(time);
  };

  const handleNext = () => {
    if (!selectedDate || !selectedTime) {
      toast.error(
        translations.select_date_time?.error || 
        "Please select a valid date and a valid time."
      );
      return;
    }

    const year = selectedDate.getFullYear();
    const month = String(selectedDate.getMonth() + 1).padStart(2, "0");
    const day = String(selectedDate.getDate()).padStart(2, "0");
    const dateString = `${year}-${month}-${day}T${selectedTime}`;

    navigate("/checkout", {
      state: { selectedServiceIds, appointmentDate: dateString }
    });
  };

  const isTimeInPast = (time: string) => {
    if (!selectedDate) return false;
    const now = new Date();
    const selectedDateTime = new Date(selectedDate);
    const [hours, minutes] = time.split(":").map(Number);
    selectedDateTime.setHours(hours, minutes, 0, 0);
    return selectedDateTime < now;
  };

  return (
    <div className="select-datetime-container">
      <div className="header">
        <h1>{translations.select_date_time?.title || "Select Date & Time"}</h1>
      </div>

      <div className="datetime-content">
        {/* Calendar Section */}
        <div className="calendar-container">
          <Calendar 
            onSelect={handleDateChange} 
            bordered
            compact
            style={{ width: "100%" }} 
          />
          {selectedDate && (
            <div className="selected-date">
              {translations.select_date_time?.selected_date
                ?.replace("{date}", selectedDate.toLocaleDateString()) || 
                `Selected Date: ${selectedDate.toLocaleDateString()}`
              }
            </div>
          )}
        </div>

        {/* Time Slots Section */}
        <div className="select-datetime-time-selection">
          <h2>{translations.select_date_time?.select_time || "Select Time"}</h2>
          <div className="select-datetime-time-buttons">
            {times.map((time) => {
              const isDisabled = isTimeInPast(time);
              return (
                <button
                  key={time}
                  onClick={() => handleTimeSelect(time)}
                  className={`select-datetime-time-button 
                    ${selectedTime === time ? "selected" : ""} 
                    ${isDisabled ? "disabled" : ""}`}
                  disabled={isDisabled}
                >
                  {time}
                </button>
              );
            })}
          </div>
        </div>
      </div>

      {/* Next Button */}
      <button onClick={handleNext} className="next-button">
        {translations.select_date_time?.next_button || "Next"}
      </button>
    </div>
  );
};

export default SelectDateTimePage;
