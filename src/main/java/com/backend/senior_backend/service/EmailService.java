// Java Program to Illustrate Creation Of
// Service Interface

package com.backend.senior_backend.service;

// Importing required classes
import com.backend.senior_backend.Entity.EmailDetails;

// Interface
public interface EmailService {

    // Method
    // To send a simple email
    String sendSimpleMail(EmailDetails details);

    // Method
    // To send an email with attachment
    String sendMailWithAttachment(EmailDetails details);
}
