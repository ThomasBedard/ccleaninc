import { useEffect, useState } from 'react';
import axiosInstance from '../api/axios';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './Services.css'; 
import { toast } from 'react-toastify';

interface Service {
  serviceId: string;
  title: string;
  description: string;
  pricing: number;
  isAvailable: boolean;
  category: string;
  durationMinutes: number;
  image?: string; // Optional field for service image
}

const Services = () => {
  const [services, setServices] = useState<Service[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [searchTerm, setSearchTerm] = useState<string>(''); // State for search term

  // NEW: track selected services in an array of serviceIds
  const [selectedServiceIds, setSelectedServiceIds] = useState<string[]>([]);

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
      toast.error('Please enter a service title to search.');
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
      toast.error('Invalid Service ID. Please try again.');
      return;
    }

    const confirmDelete = window.confirm(
      'Are you sure you want to delete this service? This action cannot be undone.'
    );

    if (confirmDelete) {
      try {
        console.log("Attempting to delete service with ID:", serviceId);
        await axiosInstance.delete(`/services/${serviceId}`);
        setServices((prevServices) =>
          prevServices.filter((service) => service.serviceId !== serviceId)
        );
        toast.success('Service deleted successfully!');
      } catch (err) {
        if (axios.isAxiosError(err)) {
          toast.error(err.response?.data?.message || 'Failed to delete the service. Please try again later.');
        } else {
          toast.error('An unexpected error occurred while deleting the service.');
        }
      }
    }
  };

  // NEW: handle checkbox toggling for multiple service selection
  const handleSelectService = (serviceId: string) => {
    setSelectedServiceIds((prevSelected) => {
      if (prevSelected.includes(serviceId)) {
        // If it's already selected, remove it
        return prevSelected.filter((id) => id !== serviceId);
      } else {
        // Otherwise, add it
        return [...prevSelected, serviceId];
      }
    });
  };

  // If data is still loading
  if (loading) {
    return (
      <div className="services-container">
        <p>Loading...</p>
      </div>
    );
  }

  // If there's an error
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

      {/* Services Grid */}
      <div className="services-grid">
        {services.length > 0 ? (
          services.map((service) => (
            <div key={service.serviceId} className="service-card">
              <div className="service-image">
                <img
                  src={service.image || `/images/fallback-image.jpg`} // Replace `/fallback-image.jpg` with a real local asset
                  alt={service.title}
                  onError={(e) => (e.currentTarget.src = `/images/fallback-image.jpg`)} // Handle broken links
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

                {/* Checkbox to select this service */}
                <div style={{ marginTop: '10px' }}>
                  <label>
                    <input
                      type="checkbox"
                      checked={selectedServiceIds.includes(service.serviceId)}
                      onChange={() => handleSelectService(service.serviceId)}
                    />
                    {' '}Select
                  </label>
                </div>

                {/* Existing buttons */}
                <button
                  className="delete-button"
                  onClick={() => handleDelete(service.serviceId)}
                >
                  Delete
                </button>
                <button
                  className="edit-button"
                  onClick={() => navigate(`/edit-service/${service.serviceId}`)}
                >
                  Edit Service
                </button>
              </div>
            </div>
          ))
        ) : (
          <p className="no-services-message">No services found. Try another search.</p>
        )}
      </div>

      <button
        className="add-service-button"
        onClick={() => navigate('/add-service')}
      >
        Add Service
      </button>

      {/* NEW: Button to continue to next step, passing the selected IDs */}
      {selectedServiceIds.length > 0 && (
        <button
          style={{ marginTop: '20px' }}
          onClick={() => {
            // Navigate to your next step (calendar/time page), passing selected IDs
            navigate('/calendar-select', { state: { selectedServiceIds } });
          }}
        >
          Continue with Selected Services
        </button>
      )}
    </div>
  );
};

export default Services;
