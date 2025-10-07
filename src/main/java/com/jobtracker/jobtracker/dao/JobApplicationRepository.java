package com.jobtracker.jobtracker.dao;

import com.jobtracker.jobtracker.entity.JobApplication;
import com.jobtracker.jobtracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByUser(User user);
}
