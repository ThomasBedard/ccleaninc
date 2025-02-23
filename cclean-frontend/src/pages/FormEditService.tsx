import { useState, useEffect, ChangeEvent } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axiosInstance from "../api/axios";
import "./FormAddService.css";
import { toast } from "react-toastify";

const FormEditService = () => {
  const { serviceId } = useParams<{ serviceId: string }>();
  const navigate = useNavigate();

  const [loading, setLoading] = useState(true);
  const [serviceData, setServiceData] = useState({
    title: "",
    description: "",
    pricing: "",
    category: "",
    durationMinutes: "",
    // NEW: Image base64 string from the DB or from a local file input
    image: "",
  });

  // Preview for newly selected file
  const [imagePreview, setImagePreview] = useState<string | null>(null);

  // Fetch service details on mount
  useEffect(() => {
    const fetchServiceDetails = async () => {
      if (!serviceId) return;

      try {
        setLoading(true);
        const response = await axiosInstance.get(`/services/${serviceId}`);
        const {
          title,
          description,
          pricing,
          category,
          durationMinutes,
          image,
        } = response.data;

        setServiceData({
          title,
          description,
          pricing: pricing?.toString() || "",
          category,
          durationMinutes: durationMinutes?.toString() || "",
          image: image || "",
        });

        // If service already has an image in the database, show it in a preview
        if (image) {
          setImagePreview(image); // image is base64 from server
        }
      } catch (error) {
        toast.error("Failed to fetch service details.");
        console.error("Error fetching service:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchServiceDetails();
  }, [serviceId]);

  // Handle text/number input changes
  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
  
    // Ensure input contains only positive numbers
    if (name === "pricing" || name === "durationMinutes") {
      if (!/^\d*$/.test(value)) { // Only allow numbers (no letters, no negative values)
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

    // Final check for negative pricing
    const pricingValue = parseFloat(serviceData.pricing);
    if (pricingValue < 0) {
      toast.error("Price cannot be negative.");
      return;
    }

    try {
      await axiosInstance.put(`/services/${serviceId}`, {
        title: serviceData.title,
        description: serviceData.description,
        pricing: pricingValue,
        category: serviceData.category,
        durationMinutes: parseInt(serviceData.durationMinutes, 10),
        image: serviceData.image, // Include the base64 image in the update
      });

      toast.success("Service updated successfully!");
      // Navigate back to the services list or wherever appropriate
      navigate("/services");
    } catch (error) {
      toast.error("Failed to update service. Please try again.");
      console.error("Error updating service:", error);
    }
  };

  return (
    <div className="form-add-service">
      <h2>Edit Service</h2>

      {loading ? (
        <p>Loading service details...</p>
      ) : (
        <>
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

          {/* NEW: Image Upload */}
          <div style={{ marginTop: "20px" }}>
            <label>Service Image:</label>
            <input type="file" accept="image/*" onChange={handleFileChange} />

            {/* Preview existing or newly chosen image */}
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
            Submit
          </button>
        </>
      )}
    </div>
  );
};

export default FormEditService;
