package com.ccleaninc.cclean.availabilitiessubdomain.presentationlayer;

import com.ccleaninc.cclean.availabilitiessubdomain.businesslayer.AvailabilityService;
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("api/v1/availabilities")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    // Get all availabilities (for admin, etc.)
    @GetMapping
    public ResponseEntity<List<AvailabilityResponseModel>> getAllAvailabilities() {
        List<AvailabilityResponseModel> availabilities = availabilityService.getAllAvailabilities();
        if (availabilities == null || availabilities.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(availabilities);
    }

    // Get availabilities for the logged in employee using the JWT email claim
    @GetMapping("/my-availabilities")
    public ResponseEntity<List<AvailabilityResponseModel>> getMyAvailabilities(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaim("https://ccleaninc.com/email");
        List<AvailabilityResponseModel> availabilities = availabilityService.getAvailabilitiesByEmployeeEmail(email);
        if (availabilities.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(availabilities);
    }

    // Create a new availability for the logged in employee
    @PostMapping("/my-availabilities")
    @PreAuthorize("hasAuthority('employee')")
    public ResponseEntity<AvailabilityResponseModel> createAvailability(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody AvailabilityRequestModel requestModel) {
        try {
            // Extract the employee's email from the token
            String email = jwt.getClaim("https://ccleaninc.com/email");
            // Let the service derive the employee details based on the email and create the availability.
            AvailabilityResponseModel createdAvailability = availabilityService.createAvailabilityForEmployee(email, requestModel);
            return new ResponseEntity<>(createdAvailability, HttpStatus.CREATED);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Get availability by ID (this can remain as-is)
    @GetMapping("/{availabilityId}")
    public ResponseEntity<AvailabilityResponseModel> getAvailabilityByAvailabilityId(
            @PathVariable String availabilityId) {
        try {
            AvailabilityResponseModel availability = availabilityService.getAvailabilityByAvailabilityId(availabilityId);
            return ResponseEntity.ok(availability);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Update an availability belonging to the logged in employee
    @PutMapping("/my-availabilities/{availabilityId}")
    @PreAuthorize("hasAuthority('employee')")
    public ResponseEntity<AvailabilityResponseModel> updateAvailability(
            @PathVariable String availabilityId,
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody AvailabilityRequestModel requestModel) {
        try {
            String email = jwt.getClaim("https://ccleaninc.com/email");
            AvailabilityResponseModel updatedAvailability = availabilityService.updateAvailabilityForEmployee(availabilityId, email, requestModel);
            return ResponseEntity.ok(updatedAvailability);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Delete an availability belonging to the logged in employee
    @DeleteMapping("/my-availabilities/{availabilityId}")
    @PreAuthorize("hasAuthority('employee')")
    public ResponseEntity<Void> deleteAvailabilityByAvailabilityId(
            @PathVariable String availabilityId,
            @AuthenticationPrincipal Jwt jwt) {
        try {
            String email = jwt.getClaim("https://ccleaninc.com/email");
            availabilityService.deleteAvailabilityForEmployee(availabilityId, email);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Get availabilities by employee ID (kept for admin use, for example)
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AvailabilityResponseModel>> getAvailabilitiesByEmployeeId(
            @PathVariable String employeeId) {
        try {
            List<AvailabilityResponseModel> availabilities = availabilityService.getAvailabilitiesByEmployeeId(employeeId);
            if (availabilities.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(availabilities);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Generate PDF of all availabilities
    @GetMapping("/pdf")
    public ResponseEntity<byte[]> generateAvailabilitiesPdf() {
        ByteArrayOutputStream pdfData = availabilityService.generateAvailabilitiesPdf();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "availabilities.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfData.toByteArray());
    }
}
