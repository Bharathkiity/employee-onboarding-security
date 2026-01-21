package com.ht.employeeonboarding.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@ToString(exclude = "employee")
@EqualsAndHashCode(exclude = "employee")
@Table(name = "employee_addresses")
public class EmployeeAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String houseNumber;
    private String street;
    private String city;
    private String state;
    private String country;
    private String pincode;

    @Enumerated(EnumType.STRING) // Store as String (PERMANENT/TEMPORARY)
    private AddressType addressType;
    
//  @JsonBackReference  // 🔹 Prevent infinite loop
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonBackReference
    private Employee employee;
    
    

}
