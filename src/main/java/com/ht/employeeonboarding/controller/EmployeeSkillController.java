package com.ht.employeeonboarding.controller;

import com.ht.employeeonboarding.entity.EmployeeSkills;
import com.ht.employeeonboarding.service.EmployeeSkillsServiceI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees/{employeeId}/skills")
@CrossOrigin("http://localhost:4200") // Allowing Angular URL
public class EmployeeSkillController {

    @Autowired
    private EmployeeSkillsServiceI employeeSkillService;

    // Add Skill to an Employee
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Secures all methods in this controller
    @PostMapping
    public ResponseEntity<?> addSkillToEmployee(
            @PathVariable Long employeeId,
            @RequestBody EmployeeSkills skill) {
        try {
            EmployeeSkills savedSkill = employeeSkillService.addSkillToEmployee(employeeId, skill);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSkill);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add skill");
        }
    }

    // Get All Skills for an Employee
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Secures all methods in this controller
    @GetMapping
    public ResponseEntity<?> getAllSkillsByEmployeeId(@PathVariable Long employeeId) {
        try {
            List<EmployeeSkills> skills = employeeSkillService.getSkillsByEmployeeId(employeeId);
            if (skills.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No skills found for this employee.");
            }
            return ResponseEntity.ok(skills);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving skills");
        }
    }

    // Get Skill by SkillId for an Employee
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Secures all methods in this controller
    @GetMapping("/{skillId}")
    public ResponseEntity<?> getSkillById(
            @PathVariable Long employeeId,
            @PathVariable Long skillId) {
        try {
            Optional<EmployeeSkills> skill = employeeSkillService.getSkillById(employeeId, skillId);
            if (skill.isPresent()) {
                return ResponseEntity.ok(skill.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Skill not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving skill");
        }
    }

    // Update Skill for an Employee
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Secures all methods in this controller
    @PutMapping("/{skillId}")
    public ResponseEntity<?> updateSkill(
            @PathVariable Long employeeId,
            @PathVariable Long skillId,
            @RequestBody EmployeeSkills updatedSkill) {
        try {
            EmployeeSkills skill = employeeSkillService.updateSkill(employeeId, skillId, updatedSkill);
            return ResponseEntity.ok(skill);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating skill");
        }
    }

    // Delete Skill for an Employee
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Secures all methods in this controller
    @DeleteMapping("/{skillId}")
    public ResponseEntity<?> deleteSkill(
            @PathVariable Long employeeId,
            @PathVariable Long skillId) {
        try {
            employeeSkillService.deleteSkill(employeeId, skillId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting skill");
        }
    }
}
