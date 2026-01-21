package com.ht.employeeonboarding.service;

import java.util.List;
import java.util.Optional;

import com.ht.employeeonboarding.entity.EmployeeTechCertifications;

public interface EmployeeTechCertificationsServiceI {

    // Save a new certification or update an existing one
    EmployeeTechCertifications saveCertification(Long employeeId, EmployeeTechCertifications certification);

    // Get all certifications for an employee
    List<EmployeeTechCertifications> getAllCertificationsForEmployee(Long employeeId);

    // Get a specific certification by ID
    Optional<EmployeeTechCertifications> getCertificationById(Long employeeId, Long id);

    // Delete a certification by ID
    void deleteCertification(Long id);
}
