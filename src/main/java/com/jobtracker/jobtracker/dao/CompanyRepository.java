package com.jobtracker.jobtracker.dao;

import com.jobtracker.jobtracker.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
