package com.example.recruiting_application.controller;

import com.example.recruiting_application.dto.JobApplicationDTO;
import com.example.recruiting_application.model.JobApplication;
import com.example.recruiting_application.service.EmailService;
import com.example.recruiting_application.service.JobApplicationService;
import com.example.recruiting_application.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class JobApplicationController {
    private JobApplicationService jobApplicationService;
    private EmailService emailService;
    private UserService userService;
    private final RestTemplate restTemplate;
    public JobApplicationController(JobApplicationService jobApplicationService, EmailService emailService, UserService userService, RestTemplate restTemplate){
        this.jobApplicationService = jobApplicationService;
        this.emailService = emailService;
        this.userService = userService;
        this.restTemplate =restTemplate;
    }



    // to apply for the jobs


    @PostMapping("/apply")
    public ResponseEntity<String> applyForJob(
            @RequestParam("userId") Long userId,
            @RequestParam("jobId") Long jobId,
            @RequestParam("jobTitle") String jobTitle,
            @RequestParam("resume") MultipartFile resumeFile,
            @RequestParam("source") String  source,
            @RequestParam("dob") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dob,
            @RequestParam("appliedDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appliedDate) {

        Logger logger = LoggerFactory.getLogger(JobApplicationController.class);

        // Check if the user has already applied for the job
        if (jobApplicationService.existsByUserIdAndJobId(userId, jobId)) {
            logger.warn("User with userId: {} has already applied for jobId: {}", userId, jobId);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(null); // Return null for the JobApplication body and set conflict status
        }

        try {
            // Rest of your code for processing the job application...
            byte[] resumeData = resumeFile.getBytes();



            // To go for the ai url

            String aiUrl;
            if(jobId == 5){
                aiUrl = "http://127.0.0.1:5000/api/ai-process/mern-stack";
            }else if (jobId == 2){
                aiUrl = "http://127.0.0.1:5000/api/ai-process/data-analyst";
            }else if (jobId == 3){
                aiUrl = "http://127.0.0.1:5000/api/ai-process/python-developer";
            } else if (jobId == 4) {
                aiUrl = "http://127.0.0.1:5000/api/ai-process/java-developer";
            }else {
                aiUrl = "http://127.0.0.1:5000/api/ai-process/full-stack";
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("resume", new ByteArrayResource(resumeData) {
                @Override
                public String getFilename() {
                    return resumeFile.getOriginalFilename();
                }
            });

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<Map> aiResponse = restTemplate.postForEntity(aiUrl, requestEntity, Map.class);

            Double aiScore = (double) Math.round((Double) aiResponse.getBody().get("aiScore"));
            if (aiScore == null) {
                logger.error("AI score is null.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(null); // Return null for the JobApplication body
            }

            JobApplication jobApplication = new JobApplication();
            jobApplication.setJobId(jobId);
            jobApplication.setUserId(userId);
            jobApplication.setJobTitle(jobTitle);
            jobApplication.setResumeFileName(resumeFile.getOriginalFilename());
            jobApplication.setResumeData(resumeData);
            jobApplication.setAiScore(aiScore);
            jobApplication.setSource(source);
            jobApplication.setApplicationDate(appliedDate);
            jobApplication.setDob(dob);

            JobApplication savedApplication = jobApplicationService.applyForJob(jobApplication);

            logger.info("AI Response: {}", aiResponse.getBody());
            logger.info("AI Score: {}", aiScore);


            String userEmail = getEmailById(userId);
            if (userEmail != null) {
                emailService.sendApplicationSubmissionEmail(userEmail);
            }else{
                logger.warn("Error Sending the Mail");
            }

            return ResponseEntity.ok("Job Application Submitted Successfully");

        } catch (Exception e) {
            logger.error("Error processing application: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    private String getEmailById(Long userId) {
        return userService.getUserEmailById(userId);
    }


    // Endpoint to accept a job application
    @PostMapping("/{id}/accept")
    public ResponseEntity<String> acceptApplication(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        String interviewDateStr = requestBody.get("interviewDate");
        LocalDate interviewDate = LocalDate.parse(interviewDateStr);

        JobApplication jobApplication = jobApplicationService.getApplicationById(id);
        if (jobApplication == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job application not found.");
        }

        // Update the job application status to 'Accepted'
        jobApplication.setStatus("Accepted");
        jobApplication.setInterviewDate(interviewDate); // Save the interview date
        jobApplicationService.save(jobApplication); // Save changes to the database

        // Send an email to the user with the interview details asynchronously
        String userEmail = userService.getUserEmailById(jobApplication.getUserId());
        if (userEmail != null) {
            emailService.sendInterviewInvitationEmail(userEmail, interviewDate);
        }

        return ResponseEntity.ok("Application accepted, interview email will be sent.");
    }

    // Endpoint to reject a job application and delete it from the database
    @PostMapping("/{id}/reject")
    public ResponseEntity<String> rejectApplication(@PathVariable Long id) {
        JobApplication jobApplication = jobApplicationService.getApplicationById(id);
        if (jobApplication == null) {
            return new ResponseEntity<>("Job application not found.", HttpStatus.NOT_FOUND);
        }

        // Get the user's email
        String userEmail = userService.getUserEmailById(jobApplication.getUserId());

        // Update the job application status to "rejected"
        jobApplication.setStatus("rejected"); // Assuming you have a status field
        jobApplicationService.update(jobApplication); // Save the updated status

        // Optionally, send a rejection email to the user
        if (userEmail != null) {
            emailService.sendRejectionMail(userEmail);
        }

        return new ResponseEntity<>("Application rejected and status updated.", HttpStatus.OK);
    }

    @GetMapping("/{id}/resume")
    public ResponseEntity<byte[]> getResume(@PathVariable Long id) {
        JobApplication application = jobApplicationService.getApplicationById(id);
        if (application != null && application.getResumeData() != null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + application.getResumeFileName() + "\"")
                    .body(application.getResumeData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    //to save the resume

    private String saveResumeFile(MultipartFile resumeFile) throws IOException {
        // Define a directory to save resumes (can be any path you choose)
        String uploadDir = "uploads/resumes/";
        String fileName = resumeFile.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);

        // Save the file to the defined path
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, resumeFile.getBytes());

        return fileName; // Return the file name for storage in the database
    }

    // to show those who are applied for the jobs

    @GetMapping("/all")
    public List<JobApplicationDTO> getAppliedJobs(){
        List<JobApplicationDTO> applicationDTOS = jobApplicationService.getAllApplications();
        System.out.println("Fetched Applications: " + applicationDTOS);
        return applicationDTOS;
    }


    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getApplicationStats(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        // Filter applications based on date range if provided
        long totalApplications = jobApplicationService.countAllApplicationsBetweenDates(startDate, endDate);
        long acceptedApplications = jobApplicationService.countApplicationsByStatusAndDates("Accepted", startDate, endDate);
        long rejectedApplications = jobApplicationService.countApplicationsByStatusAndDates("rejected", startDate, endDate);
        long underReviewApplications = jobApplicationService.countApplicationsByStatusAndDates(null, startDate, endDate);

        Map<String, Long> stats = new HashMap<>();
        stats.put("totalApplications", totalApplications);
        stats.put("acceptedApplications", acceptedApplications);
        stats.put("rejectedApplications", rejectedApplications);
        stats.put("underReviewApplications", underReviewApplications);

        return new ResponseEntity<>(stats, HttpStatus.OK);
    }



    @GetMapping("/history/{userId}")
    public ResponseEntity<List<JobApplication>> getApplicationHistory(@PathVariable Long userId) {
        List<JobApplication> applications = jobApplicationService.getApplicationHistory(userId);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/again/stats")
    public ResponseEntity<Map<String, Object>> getApplicationStats(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false) String source) {

        // Filter applications by date range and source if provided
        long totalApplications = jobApplicationService.countAllApplicationsBetweenDatesAndSource(startDate, endDate, source);
        long acceptedApplications = jobApplicationService.countApplicationsByStatusAndDatesAndSource("Accepted", startDate, endDate, source);
        long rejectedApplications = jobApplicationService.countApplicationsByStatusAndDatesAndSource("rejected", startDate, endDate, source);
        long underReviewApplications = jobApplicationService.countApplicationsByStatusAndDatesAndSource(null, startDate, endDate, source);

        // Prepare line chart data (example: applications per day or month)
        Map<String, Long> lineChartData = jobApplicationService.getLineChartData(startDate, endDate, source);

        // Prepare the response map
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalApplications", totalApplications);
        stats.put("acceptedApplications", acceptedApplications);
        stats.put("rejectedApplications", rejectedApplications);
        stats.put("underReviewApplications", underReviewApplications);
        stats.put("lineChartData", lineChartData); // Add line chart data to the response

        return new ResponseEntity<>(stats, HttpStatus.OK);
    }







}