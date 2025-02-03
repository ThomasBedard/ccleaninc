package com.ccleaninc.cclean.emailingsubdomain.businesslayer;

import com.ccleaninc.cclean.emailsubdomain.businesslayer.EmailServiceImpl;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceUnitTest {

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private EmailServiceImpl emailService;

    private Session session;
    private String username = "test@example.com";
    private String password = "password";

    @BeforeEach
    void setUp() {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        emailService = new EmailServiceImpl(username, password, templateEngine);
    }

    @Test
    void sendEmail_Success() throws MessagingException {
        String recipient = "recipient@example.com";
        String subject = "Test Subject";
        String text = "Test Body";

        Transport transport = mock(Transport.class);
        try (var mocked = Mockito.mockStatic(Transport.class)) {
            mocked.when(() -> Transport.send(any(MimeMessage.class))).thenAnswer(invocation -> null);

            int status = emailService.sendEmail(recipient, subject, text);

            assertEquals(HttpStatus.SC_OK, status);
            mocked.verify(() -> Transport.send(any(MimeMessage.class)), times(1));
        }
    }


    @Test
    void sendEmail_Template_Success() throws MessagingException {
        String recipient = "recipient@example.com";
        String subject = "Test Subject";
        String template = "emailTemplate";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", "John Doe");

        when(templateEngine.process(eq(template), any(Context.class))).thenReturn("<html><body>Email Content</body></html>");

        Transport transport = mock(Transport.class);
        try (var mocked = Mockito.mockStatic(Transport.class)) {
            mocked.when(() -> Transport.send(any(MimeMessage.class))).thenAnswer(invocation -> null);

            int status = emailService.sendEmail(recipient, subject, template, parameters);

            assertEquals(HttpStatus.SC_OK, status);
            mocked.verify(() -> Transport.send(any(MimeMessage.class)), times(1));
        }
    }

    @Test
    void sendEmail_TemplateError() throws MessagingException {
        String recipient = "recipient@example.com";
        String subject = "Test Subject";
        String template = "invalidTemplate";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", "John Doe");

        when(templateEngine.process(eq(template), any(Context.class))).thenThrow(new RuntimeException("Template not found"));

        int status = emailService.sendEmail(recipient, subject, template, parameters);

        assertEquals(HttpStatus.SC_UNPROCESSABLE_ENTITY, status);
    }

}
