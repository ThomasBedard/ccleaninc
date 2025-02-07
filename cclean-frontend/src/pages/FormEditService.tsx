import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axiosInstance from '../api/axios';
import './FormAddService.css';
import { toast } from 'react-toastify';

const FormEditService = () => {
  const { serviceId } = useParams<{ serviceId: string }>();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true); // Track loading state
  const [serviceData, setServiceData] = useState({
    title: '',
    description: '',
    pricing: '',
    category: '',
    durationMinutes: '',
  });

  // Fetch service details when component mounts
  useEffect(() => {
    const fetchServiceDetails = async () => {
      if (!serviceId) return;

      try {
        const response = await axiosInstance.get(`/services/${serviceId}`);
        const { title, description, pricing, category, durationMinutes } = response.data;

        setServiceData({
          title,
          description,
          pricing: pricing.toString(), // Ensure it's a string for input
          category,
          durationMinutes: durationMinutes.toString(),
        });
        setLoading(false);
      } catch (error) {
        toast.error('Failed to fetch service details.');
        console.error("Error fetching service:", error);
        setLoading(false);
      }
    };

    fetchServiceDetails();
  }, [serviceId]);

  // Handle input changes
  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;

    // Prevent negative pricing
    if (name === "pricing") {
      const numericValue = parseFloat(value);
      if (numericValue < 0 || isNaN(numericValue)) {
        toast.error("Price cannot be negative.");
        return;
      }
    }

    setServiceData((prev) => ({ ...prev, [name]: value }));
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
      toast.error('All fields must be filled to submit the form.');
      return;
    }

    // Final validation to prevent negative values
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
      });

      toast.success('Service updated successfully!');
      navigate('/services');
    } catch {
      toast.error('Failed to update service. Please try again.');
    }
  };

  return (
    <div className="form-add-service">
      <h2>Edit Service</h2>

      {loading ? (
        <p>Loading service details...</p>
      ) : (
        <>
          <div>
            <label>Title:</label>
            <input
              type="text"
              name="title"
              value={serviceData.title}
              onChange={handleInputChange}
            />
          </div>
          <div>
            <label>Description:</label>
            <textarea
              name="description"
              value={serviceData.description}
              onChange={handleInputChange}
            />
          </div>
          <div>
            <label>Pricing:</label>
            <input
              type="number"
              name="pricing"
              value={serviceData.pricing}
              onChange={handleInputChange}
              min="0" // Prevent negative values at the UI level
            />
          </div>
          <div>
            <label>Category:</label>
            <input
              type="text"
              name="category"
              value={serviceData.category}
              onChange={handleInputChange}
            />
          </div>
          <div>
            <label>Duration (Minutes):</label>
            <input
              type="number"
              name="durationMinutes"
              value={serviceData.durationMinutes}
              onChange={handleInputChange}
            />
          </div>
          <button onClick={handleSubmit}>Submit</button>
        </>
      )}
    </div>
  );
};

export default FormEditService;
