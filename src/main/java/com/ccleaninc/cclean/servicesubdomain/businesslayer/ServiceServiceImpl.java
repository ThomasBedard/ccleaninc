package com.ccleaninc.cclean.servicesubdomain.businesslayer;

import com.ccleaninc.cclean.servicesubdomain.datalayer.ServiceIdentifier;
import com.ccleaninc.cclean.servicesubdomain.datalayer.ServiceRepository;
import com.ccleaninc.cclean.servicesubdomain.datamapperlayer.ServiceRequestMapper;
import com.ccleaninc.cclean.servicesubdomain.datamapperlayer.ServiceResponseMapper;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceRequestModel;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceResponseModel;
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
@Service
public class ServiceServiceImpl implements ServiceService {

    private ServiceRepository serviceRepository;
    private ServiceResponseMapper serviceResponseMapper;
    private ServiceRequestMapper serviceRequestMapper;

    public ServiceServiceImpl(ServiceRepository serviceRepository, ServiceResponseMapper serviceResponseMapper, ServiceRequestMapper serviceRequestMapper) {
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
        return serviceResponseMapper.entityToResponseModel(serviceRepository.findServiceByServiceIdentifier_ServiceId(serviceId));
    }

    @Override
    public void deleteServiceByServiceId(String serviceId) {
        if (serviceId == null || serviceId.length() != 36) {
            throw new InvalidInputException("Service ID must be a valid 36-character string." + serviceId);
        }
        serviceRepository.delete(serviceRepository.findServiceByServiceIdentifier_ServiceId(serviceId));

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
        com.ccleaninc.cclean.servicesubdomain.datalayer.Service service = new com.ccleaninc.cclean.servicesubdomain.datalayer.Service();
        service.setServiceIdentifier(new ServiceIdentifier());
        service.setTitle(serviceRequestModel.getTitle());
        service.setDescription(serviceRequestModel.getDescription());
        service.setPricing(serviceRequestModel.getPricing());
        service.setCategory(serviceRequestModel.getCategory());
        service.setDurationMinutes(serviceRequestModel.getDurationMinutes());

        com.ccleaninc.cclean.servicesubdomain.datalayer.Service savedService = serviceRepository.save(service);
        return serviceResponseMapper.entityToResponseModel(savedService);
    }

    @Override
    public ServiceResponseModel updateService(String serviceId, ServiceRequestModel serviceRequestModel) {
        com.ccleaninc.cclean.servicesubdomain.datalayer.Service service = serviceRepository.findServiceByServiceIdentifier_ServiceId(serviceId);
        if (service == null) {
            throw new NotFoundException("Service not found with ID: " + serviceId);
        }

        service.setTitle(serviceRequestModel.getTitle());
        service.setDescription(serviceRequestModel.getDescription());
        service.setPricing(serviceRequestModel.getPricing());
        service.setCategory(serviceRequestModel.getCategory());
        service.setDurationMinutes(serviceRequestModel.getDurationMinutes());

        com.ccleaninc.cclean.servicesubdomain.datalayer.Service savedService = serviceRepository.save(service);
        return serviceResponseMapper.entityToResponseModel(savedService);
    }


}




