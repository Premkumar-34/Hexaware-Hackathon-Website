package com.example.recruiting_application.service;

import com.example.recruiting_application.model.Reports;
import com.example.recruiting_application.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {
    @Autowired
    private ReportRepository reportRepository;
    public List<Reports> getAllReports() {
        return reportRepository.findAll();
    }

    public Reports createReport(Reports report) {
        return reportRepository.save(report);
    }

    public Reports updateReport(Long id, Reports reportDetails) {
        Reports report = reportRepository.findById(id).orElseThrow();
        report.setName(reportDetails.getName());
        report.setDateCreated(reportDetails.getDateCreated());
        report.setCreatedBy(reportDetails.getCreatedBy());
        return reportRepository.save(report);
    }

    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }
}
