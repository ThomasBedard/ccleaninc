package com.ccleaninc.cclean.availabilitiessubdomain.businesslayer;

import com.ccleaninc.cclean.availabilitiessubdomain.presentationlayer.AvailabilityRequestModel;
import com.ccleaninc.cclean.availabilitiessubdomain.presentationlayer.AvailabilityResponseModel;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface AvailabilityService {

        // Retrieve all availability records
        List<AvailabilityResponseModel> getAllAvailabilities();

        // Create a new availability record
        AvailabilityResponseModel createAvailability(AvailabilityRequestModel requestModel);
    
        // Retrieve a specific availability by its unique identifier
        AvailabilityResponseModel getAvailabilityByAvailabilityId(String availabilityId);
    
        // Update an availability record by its unique identifier
        AvailabilityResponseModel updateAvailability(String availabilityId, AvailabilityRequestModel requestModel);
    
        // Delete an availability record by its unique identifier
        void deleteAvailabilityByAvailabilityId(String availabilityId);
    
        // Retrieve all availabilities for a specific employee
        List<AvailabilityResponseModel> getAvailabilitiesByEmployeeId(String employeeId);
    
        // Export all availability records to a PDF
        ByteArrayOutputStream generateAvailabilitiesPdf();

        List<AvailabilityResponseModel> getAvailabilitiesByEmployeeEmail(String email);

    
}
