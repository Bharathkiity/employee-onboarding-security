package com.ht.employeeonboarding.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ht.employeeonboarding.entity.Employee;
import com.ht.employeeonboarding.entity.EmployeeAddress;

@Repository
public interface EmployeeAddressRepository extends JpaRepository<EmployeeAddress, Long> {

    List<EmployeeAddress> findByEmployeeId(Long employeeId); // Correct method name

}

