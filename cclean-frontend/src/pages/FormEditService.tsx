import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axiosInstance from '../api/axios';
import './FormAddService.css';

const FormEditService = () => {
  const { serviceId } = useParams<{ serviceId: string }>();
  const navigate = useNavigate();
  const [serviceData, setServiceData] = useState({
    title: '',
    description: '',
    pricing: '',
    category: '',
    durationMinutes: '',
  });

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setServiceData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async () => {
    // Validation: Ensure all fields are filled
    if (
      !serviceData.title.trim() ||
      !serviceData.description.trim() ||
      !serviceData.pricing.trim() ||
      !serviceData.category.trim() ||
      !serviceData.durationMinutes.trim()
    ) {
      alert('All fields must be filled to submit the form.');
      return;
    }

    try {
      await axiosInstance.put(`/services/${serviceId}`, {
        title: serviceData.title,
        description: serviceData.description,
        pricing: parseFloat(serviceData.pricing),
        category: serviceData.category,
        durationMinutes: parseInt(serviceData.durationMinutes, 10),
      });
      alert('Service updated successfully!');
      navigate('/services');
    } catch {
      alert('Failed to update service. Please try again.');
    }
  };

  return (
    <div className="form-add-service">
      <h2>Edit Service</h2>
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
    </div>
  );
};

export default FormEditService;
