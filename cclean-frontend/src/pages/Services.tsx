import { useEffect, useState } from 'react';
import axiosInstance from '../api/axios';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './Services.css'; 

interface Service {
  serviceId: string;
  title: string;
  description: string;
  pricing: number;
  isAvailable: boolean;
  category: string;
  durationMinutes: number;
}

const Services = () => {
  const [services, setServices] = useState<Service[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [searchTerm, setSearchTerm] = useState<string>(''); // State for search term
  const navigate = useNavigate();

  // Fetch all services function
  const fetchAllServices = async () => {
    try {
      setLoading(true);
      setError(null);
      const response = await axiosInstance.get<Service[]>('/services');
      setServices(response.data);
    } catch (err) {
      if (axios.isAxiosError(err)) {
        setError(err.response?.data?.message || 'Failed to fetch services. Please try again later.');
      } else {
        setError('An unexpected error occurred.');
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAllServices();
  }, []);

  // Automatically fetch all services when search box is cleared
  useEffect(() => {
    if (searchTerm.trim() === '') {
      fetchAllServices();
    }
  }, [searchTerm]);

  const handleSearch = async () => {
    if (!searchTerm.trim()) {
      alert('Please enter a service title to search.');
      return;
    }

    try {
      setLoading(true);
      setError(null);
      const response = await axiosInstance.get<Service[]>(`/services/search?title=${searchTerm}`);
      setServices(response.data);
    } catch (err) {
      if (axios.isAxiosError(err)) {
        setError(err.response?.data?.message || 'No services found for the given title.');
      } else {
        setError('An unexpected error occurred.');
      }
      setServices([]); // Clear the list if no results
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (serviceId: string) => {
    if (!serviceId || serviceId.length !== 36) {
      alert('Invalid Service ID. Please try again.');
      return;
    }

    const confirmDelete = window.confirm(
      'Are you sure you want to delete this service? This action cannot be undone.'
    );

    if (confirmDelete) {
      try {
        console.log("Attempting to delete service with ID:", serviceId);
        await axiosInstance.delete(`/services/${serviceId}`);
        setServices((prevServices) => prevServices.filter(service => service.serviceId !== serviceId));
        alert('Service deleted successfully!');
      } catch (err) {
        if (axios.isAxiosError(err)) {
          alert(err.response?.data?.message || 'Failed to delete the service. Please try again later.');
        } else {
          alert('An unexpected error occurred while deleting the service.');
        }
      }
    }
  };

  if (loading) {
    return (
      <div className="services-container">
        <p>Loading...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="services-container">
        <p className="error-message">{error}</p>
      </div>
    );
  }

  return (
    <div className="services-page">
      <h1 className="services-title">Residential and Commercial Cleaning Services</h1>
      
      {/* Search Box */}
      <div className="search-bar">
        <input
          type="text"
          placeholder="Search services by title..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
        <button onClick={handleSearch}>Search</button>
      </div>

      <div className="services-grid">
        {services.map((service) => (
          <div key={service.serviceId} className="service-card">
            <div className="service-image">
              <img
                src={`https://via.placeholder.com/300?text=${encodeURIComponent(service.title)}`}
                alt={service.title}
              />
            </div>
            <div className="service-details">
              <h2 className="service-title">{service.title}</h2>
              <p className="service-description">{service.description}</p>
              <p className="service-price">
                {service.pricing > 0 ? `$${service.pricing.toFixed(2)}` : 'Contact for pricing'}
              </p>
              <span
                className={`service-availability ${
                  service.isAvailable ? 'available' : 'unavailable'
                }`}
              >
                {service.isAvailable ? 'Available' : 'Not Available'}
              </span>
              <button
                className="delete-button"
                onClick={() => handleDelete(service.serviceId)}
              >
                Delete
              </button>
            </div>
          </div>
        ))}
      </div>

      <button
        className="add-service-button"
        onClick={() => navigate('/add-service')}
      >
        Add Service
      </button>
    </div>
  );
};

export default Services;
