package com.ccleaninc.cclean.availabilitiessubdomain.datamapperlayer;

import com.ccleaninc.cclean.availabilitiessubdomain.datalayer.Availability;
import com.ccleaninc.cclean.availabilitiessubdomain.presentationlayer.AvailabilityResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AvailabilityResponseMapper {
    
        // Map individual Availability entity to AvailabilityResponseModel
        @Mapping(expression = "java(availability.getAvailabilityIdentifier().getAvailabilityId())", target = "availabilityId")
        @Mapping(target = "employeeId", source = "availability.employeeId")
        @Mapping(target = "shift", source = "availability.shift")
        AvailabilityResponseModel entityToResponseModel(Availability availability);
    
        // Map a list of Availability entities to a list of AvailabilityResponseModels
        List<AvailabilityResponseModel> entityToResponseModelList(List<Availability> availabilities);
}
