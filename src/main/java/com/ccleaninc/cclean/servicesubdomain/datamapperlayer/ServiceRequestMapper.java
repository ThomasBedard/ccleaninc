package com.ccleaninc.cclean.servicesubdomain.datamapperlayer;

import com.ccleaninc.cclean.servicesubdomain.datalayer.Service;
import com.ccleaninc.cclean.servicesubdomain.presentationlayer.ServiceRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "serviceIdentifier", ignore = true)
    Service serviceModelToEntity(ServiceRequestModel serviceRequestModel);
}
