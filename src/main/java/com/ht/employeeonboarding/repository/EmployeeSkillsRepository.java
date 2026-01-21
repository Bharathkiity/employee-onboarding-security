package com.ht.employeeonboarding.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ht.employeeonboarding.entity.EmployeeSkills;

@Repository
public interface EmployeeSkillsRepository extends JpaRepository<EmployeeSkills, Long> {
    List<EmployeeSkills> findByEmployeeId(Long employeeId);

}
