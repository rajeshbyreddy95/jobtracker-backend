package com.jobtracker.jobtracker.controller;

import com.jobtracker.jobtracker.entity.JobApplication;
import com.jobtracker.jobtracker.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://jobtracker-backend-wdpl.onrender.com", allowCredentials = "true")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    // Get all applications for a user
    @GetMapping("/user/{email}")
    public ResponseEntity<List<JobApplication>> getApplicationsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(jobApplicationService.getApplicationsByEmail(email));
    }

    // Add new application
    @PostMapping("/user/{userId}")
    public ResponseEntity<JobApplication> addApplication(
            @PathVariable String userId,
            @RequestBody JobApplication jobApplication
    ) {
        JobApplication savedApp = jobApplicationService.addApplication(userId, jobApplication);
        return ResponseEntity.ok(savedApp);
    }

    // Update existing application
    @PutMapping("/{id}")
    public ResponseEntity<JobApplication> updateApplication(
            @PathVariable Long id,
            @RequestBody JobApplication updatedApp
    ) {
        JobApplication app = jobApplicationService.updateApplication(id, updatedApp);
        return app != null
                ? ResponseEntity.ok(app)
                : ResponseEntity.notFound().build();
    }

    // Delete an application
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteApplication(@PathVariable Long id) {
        boolean deleted = jobApplicationService.deleteApplication(id);
        return deleted
                ? ResponseEntity.ok("Deleted successfully")
                : ResponseEntity.notFound().build();
    }


}
