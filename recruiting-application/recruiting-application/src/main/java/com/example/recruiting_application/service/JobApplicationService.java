package com.example.recruiting_application.service;

import com.example.recruiting_application.dto.HistoryDTO;
import com.example.recruiting_application.dto.JobApplicationDTO;
import com.example.recruiting_application.model.JobApplication;
import com.example.recruiting_application.repository.JobApplicationRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobApplicationService {
    private JobApplicationRepo jobApplicationRepo;

    public JobApplicationService(JobApplicationRepo jobApplicationRepo) {
        this.jobApplicationRepo = jobApplicationRepo;
    }

    public JobApplication applyForJob(JobApplication jobApplication) {
        return jobApplicationRepo.save(jobApplication);
    }


    public List<JobApplicationDTO> getAllApplications() {
        return jobApplicationRepo.findAllApplicationsWithUserDetails();
    }
//    public JobApplication getApplicationById(Long id) {
//        Optional<JobApplication> optionalApplication = jobApplicationRepo.findById(id);
//        return optionalApplication.orElse(null); // Return the application if found, else null
//    }


    public boolean existsByUserIdAndJobId(Long userId, Long jobId) {
        return jobApplicationRepo.existsByUserIdAndJobId(userId, jobId);
    }

    public JobApplication getApplicationById(Long id) {
        return jobApplicationRepo.findById(id).orElse(null);
    }

    public void save(JobApplication jobApplication) {
        jobApplicationRepo.save(jobApplication);
    }

//    public void delete(JobApplication jobApplication) {
//        jobApplicationRepo.delete(jobApplication);
//    }

    public void update(JobApplication jobApplication) {
        jobApplicationRepo.save(jobApplication);
    }

    public long countAllApplications() {
        return jobApplicationRepo.count();
    }

    public long countApplicationsByStatus(String status) {
        return jobApplicationRepo.countByStatus(status);
    }


    public long countAllApplicationsBetweenDates(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return jobApplicationRepo.countByApplicationDateBetween(startDate, endDate);
        } else {
            return jobApplicationRepo.count();
        }
    }

    public long countApplicationsByStatusAndDates(String status, LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return jobApplicationRepo.countByStatusAndApplicationDateBetween(status, startDate, endDate);
        } else {
            return jobApplicationRepo.countByStatus(status);
        }
    }


    @Transactional
    public List<JobApplication> getApplicationHistory(Long userId) {
        return jobApplicationRepo.findByUserId(userId);

    }


//    public Map<String, Object> getApplicationStats(LocalDate startDate, LocalDate endDate) {
//        Map<String, Object> data = new HashMap<>();
//        List<JobApplication> applications;
//
//        if (startDate != null && endDate != null) {
//            applications = jobApplicationRepo.findByApplicationDateBetween(startDate, endDate);
//        } else {
//            applications = jobApplicationRepo.findAll();
//        }
//
//        data.put("totalApplications", applications.size());
//        data.put("applicationsInProgress", applications.stream().filter(app -> "In Progress".equals(app.getStatus())).count());
//        data.put("hiredCandidates", applications.stream().filter(app -> "Hired".equals(app.getStatus())).count());
//        data.put("rejectedCandidates", applications.stream().filter(app -> "Rejected".equals(app.getStatus())).count());
//
//        // Create charts data
//        String[] statuses = {"New", "In Progress", "Hired", "Rejected"};
//        int[] applicationCounts = new int[statuses.length];
//
//        for (JobApplication application : applications) {
//            switch (application.getStatus()) {
//                case "New": applicationCounts[0]++; break;
//                case "In Progress": applicationCounts[1]++; break;
//                case "Hired": applicationCounts[2]++; break;
//                case "Rejected": applicationCounts[3]++; break;
//            }
//        }
//
//        data.put("statuses", statuses);
//        data.put("applicationCounts", applicationCounts);
//
//        // Create mock data for date and source charts
//        String[] dates = {"2024-10-01", "2024-10-02", "2024-10-03", "2024-10-04"};
//        int[] applicationNumbers = {10, 20, 15, 25}; // Mock data
//        String[] sources = {"LinkedIn", "Indeed", "Referral", "Company Website"};
//        int[] sourceCounts = {50, 30, 20, 20}; // Mock data
//
//        data.put("dates", dates);
//        data.put("applicationNumbers", applicationNumbers);
//        data.put("sources", sources);
//        data.put("sourceCounts", sourceCounts);
//
//        return data;
//    }


    public long countAllApplicationsBetweenDatesAndSource(LocalDate startDate, LocalDate endDate, String source) {
        if (startDate != null && endDate != null) {
            if (source != null && !source.isEmpty()) {
                // Query for applications within date range and specific source
                return jobApplicationRepo.countByApplicationDateBetweenAndSource(startDate, endDate, source);
            } else {
                // Query for applications within date range, no source filter
                return jobApplicationRepo.countByApplicationDateBetween(startDate, endDate);
            }
        } else {
            if (source != null && !source.isEmpty()) {
                // Query for applications with source filter, no date filter
                return jobApplicationRepo.countBySource(source);
            } else {
                // Query for all applications
                return jobApplicationRepo.count();
            }
        }
    }


    public long countApplicationsByStatusAndDatesAndSource(String status, LocalDate startDate, LocalDate endDate, String source) {
        if (startDate != null && endDate != null) {
            if (source != null && !source.isEmpty()) {
                // Query for applications by status, date range, and specific source
                return jobApplicationRepo.countByStatusAndApplicationDateBetweenAndSource(status, startDate, endDate, source);
            } else {
                // Query for applications by status and date range, no source filter
                return jobApplicationRepo.countByStatusAndApplicationDateBetween(status, startDate, endDate);
            }
        } else {
            if (source != null && !source.isEmpty()) {
                // Query for applications by status and source, no date filter
                return jobApplicationRepo.countByStatusAndSource(status, source);
            } else {
                // Query for applications by status, no date or source filter
                return jobApplicationRepo.countByStatus(status);
            }
        }
    }


    public Map<String, Long> getLineChartData(LocalDate startDate, LocalDate endDate, String source) {
        List<Object[]> data;
        if (startDate != null && endDate != null) {
            if (source != null && !source.isEmpty()) {
                // Query for applications grouped by date, filtered by date range and source
                data = jobApplicationRepo.findApplicationsGroupedByDateAndSource(startDate, endDate, source);
            } else {
                // Query for applications grouped by date, filtered by date range
                data = jobApplicationRepo.findApplicationsGroupedByDate(startDate, endDate);
            }
        } else {
            if (source != null && !source.isEmpty()) {
                // Query for applications grouped by date, filtered by source
                data = jobApplicationRepo.findApplicationsGroupedBySource(source);
            } else {
                // Query for all applications grouped by date
                data = jobApplicationRepo.findAllApplicationsGroupedByDate();
            }
        }

        // Convert list of objects to a map of date -> count
        Map<String, Long> lineChartData = new LinkedHashMap<>();
        for (Object[] row : data) {
            LocalDate date = (LocalDate) row[0];
            Long count = (Long) row[1];
            lineChartData.put(date.toString(), count);
        }

        return lineChartData;
    }



}

