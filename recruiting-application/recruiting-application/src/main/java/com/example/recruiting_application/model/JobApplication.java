package com.example.recruiting_application.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long jobId;
    private Long userId;
    private String resumeFileName;

    @Lob
    private byte[] resumeData;

    private LocalDate dob; // Date of Birth

    @Column(name = "application_date")
    private LocalDate applicationDate;  // Date of Application Submission

    private Double aiScore; // AI Score for the application
    private String status;  // Application Status
    private LocalDate interviewDate;// Scheduled Interview Date

    private String jobTitle;

    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }



    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    // Default constructor
    public JobApplication() {
    }

    // Parameterized constructor
    public JobApplication(Long jobId, Long userId, LocalDate dob, LocalDate applicationDate) {
        this.jobId = jobId;
        this.userId = userId;
        this.dob = dob;
        this.applicationDate = applicationDate != null ? applicationDate : LocalDate.now(); // Use provided date or current date
    }

    // Getters and setters
    // (No changes needed here)


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getResumeFileName() {
        return resumeFileName;
    }

    public void setResumeFileName(String resumeFileName) {
        this.resumeFileName = resumeFileName;
    }

    public byte[] getResumeData() {
        return resumeData;
    }

    public void setResumeData(byte[] resumeData) {
        this.resumeData = resumeData;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Double getAiScore() {
        return aiScore;
    }

    public void setAiScore(Double aiScore) {
        this.aiScore = aiScore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(LocalDate interviewDate) {
        this.interviewDate = interviewDate;
    }
}
