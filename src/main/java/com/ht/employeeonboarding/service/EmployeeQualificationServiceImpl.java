package com.ht.employeeonboarding.service;

import com.ht.employeeonboarding.entity.Employee;
import com.ht.employeeonboarding.entity.EmployeeQualification;
import com.ht.employeeonboarding.repository.EmployeeQualificationRepository;
import com.ht.employeeonboarding.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeQualificationServiceImpl implements EmployeeQualificationServiceI {
	
	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private EmployeeQualificationRepository employeeQualificationRepository;

	@Override
	public EmployeeQualification addQualificationToEmployee(Long employeeId, EmployeeQualification qualification) {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

		qualification.setEmployee(employee); // Associate qualification with the employee
		return employeeQualificationRepository.save(qualification);
	}

	@Override
	public List<EmployeeQualification> getAllQualifications() {
		return employeeQualificationRepository.findAll();
	}

	@Override
	public Optional<EmployeeQualification> getQualificationById(Long id) {
		return employeeQualificationRepository.findById(id);
	}

	@Override
	public void deleteQualification(Long id) {
		employeeQualificationRepository.deleteById(id);
	}
}
