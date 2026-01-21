package com.ht.employeeonboarding.controller;

import com.ht.employeeonboarding.entity.EmployeeTechCertifications;
import com.ht.employeeonboarding.service.EmployeeTechCertificationsServiceI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees/{employeeId}/tech-certifications")
@CrossOrigin(origins = "http://localhost:4200")  // Adjust the origin as per your frontend's URL
public class EmployeeTechCertificationsController {

    @Autowired
    private EmployeeTechCertificationsServiceI techCertificationsService;

    // Create or Update Certification (POST/PUT)
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Secures all methods in this controller
    public ResponseEntity<EmployeeTechCertifications> createCertification(
            @PathVariable Long employeeId,
            @RequestBody EmployeeTechCertifications certification) {

        EmployeeTechCertifications savedCertification = techCertificationsService.saveCertification(employeeId, certification);
        return ResponseEntity.ok(savedCertification);
    }

    // Update an existing certification (PUT)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Secures all methods in this controller
    public ResponseEntity<EmployeeTechCertifications> updateCertification(
            @PathVariable Long employeeId, 
            @PathVariable Long id, 
            @RequestBody EmployeeTechCertifications certification) {

        Optional<EmployeeTechCertifications> existingCertification = techCertificationsService.getCertificationById(employeeId, id);

        if (existingCertification.isPresent()) {
            EmployeeTechCertifications updatedCertification = existingCertification.get();
            
            // Update the fields
            updatedCertification.setCertificationName(certification.getCertificationName());
            updatedCertification.setCertificationLink(certification.getCertificationLink());
            updatedCertification.setInstituteName(certification.getInstituteName());
            updatedCertification.setMode(certification.getMode());
            updatedCertification.setTechStackData(certification.getTechStackData());
            updatedCertification.setCourseEnrolledDate(certification.getCourseEnrolledDate());
            updatedCertification.setCourseCompletionDate(certification.getCourseCompletionDate());

            // Calculate course duration before saving the updated entity
            updatedCertification.calculateCourseDuration();

            // Save the updated certification
            EmployeeTechCertifications savedCertification = techCertificationsService.saveCertification(employeeId, updatedCertification);
            return ResponseEntity.ok(savedCertification);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get all certifications for a specific employee
    @GetMapping
    public List<EmployeeTechCertifications> getAllCertificationsForEmployee(@PathVariable Long employeeId) {
        return techCertificationsService.getAllCertificationsForEmployee(employeeId);
    }

    // Get a specific certification by its ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Secures all methods in this controller
    public ResponseEntity<EmployeeTechCertifications> getCertificationById(
            @PathVariable Long employeeId, @PathVariable Long id) {

        Optional<EmployeeTechCertifications> certification = techCertificationsService.getCertificationById(employeeId, id);
        return certification.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a certification by its ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Secures all methods in this controller
    public ResponseEntity<Void> deleteCertification(@PathVariable Long id) {
        techCertificationsService.deleteCertification(id);
        return ResponseEntity.noContent().build();
    }
}
