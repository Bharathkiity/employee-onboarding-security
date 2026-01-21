package com.ht.employeeonboarding.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.Set;
import java.util.HashSet;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Data
@Table(name = "employee_tech_certifications")
public class EmployeeTechCertifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String certificationName; // Example: AWS Certified Developer

    private String certificationLink;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate; // Certification expiry date

    private String instituteName; // Example: Coursera, Udemy, AWS Academy

    @Enumerated(EnumType.STRING)
    private CertificationMode mode; // Enum for OFFLINE/ONLINE

    @ElementCollection
    @CollectionTable(name = "certification_tech_stack", joinColumns = @JoinColumn(name = "certification_id"))
    @Column(name = "tech_stack")
    @JsonIgnore
    private Set<String> techStackData = new HashSet<>(); // Stores multiple technologies

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonBackReference
    private Employee employee;

    // New Fields for Automatic Duration Calculation
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate courseEnrolledDate;  // Start date of the course

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate courseCompletionDate; // End date of the course

    // ✅ Calculated Course Duration stored directly in the database
    private String courseDuration;

    // Auto-calculates and sets course duration before persisting/updating
    @PrePersist
    @PreUpdate
    public void calculateCourseDuration() {
        if (courseEnrolledDate != null && courseCompletionDate != null) {
            Period period = Period.between(courseEnrolledDate, courseCompletionDate);

            int years = period.getYears();
            int months = period.getMonths();
            int days = period.getDays();

            // Format the duration string properly
            this.courseDuration = (years > 0 ? years + " Years " : "") +
                                  (months > 0 ? months + " Months " : "") +
                                  (days > 0 ? days + " Days" : "").trim();
        } else {
            this.courseDuration = "0 Days"; // Default value if dates are not set
        }
    }
}
