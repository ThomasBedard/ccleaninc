import { useEffect, useState, useCallback } from "react";
import axiosInstance from "../api/axios";
import { useNavigate } from "react-router-dom";
import { useLanguage } from "../hooks/useLanguage";
import { useAuth0 } from "@auth0/auth0-react";
import { jwtDecode } from "jwt-decode";
import "./Services.css";
import { toast } from "react-toastify";

interface Service {
  serviceId: string;
  title: string;
  description: string;
  pricing: number;
  isAvailable: boolean;
  category: string;
  durationMinutes: number;
  image?: string;
}

interface DecodedToken {
  permissions: string[];
}

const Services = () => {
  const { translations } = useLanguage();
  const { isAuthenticated, getAccessTokenSilently, loginWithRedirect } =
    useAuth0();
  const [permissions, setPermissions] = useState<string[]>([]);
  const [services, setServices] = useState<Service[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [searchTerm, setSearchTerm] = useState<string>("");
  const [selectedServiceIds, setSelectedServiceIds] = useState<string[]>([]);
  const navigate = useNavigate();

  // ✅ Fetch user permissions
  useEffect(() => {
    const fetchPermissions = async () => {
      try {
        if (!isAuthenticated) return;
        const token = await getAccessTokenSilently();
        const decodedToken: DecodedToken = jwtDecode<DecodedToken>(token);
        setPermissions(decodedToken.permissions || []);
      } catch (error) {
        console.error("Error fetching permissions:", error);
      }
    };

    fetchPermissions();
  }, [isAuthenticated, getAccessTokenSilently]);

  const isAdmin = permissions.includes("admin");

  const fetchAllServices = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const response = await axiosInstance.get<Service[]>("/services");
      setServices(response.data);
    } catch (err) {
      console.error("Error fetching services:", err);
      setError(
        translations.services?.errorMessage ||
          "An error occurred. Please try again later."
      );
    } finally {
      setLoading(false);
    }
  }, [translations.services]);

  useEffect(() => {
    fetchAllServices();
  }, [fetchAllServices]);

  useEffect(() => {
    if (searchTerm.trim() === "") {
      fetchAllServices();
    }
  }, [searchTerm, fetchAllServices]);

  const handleSearch = async () => {
    if (!searchTerm.trim()) {
      toast.error(
        translations.services?.errorMessage ||
          "Please enter a service title to search."
      );
      return;
    }

    try {
      setLoading(true);
      setError(null);
      const response = await axiosInstance.get<Service[]>(
        `/services/search?title=${searchTerm}`
      );
      setServices(response.data);
    } catch (err) {
      console.error("Error searching services:", err);
      setError(
        translations.services?.errorMessage ||
          "No services found for the given title."
      );
      setServices([]);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (serviceId: string) => {
    if (!serviceId || serviceId.length !== 36) {
      toast.error(
        translations.services?.errorMessage ||
          "Invalid Service ID. Please try again."
      );
      return;
    }

    const confirmDelete = window.confirm(
      translations.services?.delete ||
        "Are you sure you want to delete this service?"
    );

    if (confirmDelete) {
      try {
        await axiosInstance.delete(`/services/${serviceId}`);
        setServices((prevServices) =>
          prevServices.filter((service) => service.serviceId !== serviceId)
        );
        toast.success(
          translations.services?.delete || "Service deleted successfully!"
        );
      } catch (err) {
        console.error("Error deleting service:", err);
        toast.error(
          translations.services?.errorMessage ||
            "An unexpected error occurred while deleting the service."
        );
      }
    }
  };

  const handleSelectService = (serviceId: string) => {
    setSelectedServiceIds((prevSelected) =>
      prevSelected.includes(serviceId)
        ? prevSelected.filter((id) => id !== serviceId)
        : [...prevSelected, serviceId]
    );
  };

  const handleContinue = () => {
    if (!isAuthenticated) {
      // Redirect anonymous users to login
      loginWithRedirect();
      return;
    }

    // Proceed to calendar selection page for authenticated users
    navigate("/calendar-select", { state: { selectedServiceIds } });
  };

  return (
    <div className="services-page">
      <h1 className="services-title">
        {translations.services?.title ||
          "Residential and Commercial Cleaning Services"}
      </h1>

      {/* Search Box */}
      <div className="search-bar">
        <input
          type="text"
          placeholder={
            translations.services?.searchPlaceholder ||
            "Search services by title..."
          }
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
        <button onClick={handleSearch}>
          {translations.services?.searchButton || "Search"}
        </button>
      </div>

      {/* Services Grid */}
      <div className="services-grid">
        {loading ? (
          <p className="loading-text">
            {translations.services?.loading || "Loading..."}
          </p>
        ) : error ? (
          <p className="error-message">{error}</p>
        ) : services.length > 0 ? (
          services.map((service) => (
            <div key={service.serviceId} className="service-card">
              <div className="service-image">
                <img
                  src={service.image || `/images/fallback-image.jpg`}
                  alt={service.title}
                />
              </div>
              <div className="service-details">
                <h2 className="service-title">{service.title}</h2>
                <p className="service-description">{service.description}</p>
                <p className="service-price">
                  {service.pricing > 0
                    ? `$${service.pricing.toFixed(2)}`
                    : translations.services?.pricing || "Contact for pricing"}
                </p>
                <span
                  className={`service-availability ${service.isAvailable ? "available" : "unavailable"}`}
                >
                  {service.isAvailable
                    ? translations.services?.availability?.available ||
                      "Available"
                    : translations.services?.availability?.unavailable ||
                      "Not Available"}
                </span>

                {/* Checkbox to select this service */}
                <div style={{ marginTop: "10px" }}>
                  <label>
                    <input
                      type="checkbox"
                      checked={selectedServiceIds.includes(service.serviceId)}
                      onChange={() => handleSelectService(service.serviceId)}
                    />
                    {translations.services?.select || "Select"}
                  </label>
                </div>

                {/* ✅ Only show Edit & Delete buttons if user is an Admin */}
                {isAdmin && (
                  <>
                    <button
                      className="edit-button"
                      onClick={() =>
                        navigate(`/edit-service/${service.serviceId}`)
                      }
                    >
                      {translations.services?.editService || "Edit Service"}
                    </button>
                    <button
                      className="delete-button"
                      onClick={() => handleDelete(service.serviceId)}
                    >
                      {translations.services?.delete || "Delete"}
                    </button>
                  </>
                )}
              </div>
            </div>
          ))
        ) : (
          <p className="no-services-message">
            {translations.services?.noServicesFound ||
              "No services found. Try another search."}
          </p>
        )}
      </div>

      {/* ✅ "Add Service" button only for Admins */}
      {isAdmin && (
        <button
          className="add-service-button"
          onClick={() => navigate("/add-service")}
        >
          {translations.services?.addService || "Add Service"}
        </button>
      )}

      {/* ✅ "Continue with Selected Services" Button (Redirects Anonymous Users to Login) */}
      {selectedServiceIds.length > 0 && (
        <button
          style={{ marginTop: "20px" }}
          onClick={handleContinue} // ✅ Updated click handler
        >
          {translations.services?.continueWithSelected ||
            "Continue with Selected Services"}
        </button>
      )}
    </div>
  );
};

export default Services;
