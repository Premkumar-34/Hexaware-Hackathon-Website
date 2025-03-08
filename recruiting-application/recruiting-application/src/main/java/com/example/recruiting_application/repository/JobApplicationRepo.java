package com.example.recruiting_application.repository;

import com.example.recruiting_application.dto.HistoryDTO;
import com.example.recruiting_application.dto.JobApplicationDTO;
import com.example.recruiting_application.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JobApplicationRepo extends JpaRepository<JobApplication, Long> {
    @Query("SELECT new com.example.recruiting_application.dto.JobApplicationDTO(ja.id, ja.jobId, j.jobTitle, ja.userId, u.email, u.mobileNumber, ja.resumeFileName, ja.aiScore) " +
            "FROM JobApplication ja " +
            "JOIN User u ON ja.userId = u.id " +
            "JOIN Jobs j ON ja.jobId = j.id " +
            "WHERE ja.status IS NULL") // Filter for applications with null status
    List<JobApplicationDTO> findAllApplicationsWithUserDetails();

    boolean existsByUserIdAndJobId(Long userId, Long jobId);
//
//    long countByStatus(String status);
//
//
//    // New method to count applications based on applicationDate
//    long countByApplicationDateBetween(LocalDate startDate, LocalDate endDate);
//
//    long countByStatusAndApplicationDateBetween(String status, LocalDate startDate, LocalDate endDate);
//
    List<JobApplication> findByUserId(Long userId);

    long countByApplicationDateBetween(LocalDate startDate, LocalDate endDate);

    long countByApplicationDateBetweenAndSource(LocalDate startDate, LocalDate endDate, String source);

    long countBySource(String source);

    long countByStatus(String status);

    long countByStatusAndApplicationDateBetween(String status, LocalDate startDate, LocalDate endDate);

    long countByStatusAndApplicationDateBetweenAndSource(String status, LocalDate startDate, LocalDate endDate, String source);

    long countByStatusAndSource(String status, String source);

    // Group applications by date with optional filters
    @Query("SELECT j.applicationDate, COUNT(j) FROM JobApplication j WHERE j.applicationDate BETWEEN :startDate AND :endDate GROUP BY j.applicationDate ORDER BY j.applicationDate")
    List<Object[]> findApplicationsGroupedByDate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT j.applicationDate, COUNT(j) FROM JobApplication j WHERE j.applicationDate BETWEEN :startDate AND :endDate AND j.source = :source GROUP BY j.applicationDate ORDER BY j.applicationDate")
    List<Object[]> findApplicationsGroupedByDateAndSource(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("source") String source);

    @Query("SELECT j.applicationDate, COUNT(j) FROM JobApplication j WHERE j.source = :source GROUP BY j.applicationDate ORDER BY j.applicationDate")
    List<Object[]> findApplicationsGroupedBySource(@Param("source") String source);

    @Query("SELECT j.applicationDate, COUNT(j) FROM JobApplication j GROUP BY j.applicationDate ORDER BY j.applicationDate")
    List<Object[]> findAllApplicationsGroupedByDate();





//    @Query("SELECT new com.example.recruiting_application.dto.HistoryDTO(j.id, j.status, j.appliedAt) FROM JobApplication j WHERE j.user.id = :userId")
//    List<HistoryDTO> findByUserId(@Param("userId") Long userId);

}
