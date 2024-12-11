import { useEffect, useState } from 'react';
import axiosInstance from '../api/axios';
import axios from 'axios';
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

  useEffect(() => {
    const fetchServices = async () => {
      try {
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

    fetchServices();
  }, []);

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
      <div className="services-grid">
        {services.map((service) => (
          <div key={service.serviceId} className="service-card">
            <div className="service-image">
              {/* Placeholder for images */}
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
    </div>
  );
};

export default Services;
