package com.ht.employeeonboarding.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Entity
@Data
@Table(name = "employee_skills")
public class EmployeeSkills {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private SkillCategory category; // Example: TECHNICAL_SKILLS, SOFT_SKILLS

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "skill_details", joinColumns = @JoinColumn(name = "employee_skill_id"))
    @Column(name = "skill_name")
    private Set<String> skills; // Example: ["Java", "SpringBoot", "MySQL"]

    @Column(name = "experience_years", nullable = true)
    private Integer experience; // Number of years of experience with this skill

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonBackReference
    private Employee employee;
}
