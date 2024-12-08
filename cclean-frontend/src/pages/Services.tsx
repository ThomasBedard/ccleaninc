import { useEffect, useState } from 'react';
import axiosInstance from '../api/axios';
import axios from 'axios'; // Keep only what is needed

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

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <p>Loading...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <p className="text-red-500">{error}</p>
      </div>
    );
  }

  return (
    <div className="min-h-screen p-8 bg-gray-50">
      <h1 className="text-3xl font-bold mb-6">Services</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {services.map((service) => (
          <div
            key={service.serviceId}
            className="bg-white shadow-md rounded-md p-6 hover:shadow-lg"
          >
            <h2 className="text-xl font-semibold mb-2">{service.title}</h2>
            <p className="text-gray-700 mb-4">{service.description}</p>
            <p className="text-gray-800 font-medium">
              Pricing: ${service.pricing.toFixed(2)}
            </p>
            <p className={`text-sm mt-2 ${service.isAvailable ? 'text-green-600' : 'text-red-600'}`}>
              {service.isAvailable ? 'Available' : 'Not Available'}
            </p>
            <p className="text-gray-600 text-sm">Category: {service.category}</p>
            <p className="text-gray-600 text-sm">
              Duration: {service.durationMinutes} minutes
            </p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Services;
