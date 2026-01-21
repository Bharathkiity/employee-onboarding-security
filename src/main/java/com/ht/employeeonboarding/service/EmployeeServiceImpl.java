package com.ht.employeeonboarding.service;

import com.ht.employeeonboarding.entity.Employee;
import com.ht.employeeonboarding.entity.Role;
import com.ht.employeeonboarding.entity.User;
import com.ht.employeeonboarding.repository.EmployeeRepository;
import com.ht.employeeonboarding.repository.RoleRepository;
import com.ht.employeeonboarding.repository.UserRepository;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeServiceI {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private MailService mailService;

	@Transactional
	@Override
	public Employee saveEmployee(Employee employee) {

		// Step 1: Save employee
		Employee savedEmployee = employeeRepository.save(employee);

		// Step 2: Generate random password
		String rawPassword = generateRandomPassword();
		String encodedPassword = passwordEncoder.encode(rawPassword);

		// ✅ DEV LOGGING — REMOVE OR DISABLE IN PRODUCTION
		logger.info("✅ New employee created:");
		logger.info(" - Name: {}", savedEmployee.getEmployeeName());
		logger.info(" - Email: {}", savedEmployee.getEmail());
		logger.info(" - Temporary Password: {}", rawPassword);

		// Step 3: Create user and assign password + employee
		User user = new User();
		user.setUserEmail(savedEmployee.getEmail());
		user.setUserPassword(encodedPassword);
		user.setEmployee(savedEmployee);

		// ✅ Step 4: Fetch and set ROLE_USER
		Role role = roleRepository.findByRoleName("USER")
				.orElseThrow(() -> new RuntimeException("USER role not found"));
		user.setRole(role);

		// Step 5: Save user (user -> employee is already set)
		userRepository.save(user);

		// Step 6: Set user back into employee (if bidirectional mapping exists)
		savedEmployee.setUser(user);

		// Step 7: Send welcome email with credentials
		mailService.sendEmployeeCreationEmail(savedEmployee.getEmail(), savedEmployee.getEmployeeName(),
				savedEmployee.getEmail(), rawPassword);

		return savedEmployee;
	}

	@Override
	@Transactional
	public Optional<Employee> getEmployeeByEmail(String email) {
		Optional<Employee> employeeOpt = employeeRepository.findByEmail(email);

		employeeOpt.ifPresent(employee -> {
			Hibernate.initialize(employee.getEmployeeAddresses());
			Hibernate.initialize(employee.getEmployeeSkills());
			Hibernate.initialize(employee.getEmployeeQualifications());
			Hibernate.initialize(employee.getEmployeeTechCertifications());
			Hibernate.initialize(employee.getEmployeeDocuments());
			Hibernate.initialize(employee.getUser()); // Important: initialize user proxy here!
		});

		return employeeOpt;
	}

	// Utility method for random password generation
	private String generateRandomPassword() {
		return UUID.randomUUID().toString().substring(0, 8); // 8-character password
	}

	@Transactional(readOnly = true)
	@Override
	public List<Employee> getAllEmployees() {
		List<Employee> employees = employeeRepository.findAllWithDetails();

		// Initialize all collections to avoid lazy loading issues during serialization
		employees.forEach(employee -> {
			Hibernate.initialize(employee.getEmployeeAddresses());
			Hibernate.initialize(employee.getEmployeeQualifications());
			Hibernate.initialize(employee.getEmployeeSkills());
			Hibernate.initialize(employee.getEmployeeTechCertifications());
			Hibernate.initialize(employee.getEmployeeDocuments());
			if (employee.getUser() != null) {
				Hibernate.initialize(employee.getUser());
				Hibernate.initialize(employee.getUser().getRole());
			}
		});

		return employees;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Employee> getEmployeeById(Long id) {
		Optional<Employee> optEmployee = employeeRepository.findByIdWithDetails(id);

		optEmployee.ifPresent(employee -> {
			Hibernate.initialize(employee.getEmployeeAddresses());
			Hibernate.initialize(employee.getEmployeeQualifications());
			Hibernate.initialize(employee.getEmployeeSkills());
			Hibernate.initialize(employee.getEmployeeTechCertifications());
			Hibernate.initialize(employee.getEmployeeDocuments());
			if (employee.getUser() != null) {
				Hibernate.initialize(employee.getUser());
				Hibernate.initialize(employee.getUser().getRole());
			}
		});

		return optEmployee;
	}

	@Override
	@Transactional
	public Employee updateEmployee(Long id, Employee updatedEmployee) {
		return employeeRepository.findById(id).map(existingEmployee -> {
			existingEmployee.setEmployeeName(updatedEmployee.getEmployeeName());
			existingEmployee.setEmployeeMobile(updatedEmployee.getEmployeeMobile());
			existingEmployee.setEmployeeDesignation(updatedEmployee.getEmployeeDesignation());
			existingEmployee.setDepartment(updatedEmployee.getDepartment());
			existingEmployee.setEmail(updatedEmployee.getEmail());
			return employeeRepository.save(existingEmployee);
		}).orElseThrow(() -> new RuntimeException("Employee with ID " + id + " not found"));
	}

	@Override
	@Transactional
	public void deleteEmployee(Long id) {
		if (employeeRepository.existsById(id)) {
			employeeRepository.deleteById(id);
		} else {
			throw new RuntimeException("Employee with ID " + id + " not found");
		}
	}
}
