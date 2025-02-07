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
@RequestMapping("/api/v1/contact")
@Generated
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendContactEmail(@RequestBody Map<String, String> payload) {
        try {
            String recipient = "viniciusvelozodesousa@gmail.com";
            String subject = payload.getOrDefault("subject", "New Contact Message from Website");

            // ✅ Extract user details
            String senderName = payload.get("name");
            String senderEmail = payload.get("email");
            String messageBody = payload.get("message");
            String language = payload.getOrDefault("language", "en"); // Default to English

            if (senderName == null || senderEmail == null || messageBody == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Missing required fields"));
            }

            // ✅ Email content parameters
            Map<String, String> parameters = new HashMap<>();
            parameters.put("senderName", senderName);
            parameters.put("senderEmail", senderEmail);
            parameters.put("message", messageBody);
            parameters.put("language", language); // ✅ Ensure the language is passed

            // ✅ Send email
            int status = emailService.sendEmail(recipient, subject, "contact.html", parameters);

            if (status == 200) {
                return ResponseEntity.ok(Map.of("message", "Email sent successfully"));
            } else {
                return ResponseEntity.status(status).body(Map.of("error", "Failed to send email"));
            }
        } catch (MessagingException e) {
            log.error("Error sending contact email", e);
            return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error"));
        }
    }
}
