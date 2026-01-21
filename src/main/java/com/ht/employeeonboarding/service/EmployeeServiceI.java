package com.ht.employeeonboarding.service;

import java.util.List;
import java.util.Optional;

import com.ht.employeeonboarding.entity.Employee;

public interface EmployeeServiceI {

	Employee saveEmployee(Employee employee);

	List<Employee> getAllEmployees();

	Optional<Employee> getEmployeeById(Long id);

	Employee updateEmployee(Long id, Employee employee);

	void deleteEmployee(Long id);
	
	
	Optional<Employee> getEmployeeByEmail(String email);

}
