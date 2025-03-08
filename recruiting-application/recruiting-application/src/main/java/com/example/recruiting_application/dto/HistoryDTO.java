package com.example.recruiting_application.dto;

import java.time.LocalDate;

public class HistoryDTO {


        private Long id;
        private String jobTitle;
        private String status; // 'Accepted', 'Rejected', or 'Under Review'
        private LocalDate interviewDate; // Date of the interview
        private Double aiScore; // AI Score

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getJobTitle() {
            return jobTitle;
        }

        public void setJobTitle(String jobTitle) {
            this.jobTitle = jobTitle;
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

        public Double getAiScore() {
            return aiScore;
        }

        public void setAiScore(Double aiScore) {
            this.aiScore = aiScore;
        }
    }


