package com.bluebell.project.controller;

import com.bluebell.project.service.PayMongoService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/paymongo")
public class PayMongoController {

    @Autowired
    PayMongoService payMongoService;

    // Create payment intent
//    @PostMapping("/intent")
//    public ResponseEntity<?> createIntent(@RequestParam long amount, @RequestParam String returnUrl, @RequestParam String phoneNumber) {
//        try {
//            // Pass returnUrl so GCash knows where to redirect
//            return ResponseEntity.ok(payMongoService.createGcashIntent(amount, returnUrl,phoneNumber));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body(e.getMessage());
//        }
//    }
    @PostMapping("/intent/maya")
    public JsonNode createMayaPayment(@RequestBody PaymentRequest request) throws Exception {
        return payMongoService.createMayaIntent(request.getAmount(), request.getReturnUrl());
    }

    @GetMapping("/intent/{intentId}")
    public JsonNode checkPaymentStatus(@PathVariable String intentId) throws Exception {
        return payMongoService.getPaymentIntentStatus(intentId);
    }


    public static class PaymentRequest {
        private long amount;
        private String returnUrl;

        public long getAmount() { return amount; }
        public void setAmount(long amount) { this.amount = amount; }

        public String getReturnUrl() { return returnUrl; }
        public void setReturnUrl(String returnUrl) { this.returnUrl = returnUrl; }
    }
//
//    // Accept JSON body
//    @PostMapping("/intent/maya")
//    public ResponseEntity<?> createIntent(@RequestBody Map<String, Object> payload) {
//        try {
//            if (payload == null) {
//                return ResponseEntity.badRequest().body("Payload cannot be null");
//            }
//
//            Object amountObj = payload.get("amount");
//            Object returnUrlObj = payload.get("returnUrl");
//
//            if (amountObj == null || returnUrlObj == null) {
//                return ResponseEntity.badRequest().body("Missing required fields: amount or returnUrl");
//            }
//
//            long amount;
//            try {
//                amount = Long.parseLong(amountObj.toString());
//            } catch (NumberFormatException e) {
//                return ResponseEntity.badRequest().body("Amount must be a number");
//            }
//
//            String returnUrl = returnUrlObj.toString();
////            return ResponseEntity.ok(payMongoService.createQRPHIntent(amount, returnUrl));
//            return ResponseEntity.ok(payMongoService.createMayaIntent(amount,returnUrl));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body(e.getMessage());
//        }
//    }



}
