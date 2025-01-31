package com.ccleaninc.cclean.availabilitiessubdomain.datamapperlayer;

import com.ccleaninc.cclean.availabilitiessubdomain.datalayer.Availability;
import com.ccleaninc.cclean.availabilitiessubdomain.presentationlayer.AvailabilityRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AvailabilityRequestMapper {
    
    @Mapping(target = "id", ignore = true) // Auto-generated, so ignore during mapping
    @Mapping(target = "availabilityIdentifier", ignore = true) // UUID will be generated
    Availability requestModelToEntity(AvailabilityRequestModel availabilityRequestModel);
}
