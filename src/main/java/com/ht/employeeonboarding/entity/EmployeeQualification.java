package com.ht.employeeonboarding.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data

@Table(name = "employee_qualifications")
public class EmployeeQualification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private QualificationType qualificationType; // Enum for SSC, UG, PG, etc.

    private String duration;     // Example: "4 Years", "2 Years"
    private double percentage;   // Example: 85.5
    private String grade;        // Example: "A", "B+"
    private String collegeName;
    private String universityName;
    private String city;
    private String state;
    private String country;

    @JsonIgnore
    private String memoFileLink; // Store file as Base64 or binary

//  @JsonBackReference  // 🔹 Prevent infinite loop
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonBackReference
    private Employee employee;
}
