import { useState, ChangeEvent } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import axiosInstance from "../api/axios";
import "./FormAddService.css";

const AddService = () => {
  const navigate = useNavigate();

  // Initialize empty form fields
  const [serviceData, setServiceData] = useState({
    title: "",
    description: "",
    pricing: "",
    category: "",
    durationMinutes: "",
    // For image
    image: "",
  });

  const [imagePreview, setImagePreview] = useState<string | null>(null);

  // Handle text/number input changes
  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
  
    // Ensure input contains only positive numbers
    if (name === "pricing" || name === "durationMinutes") {
      if (!/^\d*$/.test(value)) { // Only allow digits (no letters, no negative values)
        return;
      }
      if (value.length > 0 && parseInt(value, 10) < 1) {
        toast.error(`${name === "pricing" ? "Price" : "Duration"} must be a positive number.`);
        return;
      }
    }
  
    setServiceData((prev) => ({ ...prev, [name]: value }));
  };
  

  // Handle file input (image)
  const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;

    // Optional: double-check MIME type
    if (!(file.type === "image/jpeg" || file.type === "image/png")) {
      toast.error("Only JPEG or PNG files are allowed.");
      return;
    }

    // Convert to Base64 and store
    const reader = new FileReader();
    reader.onloadend = () => {
      const base64String = reader.result as string;
      setServiceData((prev) => ({ ...prev, image: base64String }));
      setImagePreview(base64String);
    };
    reader.readAsDataURL(file);
  };

  // Handle form submission
  const handleSubmit = async () => {
    // Basic validation
    if (
      !serviceData.title.trim() ||
      !serviceData.description.trim() ||
      !serviceData.pricing.trim() ||
      !serviceData.category.trim() ||
      !serviceData.durationMinutes.trim()
    ) {
      toast.error("All fields must be filled to submit the form.");
      return;
    }

    // Convert pricing/duration to numeric
    const pricingValue = parseFloat(serviceData.pricing);
    if (pricingValue < 0) {
      toast.error("Price cannot be negative.");
      return;
    }

    try {
      // Call POST /services
      await axiosInstance.post("/services", {
        ...serviceData,
        pricing: pricingValue,
        durationMinutes: parseInt(serviceData.durationMinutes, 10),
      });

      toast.success("Service created successfully!");
      navigate("/services"); // Go back to the main services page
    } catch (error) {
      toast.error("Failed to create service. Please try again.");
      console.error("Error creating service:", error);
    }
  };

  return (
    <div className="form-add-service">
      <h2>Add Service</h2>

      {/* Title */}
      <div>
        <label>Title:</label>
        <input
          type="text"
          name="title"
          value={serviceData.title}
          onChange={handleInputChange}
        />
      </div>

      {/* Description */}
      <div>
        <label>Description:</label>
        <textarea
          name="description"
          value={serviceData.description}
          onChange={handleInputChange}
        />
      </div>

      {/* Pricing */}
      <div>
        <label>Pricing:</label>
        <input
          type="text" // Changed from "number" to "text" to allow better validation
          name="pricing"
          value={serviceData.pricing}
          onChange={handleInputChange}
          placeholder="Enter price (e.g. 50)"
        />
      </div>

      {/* Category */}
      <div>
        <label>Category:</label>
        <input
          type="text"
          name="category"
          value={serviceData.category}
          onChange={handleInputChange}
        />
      </div>

      {/* Duration */}
      <div>
        <label>Duration (Minutes):</label>
        <input
          type="text" // Changed from "number" to "text" to allow better validation
          name="durationMinutes"
          value={serviceData.durationMinutes}
          onChange={handleInputChange}
          placeholder="Enter duration (e.g. 30)"
        />
      </div>

      {/* Image Upload */}
      <div style={{ marginTop: "20px" }}>
        <label>Service Image:</label>
        <input type="file" accept="image/*" onChange={handleFileChange} />
        {imagePreview && (
          <div style={{ marginTop: "10px" }}>
            <img
              src={imagePreview}
              alt="Service Preview"
              style={{ maxWidth: "300px", maxHeight: "300px" }}
            />
          </div>
        )}
      </div>

      {/* Submit button */}
      <button style={{ marginTop: "20px" }} onClick={handleSubmit}>
        Create Service
      </button>
    </div>
  );
};

export default AddService;
