package com.bluebell.project.controller;

import com.bluebell.project.config.BookingConfig;
import com.bluebell.project.config.EntrancePricesConfig;
import com.bluebell.project.dto.BookingCreateRequest;
import com.bluebell.project.dto.BookingDto;
import com.bluebell.project.dto.BookingUpdateRequest;
import com.bluebell.project.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
//@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingConfig bookingConfig;

    private final BookingService svc;

    public BookingController(BookingService svc, BookingConfig bookingConfig, EntrancePricesConfig entrancePricesConfig) {
        this.svc = svc;
        this.bookingConfig = bookingConfig;
    }

    @GetMapping
    public ResponseEntity<List<BookingDto>> list() {
        return ResponseEntity.ok(svc.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> get(@PathVariable Long id) {
        BookingDto dto = svc.getById(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<BookingDto> create(@Valid @RequestBody BookingCreateRequest req) {
        BookingDto dto = svc.create(req);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingDto> update(@PathVariable Long id, @Valid @RequestBody BookingUpdateRequest req) {
        BookingDto dto = svc.update(id, req);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/prices")
    public ResponseEntity<Map<String, Integer>> getPrices() {
        return ResponseEntity.ok(bookingConfig.getPrices());
    }

    @GetMapping("/code/{bookingCode}")
    public ResponseEntity<BookingDto> getByCode(@PathVariable String bookingCode) {
        BookingDto dto = svc.getByCode(bookingCode);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }




}