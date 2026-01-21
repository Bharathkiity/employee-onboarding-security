package com.ht.employeeonboarding.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ht.employeeonboarding.entity.EmployeeTechCertifications;

@Repository
public interface EmployeeTechCertificationsRepository extends JpaRepository<EmployeeTechCertifications, Long> {
    // Find all certifications for a specific employee
    List<EmployeeTechCertifications> findByEmployeeId(Long employeeId);

    // Find a certification by employeeId and certificationId
    Optional<EmployeeTechCertifications> findByEmployeeIdAndId(Long employeeId, Long id);

}
