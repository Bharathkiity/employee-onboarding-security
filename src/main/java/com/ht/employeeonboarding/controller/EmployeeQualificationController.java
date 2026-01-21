package com.ht.employeeonboarding.controller;

import com.ht.employeeonboarding.entity.EmployeeQualification;
import com.ht.employeeonboarding.service.EmployeeQualificationServiceI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees/{employeeId}/qualifications")
@CrossOrigin("http://localhost:4200") // angular url acess by angular or any
public class EmployeeQualificationController {

    @Autowired
    private EmployeeQualificationServiceI employeeQualificationService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Secures all methods in this controller
    public ResponseEntity<EmployeeQualification> addQualificationToEmployee(
            @PathVariable Long employeeId, 
            @RequestBody EmployeeQualification qualification) {
        
        EmployeeQualification savedQualification = employeeQualificationService.addQualificationToEmployee(employeeId, qualification);
        return ResponseEntity.ok(savedQualification);
    }
    
//    @PostMapping
//    public ResponseEntity<List<EmployeeQualification>> addQualificationsToEmployee(
//            @PathVariable Long employeeId,
//            @RequestBody List<EmployeeQualification> qualifications) {
//        
//        List<EmployeeQualification> savedQualifications = new ArrayList<>();
//        for(EmployeeQualification q : qualifications) {
//            savedQualifications.add(employeeQualificationService.addQualificationToEmployee(employeeId, q));
//        }
//        return ResponseEntity.ok(savedQualifications);
//    }


    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Secures all methods in this controller
    public List<EmployeeQualification> getAllQualifications() {
        return employeeQualificationService.getAllQualifications();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Secures all methods in this controller
    public Optional<EmployeeQualification> getQualificationById(@PathVariable Long id) {
        return employeeQualificationService.getQualificationById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Secures all methods in this controller
    public ResponseEntity<EmployeeQualification> updateQualification(
            @PathVariable Long employeeId, 
            @PathVariable Long id, 
            @RequestBody EmployeeQualification qualification) {
        
        // Fetching the qualification by ID
        Optional<EmployeeQualification> existingQualification = employeeQualificationService.getQualificationById(id);

        if (existingQualification.isPresent()) {
            // Update the existing qualification details
            EmployeeQualification updatedQualification = existingQualification.get();
            updatedQualification.setQualificationType(qualification.getQualificationType());
            updatedQualification.setDuration(qualification.getDuration());
            updatedQualification.setPercentage(qualification.getPercentage());
            updatedQualification.setGrade(qualification.getGrade());
            updatedQualification.setCollegeName(qualification.getCollegeName());
            updatedQualification.setUniversityName(qualification.getUniversityName());
            updatedQualification.setCity(qualification.getCity());
            updatedQualification.setState(qualification.getState());
            updatedQualification.setCountry(qualification.getCountry());
            updatedQualification.setMemoFileLink(qualification.getMemoFileLink());

            // Save the updated qualification to the database
            EmployeeQualification savedQualification = employeeQualificationService.addQualificationToEmployee(employeeId, updatedQualification);

            return ResponseEntity.ok(savedQualification); // Return the updated qualification
        } else {
            return ResponseEntity.notFound().build(); // Qualification not found
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Secures all methods in this controller
    public ResponseEntity<Void> deleteQualification(@PathVariable Long id) {
        Optional<EmployeeQualification> existingQualification = employeeQualificationService.getQualificationById(id);

        if (existingQualification.isPresent()) {
            employeeQualificationService.deleteQualification(id); // Delete the qualification
            return ResponseEntity.noContent().build(); // Success response
        } else {
            return ResponseEntity.notFound().build(); // Qualification not found
        }
    }
}
