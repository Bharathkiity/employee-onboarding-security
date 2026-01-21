package com.ht.employeeonboarding.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ht.employeeonboarding.entity.Employee;

import java.util.List;
import java.util.Optional;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @EntityGraph(attributePaths = {
        "user",
        "user.role", // Explicitly include role
        "employeeAddresses",
        "employeeQualifications",
        "employeeSkills",
        "employeeTechCertifications",
        "employeeDocuments"
    })
    @Query("SELECT DISTINCT e FROM Employee e LEFT JOIN FETCH e.user u LEFT JOIN FETCH u.role")
    List<Employee> findAllWithDetails();
    
    @Query("SELECT e FROM Employee e " +
    	       "LEFT JOIN FETCH e.user u " +
    	       "LEFT JOIN FETCH u.role " +
    	       "LEFT JOIN FETCH e.employeeAddresses " +
    	       "LEFT JOIN FETCH e.employeeQualifications " +
    	       "LEFT JOIN FETCH e.employeeSkills " +
    	       "LEFT JOIN FETCH e.employeeTechCertifications " +
    	       "LEFT JOIN FETCH e.employeeDocuments " +
    	       "WHERE e.id = :id")
    	Optional<Employee> findByIdWithDetails(Long id);
    
    

    
    
    Optional<Employee> findByEmail(String email);
}
