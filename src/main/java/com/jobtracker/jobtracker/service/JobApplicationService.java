package com.jobtracker.jobtracker.service;

import com.jobtracker.jobtracker.dao.JobApplicationRepository;
import com.jobtracker.jobtracker.dao.UserRepository;
import com.jobtracker.jobtracker.entity.JobApplication;
import com.jobtracker.jobtracker.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final UserRepository userRepository;

    // Fetch applications by email
    public List<JobApplication> getApplicationsByEmail(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            return jobApplicationRepository.findByUser(userOpt.get());
        }
        throw new RuntimeException("User not found with email: " + email);
    }

    public JobApplication updateApplication(Long id, JobApplication updatedApp) {
        Optional<JobApplication> existing = jobApplicationRepository.findById(id);
        if (existing.isPresent()) {
            JobApplication app = existing.get();
            app.setJobTitle(updatedApp.getJobTitle());
            app.setApplicationDate(updatedApp.getApplicationDate());
            app.setStatus(updatedApp.getStatus());
            app.setCompanyName(updatedApp.getCompanyName());
            app.setJobType(updatedApp.getJobType());
            app.setJobLocation(updatedApp.getJobLocation());
            app.setSalaryExpectation(updatedApp.getSalaryExpectation());
            app.setNotes(updatedApp.getNotes());
            return jobApplicationRepository.save(app);
        }
        return null;
    }


    // Add application for a user (by email)
    public JobApplication addApplication(String email, JobApplication jobApplication) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            jobApplication.setUser(userOpt.get());
            return jobApplicationRepository.save(jobApplication);
        }
        throw new RuntimeException("User not found with email: " + email);
    }

    // Delete application by ID
    public boolean deleteApplication(Long id) {
        if (jobApplicationRepository.existsById(id)) {
            jobApplicationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
