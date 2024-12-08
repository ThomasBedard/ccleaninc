package com.ccleaninc.cclean.servicesubdomain.datamapperlayer;

import com.ccleaninc.cclean.servicesubdomain.datalayer.Service;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceResponseModel;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceResponseMapper {

    List<ServiceResponseModel> entityToResponseModelList(List<Service> services);
}
