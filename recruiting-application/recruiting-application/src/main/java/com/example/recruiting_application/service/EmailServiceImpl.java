package com.example.recruiting_application.service;

import com.example.recruiting_application.dto.EmailDetails;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

import java.time.LocalDate;

@Service
public class EmailServiceImpl implements EmailService{
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") private String sender;

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Override
    public String simpleMail(EmailDetails emailDetails) {
        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(emailDetails.getRecipient());
            mailMessage.setText(emailDetails.getMsgBody());
            mailMessage.setSubject(emailDetails.getSubject());

            javaMailSender.send(mailMessage);
            logger.info("Mail Sent Successfully");
            return "Mail Sent Successfully!";
        }catch (Exception e){
            logger.error("Error While Sending Mail");
            return "Error While Sending Mail" + e.getMessage();
        }
    }
    @Override
    public String sendApplicationSubmissionEmail(String recipient){
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(recipient);
        emailDetails.setSubject("Application Submitted Successfully");
        emailDetails.setMsgBody("Dear Candidate,\n\nYour application has been submitted successfully. We will review it and get back to you soon.\n\nBest regards,\nRecruiting Team");

        return simpleMail(emailDetails);
    }

    @Override
    public String sendInterviewInvitationEmail(String userEmail, LocalDate interviewDate) {
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(userEmail);
        emailDetails.setMsgBody("Dear Candidate,\n\nWe are pleased to inform you that you have been selected for an interview scheduled on "  + interviewDate + " Please check your email for further details regarding the schedule and next steps.\n\nBest regards,\nRecruiting Team");
        emailDetails.setSubject("Interview Invitation");
        return simpleMail(emailDetails);

    }

    @Override
    public String sendRejectionMail(String userMail) {
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(userMail);
        emailDetails.setSubject("Application Status");
        emailDetails.setMsgBody("Thank you for your interest in the position and for the time you invested in the interview process. After careful consideration, we regret to inform you that we will not be moving forward with your application at this time.\n\nBest regards,\nRecruiting Team");
        return simpleMail(emailDetails);
    }


    public void sendSimpleMail(EmailDetails details) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(details.getRecipient());
        mailMessage.setSubject(details.getSubject());
        mailMessage.setText(details.getMsgBody());

        // Sending the mail
        javaMailSender.send(mailMessage);
    }
}
