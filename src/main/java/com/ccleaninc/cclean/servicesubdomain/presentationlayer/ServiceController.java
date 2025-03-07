package com.ccleaninc.cclean.servicesubdomain.presentationlayer;

import com.ccleaninc.cclean.servicesubdomain.businesslayer.ServiceService;
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ServiceController {

    private final ServiceService serviceService;

    @GetMapping("/services")
    public ResponseEntity<List<ServiceResponseModel>> getAllServices() {
        List<ServiceResponseModel> services = serviceService.getAllServices();
        if (services == null || services.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(services);
    }

    @GetMapping("/services/{serviceId}")
    public ResponseEntity<ServiceResponseModel> getServiceByServiceId(@PathVariable String serviceId) {
        try {
            ServiceResponseModel service = serviceService.getServiceByServiceId(serviceId);
            return ResponseEntity.ok().body(service);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/services/{serviceId}")
    public ResponseEntity<Void> deleteServiceByServiceId(@PathVariable String serviceId) {
        try {
            serviceService.deleteServiceByServiceId(serviceId);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/services/search")
    public ResponseEntity<List<ServiceResponseModel>> searchServiceByTitle(@RequestParam String title) {
        try {
            List<ServiceResponseModel> services = serviceService.searchServiceByServiceTitle(title);
            return ResponseEntity.ok().body(services);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/services")
    public ResponseEntity<ServiceResponseModel> addService(@RequestBody ServiceRequestModel serviceRequestModel) {
        ServiceResponseModel service = serviceService.addService(serviceRequestModel);
        if (service != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(service);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/services/{serviceId}")
    public ResponseEntity<ServiceResponseModel> updateService(
            @PathVariable String serviceId,
            @RequestBody ServiceRequestModel serviceRequestModel) {
        try {
            ServiceResponseModel service = serviceService.updateService(serviceId, serviceRequestModel);
            return ResponseEntity.ok().body(service);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
