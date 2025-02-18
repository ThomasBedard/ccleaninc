package com.ccleaninc.cclean.availabilitiessubdomain.businesslayer;

import com.ccleaninc.cclean.availabilitiessubdomain.datalayer.Availability;
import com.ccleaninc.cclean.availabilitiessubdomain.datalayer.AvailabilityIdentifier;
import com.ccleaninc.cclean.availabilitiessubdomain.datalayer.AvailabilityRepository;
import com.ccleaninc.cclean.availabilitiessubdomain.datamapperlayer.AvailabilityRequestMapper;
import com.ccleaninc.cclean.availabilitiessubdomain.datamapperlayer.AvailabilityResponseMapper;
import com.ccleaninc.cclean.availabilitiessubdomain.presentationlayer.AvailabilityRequestModel;
import com.ccleaninc.cclean.availabilitiessubdomain.presentationlayer.AvailabilityResponseModel;
import com.ccleaninc.cclean.employeessubdomain.businesslayer.EmployeeService;
import com.ccleaninc.cclean.employeessubdomain.presentationlayer.EmployeeResponseModel;
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final AvailabilityResponseMapper availabilityResponseMapper;
    private final AvailabilityRequestMapper availabilityRequestMapper;

    @Override
    public List<AvailabilityResponseModel> getAllAvailabilities() {
        return availabilityResponseMapper.entityToResponseModelList(availabilityRepository.findAll());
    }

    @Override
    public AvailabilityResponseModel createAvailability(AvailabilityRequestModel requestModel) {
        if (requestModel.getEmployeeId() == null || requestModel.getEmployeeId().isBlank()) {
            throw new InvalidInputException("Employee ID is required.");
        }
        if (requestModel.getAvailableDate() == null) {
            throw new InvalidInputException("Availability date/time is required.");
        }

        Availability newAvailability = Availability.builder()
                .availabilityIdentifier(new AvailabilityIdentifier())
                .employeeId(requestModel.getEmployeeId())
                .employeeFirstName(requestModel.getEmployeeFirstName())
                .employeeLastName(requestModel.getEmployeeLastName())
                .availableDate(requestModel.getAvailableDate())
                .shift(requestModel.getShift())
                .comments(requestModel.getComments())
                .build();

        Availability savedAvailability = availabilityRepository.save(newAvailability);
        return availabilityResponseMapper.entityToResponseModel(savedAvailability);
    }

    @Override
    public AvailabilityResponseModel getAvailabilityByAvailabilityId(String availabilityId) {
        if (availabilityId == null || availabilityId.length() != 36) {
            throw new InvalidInputException("Availability ID must be a valid 36-character string.");
        }
        Availability availability = availabilityRepository
                .findAvailabilityByAvailabilityIdentifier_AvailabilityId(availabilityId);
        if (availability == null) {
            throw new NotFoundException("Availability with ID " + availabilityId + " was not found.");
        }
        return availabilityResponseMapper.entityToResponseModel(availability);
    }

    @Override
    public AvailabilityResponseModel updateAvailability(String availabilityId, AvailabilityRequestModel requestModel) {
        if (availabilityId == null || availabilityId.length() != 36) {
            throw new InvalidInputException("Availability ID must be a valid 36-character string.");
        }
        if (requestModel == null) {
            throw new InvalidInputException("Availability request model cannot be null.");
        }

        Availability availability = availabilityRepository
                .findAvailabilityByAvailabilityIdentifier_AvailabilityId(availabilityId);
        if (availability == null) {
            throw new NotFoundException("Availability with ID " + availabilityId + " was not found.");
        }

        // Update fields
        availability.setAvailableDate(requestModel.getAvailableDate());
        availability.setShift(requestModel.getShift());
        availability.setComments(requestModel.getComments());

        Availability savedAvailability = availabilityRepository.save(availability);
        return availabilityResponseMapper.entityToResponseModel(savedAvailability);
    }

    @Override
    public void deleteAvailabilityByAvailabilityId(String availabilityId) {
        if (availabilityId == null || availabilityId.length() != 36) {
            throw new InvalidInputException("Availability ID must be a valid 36-character string.");
        }
        Availability availability = availabilityRepository
                .findAvailabilityByAvailabilityIdentifier_AvailabilityId(availabilityId);
        if (availability == null) {
            throw new NotFoundException("Availability with ID " + availabilityId + " was not found.");
        }
        availabilityRepository.delete(availability);
    }

    @Override
    public List<AvailabilityResponseModel> getAvailabilitiesByEmployeeId(String employeeId) {
        if (employeeId == null || employeeId.isBlank()) {
            throw new InvalidInputException("Employee ID cannot be null or empty.");
        }
        List<Availability> availabilities = availabilityRepository.findAllByEmployeeId(employeeId);
        return availabilityResponseMapper.entityToResponseModelList(availabilities);
    }

    private final EmployeeService employeeService; // Inject EmployeeService

    @Override
    public List<AvailabilityResponseModel> getAvailabilitiesByEmployeeEmail(String email) {
        // Lookup Employee by Email
        EmployeeResponseModel employee = employeeService.getEmployeeByEmail(email);
        if (employee == null) {
            throw new NotFoundException("Employee not found for email: " + email);
        }

        // Fetch availabilities using Employee ID
        List<Availability> availabilities = availabilityRepository.findAllByEmployeeId(employee.getEmployeeId());
        return availabilityResponseMapper.entityToResponseModelList(availabilities);
    }

    @Override
    public ByteArrayOutputStream generateAvailabilitiesPdf() {
        List<Availability> availabilities = availabilityRepository.findAll();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Add company name
            Font companyFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Paragraph companyName = new Paragraph("CCLEAN INC.", companyFont);
            companyName.setAlignment(Element.ALIGN_LEFT);
            document.add(companyName);

            document.add(new Paragraph("\n"));

            // Add title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Employee Availabilities List", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Create table
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);

            // Add table headers
            addTableHeader(table);

            // Add table data
            for (Availability availability : availabilities) {
                addTableRow(table, availability);
            }

            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        return outputStream;
    }

    private void addTableHeader(PdfPTable table) {
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

        PdfPCell cell = new PdfPCell(new Paragraph("Employee ID", headerFont));
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Employee Name", headerFont));
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Available Date", headerFont));
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Shift", headerFont));
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Comments", headerFont));
        table.addCell(cell);
    }

    private void addTableRow(PdfPTable table, Availability availability) {
        table.addCell(availability.getEmployeeId());
        table.addCell(availability.getEmployeeFirstName() + " " + availability.getEmployeeLastName());
        table.addCell(availability.getAvailableDate().toString());
        table.addCell(availability.getShift().toString());
        table.addCell(availability.getComments());
    }

    // --- New Methods for JWT-based Operations ---

    @Override
    public AvailabilityResponseModel createAvailabilityForEmployee(String email, AvailabilityRequestModel requestModel) {
        EmployeeResponseModel employee = employeeService.getEmployeeByEmail(email);
        if (employee == null) {
            throw new NotFoundException("Employee not found for email: " + email);
        }
        // Build a new request model using employee details from JWT
        AvailabilityRequestModel modifiedRequest = AvailabilityRequestModel.builder()
                .employeeId(employee.getEmployeeId())
                .employeeFirstName(employee.getFirstName())
                .employeeLastName(employee.getLastName())
                .availableDate(requestModel.getAvailableDate())
                .shift(requestModel.getShift())
                .comments(requestModel.getComments())
                .build();
        return createAvailability(modifiedRequest);
    }

    @Override
    public AvailabilityResponseModel updateAvailabilityForEmployee(String availabilityId, String email, AvailabilityRequestModel requestModel) {
        EmployeeResponseModel employee = employeeService.getEmployeeByEmail(email);
        if (employee == null) {
            throw new NotFoundException("Employee not found for email: " + email);
        }
        Availability availability = availabilityRepository.findAvailabilityByAvailabilityIdentifier_AvailabilityId(availabilityId);
        if (availability == null) {
            throw new NotFoundException("Availability with ID " + availabilityId + " was not found.");
        }
        if (!availability.getEmployeeId().equals(employee.getEmployeeId())) {
            throw new InvalidInputException("You do not have permission to update this availability.");
        }
        // Update allowed fields: availableDate, shift, and comments
        availability.setAvailableDate(requestModel.getAvailableDate());
        availability.setShift(requestModel.getShift());
        availability.setComments(requestModel.getComments());
        Availability savedAvailability = availabilityRepository.save(availability);
        return availabilityResponseMapper.entityToResponseModel(savedAvailability);
    }

    @Override
    public void deleteAvailabilityForEmployee(String availabilityId, String email) {
        EmployeeResponseModel employee = employeeService.getEmployeeByEmail(email);
        if (employee == null) {
            throw new NotFoundException("Employee not found for email: " + email);
        }
        Availability availability = availabilityRepository.findAvailabilityByAvailabilityIdentifier_AvailabilityId(availabilityId);
        if (availability == null) {
            throw new NotFoundException("Availability with ID " + availabilityId + " was not found.");
        }
        if (!availability.getEmployeeId().equals(employee.getEmployeeId())) {
            throw new InvalidInputException("You do not have permission to delete this availability.");
        }
        availabilityRepository.delete(availability);
    }
}
