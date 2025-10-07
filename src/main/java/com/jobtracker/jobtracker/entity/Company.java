package com.jobtracker.jobtracker.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String industry;
    private String location;
    private String website;
    private String contactEmail;

    @OneToMany(mappedBy = "company")
    private List<JobApplication> applications;

    // Getters and Setters
}
