// Java Program to Illustrate Creation Of
// Service implementation class

package com.backend.senior_backend.service;

// Importing required classes
import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.backend.senior_backend.Entity.EmailDetails;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

// Annotation
@Service
// Class
// Implementing EmailService interface
public class EmailServiceImpl implements EmailService {

    @Autowired private JavaMailSender javaMailSender;

    @Value("hattab.sm@gmail.com") private String sender;

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Override
    public String sendSimpleMail(EmailDetails details) {
        logger.info("Starting email sending process...");

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            logger.info("Sending email to: {}", details.getRecipient());
            logger.info("Email subject: {}", details.getSubject());
            logger.info("Email Sender: {}", sender);
            javaMailSender.send(mailMessage);
            logger.info("Email sent successfully!");

            return "Mail Sent Successfully...";
        } catch (Exception e) {
            logger.error("Error while sending email: ", e);
            return "Error while Sending Mail: " + e.getMessage();
        }
    }
    

    // Method 2
    // To send an email with attachment
    public String
    sendMailWithAttachment(EmailDetails details)
    {
        // Creating a mime message
        MimeMessage mimeMessage
            = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {

            // Setting multipart as true for attachments to
            // be send
            mimeMessageHelper
                = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(
                details.getSubject());

            // Adding the attachment
            FileSystemResource file
                = new FileSystemResource(
                    new File(details.getAttachment()));

            mimeMessageHelper.addAttachment(
                file.getFilename(), file);

            // Sending the mail
            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";
        }

        // Catch block to handle MessagingException
        catch (MessagingException e) {

            // Display message when exception occurred
            return "Error while sending mail!!!";
        }
    }
}
