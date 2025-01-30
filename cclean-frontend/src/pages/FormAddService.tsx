import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../api/axios';
import './FormAddService.css';
import { toast } from 'react-toastify';

const FormAddService = () => {
  const [newService, setNewService] = useState({
    title: '',
    description: '',
    pricing: '',
    category: '',
    durationMinutes: '',
  });

  const navigate = useNavigate();

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setNewService((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async () => {
    // Check if any field is empty
    if (
      !newService.title.trim() ||
      !newService.description.trim() ||
      !newService.pricing.trim() ||
      !newService.category.trim() ||
      !newService.durationMinutes.trim()
    ) {
      toast.error('All fields are required. Please fill in all the fields.');
      return;
    }

    try {
      await axiosInstance.post('/services', {
        title: newService.title,
        description: newService.description,
        pricing: parseFloat(newService.pricing),
        category: newService.category,
        durationMinutes: parseInt(newService.durationMinutes, 10),
      });
      toast.success('Service created successfully!');
      navigate('/services'); 
    } catch {
      toast.error('Failed to create service. Please try again.');
    }
  };

  return (
    <div className="form-add-service">
      <h2>Add New Service</h2>
      <div>
        <label>Title:</label>
        <input
          type="text"
          name="title"
          value={newService.title}
          onChange={handleInputChange}
          required
        />
      </div>
      <div>
        <label>Description:</label>
        <textarea
          name="description"
          value={newService.description}
          onChange={handleInputChange}
          required
        />
      </div>
      <div>
        <label>Pricing:</label>
        <input
          type="number"
          step="0.01"
          name="pricing"
          value={newService.pricing}
          onChange={handleInputChange}
          required
        />
      </div>
      <div>
        <label>Category:</label>
        <input
          type="text"
          name="category"
          value={newService.category}
          onChange={handleInputChange}
          required
        />
      </div>
      <div>
        <label>Duration (Minutes):</label>
        <input
          type="number"
          name="durationMinutes"
          value={newService.durationMinutes}
          onChange={handleInputChange}
          required
        />
      </div>
      <button onClick={handleSubmit}>Submit</button>
    </div>
  );
};

export default FormAddService;
