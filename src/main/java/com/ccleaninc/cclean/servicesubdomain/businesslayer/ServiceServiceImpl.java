package com.ccleaninc.cclean.servicesubdomain.businesslayer;

import com.ccleaninc.cclean.servicesubdomain.datalayer.ServiceRepository;
import com.ccleaninc.cclean.servicesubdomain.datamapperlayer.ServiceRequestMapper;
import com.ccleaninc.cclean.servicesubdomain.datamapperlayer.ServiceResponseMapper;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceResponseModel;
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


}
