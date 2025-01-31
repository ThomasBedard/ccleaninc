package com.ccleaninc.cclean.appointmentssubdomain.businesslayer;

import com.ccleaninc.cclean.appointmentssubdomain.datalayer.Appointment;
import com.ccleaninc.cclean.appointmentssubdomain.datalayer.AppointmentIdentifier;
import com.ccleaninc.cclean.appointmentssubdomain.datalayer.AppointmentRepository;
import com.ccleaninc.cclean.appointmentssubdomain.datalayer.Status;
import com.ccleaninc.cclean.appointmentssubdomain.datamapperlayer.AppointmentRequestMapper;
import com.ccleaninc.cclean.appointmentssubdomain.datamapperlayer.AppointmentResponseMapper;
import com.ccleaninc.cclean.appointmentssubdomain.presentationlayer.AppointmentRequestModel;
import com.ccleaninc.cclean.appointmentssubdomain.presentationlayer.AppointmentResponseModel;
import com.ccleaninc.cclean.customerssubdomain.datalayer.Customer;
import com.ccleaninc.cclean.customerssubdomain.datalayer.CustomerRepository;
import com.ccleaninc.cclean.emailsubdomain.businesslayer.EmailService;
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final AppointmentResponseMapper appointmentResponseMapper;
    private final AppointmentRequestMapper appointmentRequestMapper;

    private EmailService emailService;


    @Override
    public List<AppointmentResponseModel> getAllAppointments() {
        return appointmentResponseMapper.entityToResponseModelList(appointmentRepository.findAll());
    }


    @Override
    public AppointmentResponseModel createAppointment(AppointmentRequestModel requestModel) {
        if (requestModel.getCustomerId() == null || requestModel.getCustomerId().isBlank()) {
            throw new InvalidInputException("Customer ID is required.");
        }
        if (requestModel.getAppointmentDate() == null) {
            throw new InvalidInputException("Appointment date/time is required.");
        }

        var optionalCustomer = customerRepository.findByCustomerIdentifier_CustomerId(requestModel.getCustomerId());
        if (optionalCustomer.isEmpty()) {
            throw new NotFoundException("No customer found for ID: " + requestModel.getCustomerId());
        }
        var foundCustomer = optionalCustomer.get();

        Appointment newAppointment = Appointment.builder()
                .appointmentIdentifier(new AppointmentIdentifier()) // auto-generated UUID
                .customerId(foundCustomer.getCustomerIdentifier().getCustomerId())  // store the actual ID
                .customerFirstName(foundCustomer.getFirstName())
                .customerLastName(foundCustomer.getLastName())
                .appointmentDate(requestModel.getAppointmentDate())
                .services(requestModel.getServices())   // e.g. "id1,id2"
                .comments(requestModel.getComments())
                .status(requestModel.getStatus() == null ? Status.pending : requestModel.getStatus())
                .build();

        Appointment savedAppointment = appointmentRepository.save(newAppointment);
        return appointmentResponseMapper.entityToResponseModel(savedAppointment);
    }


    @Override
    public AppointmentResponseModel getAppointmentByAppointmentId(String appointmentId) {
        if (appointmentId == null || appointmentId.length() != 36) {
            throw new InvalidInputException("Appointment ID must be a valid 36-character string: " + appointmentId);
        }
        Appointment appointment = appointmentRepository.findAppointmentByAppointmentIdentifier_AppointmentId(appointmentId);
        if (appointment == null) {
            throw new NotFoundException("Appointment with ID " + appointmentId + " was not found.");
        }
        return appointmentResponseMapper.entityToResponseModel(appointment);
    }


    @Override
    public AppointmentResponseModel addAppointment(AppointmentRequestModel appointmentRequestModel) {
        if (appointmentRequestModel == null) {
            throw new InvalidInputException("Appointment request model cannot be null.");
        }

        Appointment appointment = new Appointment();
        appointment.setAppointmentIdentifier(new AppointmentIdentifier()); // UUID

        appointment.setCustomerFirstName(appointmentRequestModel.getCustomerFirstName());
        appointment.setCustomerLastName(appointmentRequestModel.getCustomerLastName());


        appointment.setCustomerId(UUID.randomUUID().toString());

        appointment.setAppointmentDate(appointmentRequestModel.getAppointmentDate());
        appointment.setServices(appointmentRequestModel.getServices());
        appointment.setComments(appointmentRequestModel.getComments());
        appointment.setStatus(appointmentRequestModel.getStatus());

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return appointmentResponseMapper.entityToResponseModel(savedAppointment);
    }


    @Override
    public AppointmentResponseModel updateAppointment(String appointmentId, AppointmentRequestModel appointmentRequestModel) {
        if (appointmentId == null || appointmentId.length() != 36) {
            throw new InvalidInputException("Appointment ID must be a valid 36-character string: " + appointmentId);
        }
        if (appointmentRequestModel == null) {
            throw new InvalidInputException("Appointment request model cannot be null.");
        }

        Appointment appointment = appointmentRepository.findAppointmentByAppointmentIdentifier_AppointmentId(appointmentId);


        appointment.setCustomerFirstName(appointmentRequestModel.getCustomerFirstName());
        appointment.setCustomerLastName(appointmentRequestModel.getCustomerLastName());

        appointment.setCustomerId(UUID.randomUUID().toString());

        appointment.setAppointmentDate(appointmentRequestModel.getAppointmentDate());
        appointment.setServices(appointmentRequestModel.getServices());
        appointment.setComments(appointmentRequestModel.getComments());
        appointment.setStatus(appointmentRequestModel.getStatus());

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return appointmentResponseMapper.entityToResponseModel(savedAppointment);
    }


    @Override
    public void deleteAppointmentByAppointmentId(String appointmentId) {
        if (appointmentId == null || appointmentId.length() != 36) {
            throw new InvalidInputException("Appointment ID must be a valid 36-character string: " + appointmentId);
        }
        Appointment existing = appointmentRepository.findAppointmentByAppointmentIdentifier_AppointmentId(appointmentId);
        if (existing == null) {
            throw new NotFoundException("Appointment with ID " + appointmentId + " was not found.");
        }
        appointmentRepository.delete(existing);
    }

    @Override
    public List<AppointmentResponseModel> getAppointmentsByCustomerId(String customerId) {
        if (customerId == null || customerId.isBlank()) {
            throw new InvalidInputException("Customer ID cannot be null or empty.");
        }

        var optionalCustomer = customerRepository.findByCustomerIdentifier_CustomerId(customerId);
        if (optionalCustomer.isEmpty()) {
            throw new NotFoundException("No customer found for ID: " + customerId);
        }

        // Use the correct repository method name:
        var appointments = appointmentRepository.findAllByCustomerId(customerId);

        return appointmentResponseMapper.entityToResponseModelList(appointments);
    }

    @Override
    public AppointmentResponseModel updateAppointmentForCustomer(String appointmentId, AppointmentRequestModel requestModel) {
        if (appointmentId == null || appointmentId.length() != 36) {
            throw new InvalidInputException("Appointment ID must be a valid 36-character string.");
        }

        Appointment appointment = appointmentRepository.findAppointmentByAppointmentIdentifier_AppointmentId(appointmentId);
        if (appointment == null) {
            throw new NotFoundException("Appointment not found for " + appointmentId);
        }

        // Update fields but retain the existing customerId
        appointment.setCustomerFirstName(requestModel.getCustomerFirstName());
        appointment.setCustomerLastName(requestModel.getCustomerLastName());
        appointment.setAppointmentDate(requestModel.getAppointmentDate());
        appointment.setServices(requestModel.getServices());
        appointment.setComments(requestModel.getComments());
        appointment.setStatus(requestModel.getStatus());

        Appointment saved = appointmentRepository.save(appointment);
        return appointmentResponseMapper.entityToResponseModel(saved);
    }

    @Override
    public ByteArrayOutputStream generateAppointmentsPdf() {
        List<Appointment> appointments = appointmentRepository.findAll(); // Fetch all appointments
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Add company name at the top left
            Font companyFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Paragraph companyName = new Paragraph("CCLEAN INC.", companyFont);
            companyName.setAlignment(Element.ALIGN_LEFT);
            document.add(companyName);

            document.add(new Paragraph("\n"));

            // Add title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Appointments List", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Create table
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);

            // Add table headers
            addTableHeader(table);

            // Add table data
            for (Appointment appointment : appointments) {
                addTableRow(table, appointment);
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

        PdfPCell cell = new PdfPCell(new Paragraph("ID", headerFont));
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Customer Name", headerFont));
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Date", headerFont));
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Status", headerFont));
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Services", headerFont));
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Comments", headerFont));
        table.addCell(cell);
    }

    private void addTableRow(PdfPTable table, Appointment appointment) {
        table.addCell(appointment.getId().toString());
        table.addCell(appointment.getCustomerFirstName() + " " + appointment.getCustomerLastName());
        table.addCell(appointment.getAppointmentDate().toString());
        table.addCell(appointment.getStatus().toString());
        table.addCell(appointment.getServices());
        table.addCell(appointment.getComments());
    }

    @Override
    public AppointmentResponseModel addAppointmentToCustomerAccount(String customerId, AppointmentRequestModel appointmentRequestModel) throws MessagingException {
        Customer customerAccount = customerRepository.findByCustomerIdentifier_CustomerId(customerId).orElse(null);

        if (customerAccount == null) {
            log.error("❌ Customer account not found for customer ID: {}", customerId);
            throw new RuntimeException("Customer not found with ID: " + customerId);
        }

        // ✅ Debug: Log the services being sent
        log.info("📢 Services received: {}", appointmentRequestModel.getServices());



        // ✅ Fix: Save the first and last name correctly
        Appointment appointment = new Appointment();
        appointment.setAppointmentIdentifier(new AppointmentIdentifier());
        appointment.setCustomerId(appointmentRequestModel.getCustomerId());
        appointment.setCustomerFirstName(appointmentRequestModel.getCustomerFirstName());
        appointment.setCustomerLastName(appointmentRequestModel.getCustomerLastName());
        appointment.setAppointmentDate(appointmentRequestModel.getAppointmentDate());
        appointment.setServices(appointmentRequestModel.getServices());
        appointment.setComments(appointmentRequestModel.getComments());
        appointment.setStatus(appointmentRequestModel.getStatus());

        Appointment savedAppointment = appointmentRepository.save(appointment);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("appointmentDate", savedAppointment.getAppointmentDate().toString());
        parameters.put("services", savedAppointment.getServices());
        parameters.put("comments", savedAppointment.getComments());

        emailService.sendEmail(customerAccount.getEmail(),"Appointment Scheduled - ACMS","appointment.html",parameters);

        return appointmentResponseMapper.entityToResponseModel(savedAppointment);
    }


}
