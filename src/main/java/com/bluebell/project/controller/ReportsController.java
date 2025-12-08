package com.bluebell.project.controller;

import com.bluebell.project.dto.BookingCreateRequest;
import com.bluebell.project.dto.BookingDto;
import com.bluebell.project.dto.ReportsDto;
import com.bluebell.project.model.Reports;
import com.bluebell.project.repository.ReportsRepository;
import com.bluebell.project.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    private final ReportsRepository repository;
    private final ReportService reportService;

    public ReportsController(ReportsRepository repository, ReportService reportService) {
        this.repository = repository;
        this.reportService = reportService;
    }


    @GetMapping
    public ResponseEntity<List<ReportsDto>> list(){
        return  ResponseEntity.ok(reportService.listAll());
    }

    @PostMapping
    public ResponseEntity<ReportsDto> create(@Valid @RequestBody ReportsDto req){
        ReportsDto dto = reportService.create(req);
        return ResponseEntity.ok(dto);

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<ReportsDto> update(@PathVariable Long id, @Valid @RequestBody ReportsDto req) {
        ReportsDto updated = reportService.update(id, req);
        return ResponseEntity.ok(updated);
    }


}
