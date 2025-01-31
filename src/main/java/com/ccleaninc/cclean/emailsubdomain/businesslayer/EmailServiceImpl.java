package com.ccleaninc.cclean.emailsubdomain.businesslayer;


import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Properties;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.exceptions.TemplateInputException;

@Service
@Slf4j
@Generated
public class EmailServiceImpl implements EmailService {

    private final String username;
    private final TemplateEngine templateEngine;
    private final Session session;

    @Generated
    public EmailServiceImpl(@Value("${SPRING_MAIL_USERNAME}") String username,
                            @Value("${SPRING_MAIL_PASSWORD}") String password, TemplateEngine templateEngine) {

        this.username = username;
        this.templateEngine = templateEngine;

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        session = Session.getInstance(prop, new Authenticator() {
            @Generated
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        session.setDebug(true); // Enable SMTP debugging
        testSMTPConnection(); // Test SMTP connection on startup
    }

    /**
     * Test SMTP Connection on startup
     */
    private void testSMTPConnection() {
        try {
            log.info("Testing SMTP connection...");
            Transport transport = session.getTransport("smtp");
            transport.connect();
            log.info("✅ SMTP Connection Successful!");
            transport.close();
        } catch (MessagingException e) {
            log.error("❌ SMTP Connection Failed: " + e.getMessage());
        }
    }

    @Override
    public int sendEmail(String recipient, String subject, String text) throws MessagingException {
        try {
            log.info("📧 Preparing to send email to {}", recipient);
            log.info("📧 SMTP Server: smtp.gmail.com, Port: 587");
            log.info("📧 Using sender email: {}", username);

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);
            log.info("✅ Email sent successfully to {}", recipient);
            return HttpStatus.SC_OK;
        } catch (AuthenticationFailedException e) {
            log.error("❌ Authentication Failed: Invalid username/password.");
            return HttpStatus.SC_UNAUTHORIZED;
        } catch (MessagingException e) {
            log.error("❌ Email sending failed: {}", e.getMessage());
            throw new MessagingException("Error sending email: " + e.getMessage());
        } catch (Exception e) {
            log.error("❌ Unexpected error: {}", e.getMessage());
            return HttpStatus.SC_UNPROCESSABLE_ENTITY;
        }
    }

    @Override
    public int sendEmail(String recipient, String subject, String template, Map<String, String> parameters) throws MessagingException {
        try {
            log.info("📧 Preparing to send email to {}", recipient);
            log.info("📢 Email template: {}", template);
            log.info("📢 Email parameters: {}", parameters);

            Context context = new Context();
            parameters.forEach(context::setVariable);

            // ✅ Debug: Check if template exists before processing
            log.info("📢 Checking if template [{}] exists...", template);
            String processedString = templateEngine.process(template, context);
            log.info("✅ Template processed successfully!");

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setContent(processedString, "text/html; charset=utf-8");

            log.info("📧 Sending email to {}", recipient);
            Transport.send(message);
            log.info("✅ Email sent successfully to {}", recipient);

            return HttpStatus.SC_OK;
        } catch (TemplateInputException e) {
            log.error("❌ Template error: {}", e.getMessage(), e);
            return HttpStatus.SC_UNPROCESSABLE_ENTITY;
        } catch (MessagingException e) {
            log.error("❌ Email sending failed: {}", e.getMessage(), e);
            throw new MessagingException("Error sending email: " + e.getMessage());
        } catch (Exception e) {
            log.error("❌ Unexpected error: {}", e.getMessage(), e);
            return HttpStatus.SC_UNPROCESSABLE_ENTITY;
        }
    }
    }


