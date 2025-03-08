package com.example.recruiting_application.service;

import com.example.recruiting_application.dto.EmailDetails;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDate;

public interface EmailService {
    String simpleMail(EmailDetails emailDetails);

    String sendApplicationSubmissionEmail(String recipient);


    String sendInterviewInvitationEmail(String userEmail, LocalDate interviewDate);

    String sendRejectionMail(String userMail);
}
