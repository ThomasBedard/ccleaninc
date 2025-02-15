package com.ccleaninc.cclean.servicesubdomain.businesslayer;

import com.ccleaninc.cclean.servicesubdomain.datalayer.Service;
import com.ccleaninc.cclean.servicesubdomain.datalayer.ServiceIdentifier;
import com.ccleaninc.cclean.servicesubdomain.datalayer.ServiceRepository;
import com.ccleaninc.cclean.servicesubdomain.datamapperlayer.ServiceRequestMapper;
import com.ccleaninc.cclean.servicesubdomain.datamapperlayer.ServiceResponseMapper;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceRequestModel;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceResponseModel;
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;


import java.math.BigDecimal;
import java.util.List;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;
    private final ServiceResponseMapper serviceResponseMapper;
    private final ServiceRequestMapper serviceRequestMapper;

    public ServiceServiceImpl(ServiceRepository serviceRepository,
                              ServiceResponseMapper serviceResponseMapper,
                              ServiceRequestMapper serviceRequestMapper) {
        this.serviceRepository = serviceRepository;
        this.serviceResponseMapper = serviceResponseMapper;
        this.serviceRequestMapper = serviceRequestMapper;
    }

    @Override
    public List<ServiceResponseModel> getAllServices() {
        return serviceResponseMapper.entityToResponseModelList(serviceRepository.findAll());
    }

    @Override
    public ServiceResponseModel getServiceByServiceId(String serviceId) {
        if (serviceId == null || serviceId.length() != 36) {
            throw new InvalidInputException("Service ID must be a valid 36-character string." + serviceId);
        }
        Service foundService = serviceRepository.findServiceByServiceIdentifier_ServiceId(serviceId);
        if (foundService == null) {
            throw new NotFoundException("Service not found with ID: " + serviceId);
        }
        return serviceResponseMapper.entityToResponseModel(foundService);
    }

    @Override
    public void deleteServiceByServiceId(String serviceId) {
        if (serviceId == null || serviceId.length() != 36) {
            throw new InvalidInputException("Service ID must be a valid 36-character string." + serviceId);
        }
        Service foundService = serviceRepository.findServiceByServiceIdentifier_ServiceId(serviceId);
        if (foundService == null) {
            throw new NotFoundException("Service not found with ID: " + serviceId);
        }
        serviceRepository.delete(foundService);
    }

    @Override
    public List<ServiceResponseModel> searchServiceByServiceTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidInputException("Service title cannot be null or empty.");
        }

        var services = serviceRepository.findByTitleContainingIgnoreCase(title);
        if (services.isEmpty()) {
            throw new NotFoundException("No services found with title: " + title);
        }

        return serviceResponseMapper.entityToResponseModelList(services);
    }

    @Override
    public ServiceResponseModel addService(ServiceRequestModel serviceRequestModel) {
        if (serviceRequestModel == null) {
            throw new InvalidInputException("Service request model cannot be null.");
        }

        // Map request model to entity
        Service service = serviceRequestMapper.serviceModelToEntity(serviceRequestModel);

        // Create a new ServiceIdentifier
        service.setServiceIdentifier(new ServiceIdentifier());

        // Ensure isAvailable is set to true if it's null
        if (service.getIsAvailable() == null) {
            service.setIsAvailable(true);
        }

        // Manually set image if present in the request
        service.setImage(serviceRequestModel.getImage());

        // Save the service entity
        Service savedService = serviceRepository.save(service);

        // Convert and return response model
        return serviceResponseMapper.entityToResponseModel(savedService);
    }


    @Override
    public ServiceResponseModel updateService(String serviceId, ServiceRequestModel serviceRequestModel) {
        Service service = serviceRepository.findServiceByServiceIdentifier_ServiceId(serviceId);
        if (service == null) {
            throw new NotFoundException("Service not found with ID: " + serviceId);
        }

        service.setTitle(serviceRequestModel.getTitle());
        service.setDescription(serviceRequestModel.getDescription());
        service.setPricing(serviceRequestModel.getPricing());
        service.setCategory(serviceRequestModel.getCategory());
        service.setDurationMinutes(serviceRequestModel.getDurationMinutes());
        // NEW: update the image as well
        service.setImage(serviceRequestModel.getImage());

        Service savedService = serviceRepository.save(service);
        return serviceResponseMapper.entityToResponseModel(savedService);
    }
}
