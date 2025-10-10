package com.bluebell.project.controller;

import com.bluebell.project.model.Feedback;
import com.bluebell.project.repository.FeedbackRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedback")
//@CrossOrigin(origins = "http://localhost:3000") // Allow frontend connection
public class FeedbackController {

    private final FeedbackRepository feedbackRepository;

    public FeedbackController(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @PostMapping
    public ResponseEntity<String> submitFeedback(@RequestBody Feedback feedback) {
        feedbackRepository.save(feedback);
        return ResponseEntity.ok("Feedback saved successfully");
    }

    @GetMapping
    public ResponseEntity<?> getAllFeedback() {
        return ResponseEntity.ok(feedbackRepository.findAll());
    }
}
