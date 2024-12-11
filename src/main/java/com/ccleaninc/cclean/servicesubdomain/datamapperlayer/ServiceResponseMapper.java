package com.ccleaninc.cclean.servicesubdomain.datamapperlayer;

import com.ccleaninc.cclean.servicesubdomain.datalayer.Service;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceResponseMapper {

    @Mapping(expression = "java(service.getServiceIdentifier().getServiceId())", target = "serviceId")
    ServiceResponseModel entityToResponseModel(Service service);
    List<ServiceResponseModel> entityToResponseModelList(List<Service> services);

}
