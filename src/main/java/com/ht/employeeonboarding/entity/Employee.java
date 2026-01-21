package com.ht.employeeonboarding.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Getter
@Setter
@ToString(exclude = { "employeeAddresses", "employeeSkills", "employeeQualifications", "employeeTechCertifications",
		"employeeDocuments", "user" })
@EqualsAndHashCode(exclude = { "employeeAddresses", "employeeSkills", "employeeQualifications",
		"employeeTechCertifications", "employeeDocuments", "user" })
@Table(name = "employees")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String employeeName;
	private String employeeMobile;
	private String employeeDesignation;
	private String department;
	private String email;

	@Enumerated(EnumType.STRING) // Store as String
	private Gender gender;

	// One-to-Many with EmployeeAddress (Changed to Set)
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JsonManagedReference
	private Set<EmployeeAddress> employeeAddresses;

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference
	private Set<EmployeeSkills> employeeSkills;

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference
	private Set<EmployeeQualification> employeeQualifications;

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference
	private Set<EmployeeTechCertifications> employeeTechCertifications;

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JsonManagedReference
	private Set<EmployeeDocument> employeeDocuments;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	// @JsonIgnoreProperties({"employee"}) // Add this to break the cycle
	@JsonManagedReference
	private User user;

}
