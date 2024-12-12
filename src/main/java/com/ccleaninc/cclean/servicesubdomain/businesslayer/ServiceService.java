package com.ccleaninc.cclean.servicesubdomain.businesslayer;

import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceRequestModel;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ServiceService {

    List<ServiceResponseModel> getAllServices();

    ServiceResponseModel getServiceByServiceId(String serviceId);

    void deleteServiceByServiceId(String serviceId);

    List<ServiceResponseModel> searchServiceByServiceTitle(String title);

    ServiceResponseModel addService (ServiceRequestModel serviceRequestModel);

    ServiceResponseModel updateService (String serviceId, ServiceRequestModel serviceRequestModel);





}
