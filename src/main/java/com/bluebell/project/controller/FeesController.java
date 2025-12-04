package com.bluebell.project.controller;

import com.bluebell.project.config.EntrancePricesConfig;
import com.bluebell.project.service.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prices")
public class FeesController {

    @Autowired
    private FeeService feeService;

    // GET: /api/fees/entrance
    @GetMapping("/entrance-fee")
    public ResponseEntity<EntrancePricesConfig> getEntranceFees() {
        return ResponseEntity.ok(feeService.getEntrancePricesConfig());
    }
}
