package com.ht.employeeonboarding.service;

import java.util.List;
import java.util.Optional;

import com.ht.employeeonboarding.entity.EmployeeQualification;

public interface EmployeeQualificationServiceI {

	EmployeeQualification addQualificationToEmployee(Long employeeId, EmployeeQualification qualification);

	List<EmployeeQualification> getAllQualifications();

	Optional<EmployeeQualification> getQualificationById(Long id);

	void deleteQualification(Long id);
}
