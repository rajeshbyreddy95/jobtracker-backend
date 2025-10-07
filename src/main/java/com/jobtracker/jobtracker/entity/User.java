package com.jobtracker.jobtracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data                       // generates getters/setters, toString, equals, hashCode
@NoArgsConstructor          // default constructor
@AllArgsConstructor         // all args constructor
@Builder                    // builder pattern
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Automatically set createdAt before inserting
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
