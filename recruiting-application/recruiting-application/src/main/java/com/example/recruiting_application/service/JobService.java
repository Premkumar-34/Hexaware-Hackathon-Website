package com.example.recruiting_application.service;

import com.example.recruiting_application.model.Jobs;
import com.example.recruiting_application.repository.JobRepo;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {
    private JobRepo jobRepo;
    public JobService(JobRepo jobRepo){
        this.jobRepo = jobRepo;
    }


    public Jobs findById(Long id){
        return jobRepo.findById(id).orElse(null);
    }

    public Jobs saveJob(Jobs jobs) {
        return jobRepo.save(jobs);
    }


    public List<Jobs> alljobs() {
        return jobRepo.findAll();
    }


    public List<Jobs> getDrafts(){
        return jobRepo.findByStatus("draft");
    }
    public Jobs updateJob(Long id, Jobs updatedJob) {
        Optional<Jobs> existingJob = jobRepo.findById(id);
        if (existingJob.isPresent()) {
            Jobs job = existingJob.get();
            job.setJobTitle(updatedJob.getJobTitle());
            job.setJobDescription(updatedJob.getJobDescription());
            job.setDepartment(updatedJob.getDepartment());
            job.setJobLocation(updatedJob.getJobLocation());
            job.setEmploymentType(updatedJob.getEmploymentType());
            job.setSalaryRange(updatedJob.getSalaryRange());
            job.setApplicationDeadline(updatedJob.getApplicationDeadline());
            job.setRequiredQualifications(updatedJob.getRequiredQualifications());
            job.setPreferredQualifications(updatedJob.getPreferredQualifications());
            job.setResponsibilities(updatedJob.getResponsibilities());
            job.setStatus(updatedJob.getStatus());  // Update status, if applicable

            return jobRepo.save(job);  // Save the updated job
        } else {
            throw new RuntimeException("Job not found with id " + id);
        }
    }

    public List<Jobs> getPublished() {
        return jobRepo.findByStatus("publish");
    }

    public boolean deleteJob(Long id) {
        Optional<Jobs> jobs =jobRepo.findById(id);
        if (jobs.isPresent()){
            jobRepo.deleteById(id);
            return true;
        }else {
            return false;
        }
    }

    public List<Jobs> searchJobs(String jobTitle, String jobLocation, String employmentType) {
        return jobRepo.findAll(createSpecification(jobTitle, jobLocation, employmentType).and(publishedSpecification()));
    }

    private Specification<Jobs> publishedSpecification() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), "publish");
    }






    private Specification<Jobs> createSpecification(String jobTitle, String jobLocation, String employmentType) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (jobTitle != null && !jobTitle.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("jobTitle"), "%" + jobTitle + "%"));
            }

            if (jobLocation != null && !jobLocation.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("jobLocation"), "%" + jobLocation + "%"));
            }

            if (employmentType != null && !employmentType.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("employmentType"), employmentType));
            }

            // Combine all predicates with AND condition
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));  // Correct array type for Predicate
        };
    }


//    public Optional<Jobs> getFullDetailsOfTheJob(Long id) {
//        return jobRepo.findById(id);
//    }


    public Optional<Jobs> getJobById(Long jobId) {
        return jobRepo.findById(jobId);
    }
}
