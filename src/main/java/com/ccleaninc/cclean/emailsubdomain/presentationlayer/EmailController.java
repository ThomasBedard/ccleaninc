package com.ccleaninc.cclean.emailsubdomain.presentationlayer;

import com.ccleaninc.cclean.emailsubdomain.businesslayer.EmailService;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:5173"},allowCredentials = "true")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test-email")
@Generated
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<Void> sendTestEmail(@RequestBody Map<String, String> payload) throws MessagingException {
        String recipient = payload.get("recipient");
        String subject = "Test Email";
        String template = "appointment.html";

        Map<String, String> parameters = new HashMap<>();
        parameters.put("appointmentDate", "2025-02-01T14:00");
        parameters.put("services", "Cleaning");
        parameters.put("comments", "Please arrive on time");

        int status = emailService.sendEmail(recipient, subject, template, parameters);

        if (status == 200) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(status).build();
        }
    }

}

