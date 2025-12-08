package com.bluebell.project.service;

import com.bluebell.project.dto.BookingDto;
import com.bluebell.project.dto.ReportsDto;
import com.bluebell.project.model.Booking;
import com.bluebell.project.model.Reports;
import com.bluebell.project.repository.ReportsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final ReportsRepository repository;

    public ReportService(ReportsRepository repository) {
        this.repository = repository;
    }

    public List<ReportsDto> listAll() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public ReportsDto create(ReportsDto req){

        Reports report = new Reports();

        report.setCustomerName(req.getCustomerName());
        report.setUnit(req.getUnit());
        report.setDamageDescription(req.getDamageDescription());
        report.setDamageAmount(req.getDamageAmount());
        report.setDate(req.getDate() != null ? req.getDate() : LocalDateTime.now());

        Reports saved = repository.save(report);

        return toDto(saved);
    }

    public ReportsDto update(Long id,ReportsDto req){
        Reports existing = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        existing.setCustomerName(req.getCustomerName());
        existing.setUnit(req.getUnit());
        existing.setDamageDescription(req.getDamageDescription());
        existing.setDamageAmount(req.getDamageAmount());
        existing.setDate(req.getDate());

        Reports saved = repository.save(existing);
        return toDto(saved);
    }


    private ReportsDto toDto(Reports reports){
        ReportsDto r = new ReportsDto();
        r.setId(reports.getId());
        r.setCustomerName(reports.getCustomerName());
        r.setUnit(reports.getUnit());
        r.setDamageDescription(reports.getDamageDescription());
        r.setDamageAmount(reports.getDamageAmount());
        r.setDate(reports.getDate());

        return r;
    }


}
