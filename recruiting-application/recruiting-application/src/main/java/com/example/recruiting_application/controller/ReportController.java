package com.example.recruiting_application.controller;

import com.example.recruiting_application.model.Reports;
import com.example.recruiting_application.repository.ReportRepository;
import com.example.recruiting_application.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class ReportController {
    @Autowired
    private ReportService reportService;
    @Autowired
    private ReportRepository reportRepository;

    @GetMapping
    public List<Reports> getAllReports() {
        return reportRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reports> getReportById(@PathVariable Long id) {
        return reportRepository.findById(id)
                .map(report -> ResponseEntity.ok().body(report))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Reports createReport(@RequestBody Reports report) {
        report.setDateCreated(LocalDate.now());
        return reportRepository.save(report);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reports> updateReport(@PathVariable Long id, @RequestBody Reports reportDetails) {
        return reportRepository.findById(id)
                .map(report -> {
                    report.setName(reportDetails.getName());
                    report.setCreatedBy(reportDetails.getCreatedBy());
                    report.setDateRange(reportDetails.getDateRange());
                    report.setStatus(reportDetails.getStatus());
                    report.setPosition(reportDetails.getPosition());
                    report.setSource(reportDetails.getSource());
                    return ResponseEntity.ok(reportRepository.save(report));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        return reportRepository.findById(id)
                .map(report -> {
                    reportRepository.delete(report);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
