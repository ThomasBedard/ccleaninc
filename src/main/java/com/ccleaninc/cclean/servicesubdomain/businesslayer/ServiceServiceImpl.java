package com.ccleaninc.cclean.servicesubdomain.businesslayer;

import com.ccleaninc.cclean.servicesubdomain.datalayer.ServiceRepository;
import com.ccleaninc.cclean.servicesubdomain.datamapperlayer.ServiceRequestMapper;
import com.ccleaninc.cclean.servicesubdomain.datamapperlayer.ServiceResponseMapper;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceResponseModel;
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

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


    }



