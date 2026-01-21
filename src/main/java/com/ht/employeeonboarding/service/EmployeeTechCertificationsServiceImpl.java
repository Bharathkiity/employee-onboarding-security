package com.ht.employeeonboarding.service;

import com.ht.employeeonboarding.entity.Employee;
import com.ht.employeeonboarding.entity.EmployeeTechCertifications;
import com.ht.employeeonboarding.repository.EmployeeRepository;
import com.ht.employeeonboarding.repository.EmployeeTechCertificationsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeTechCertificationsServiceImpl implements EmployeeTechCertificationsServiceI {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeTechCertificationsRepository employeeTechCertificationsRepository;

    @Override
    public EmployeeTechCertifications saveCertification(Long employeeId, EmployeeTechCertifications certification) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

        certification.setEmployee(employee); // Associate certification with the employee
        return employeeTechCertificationsRepository.save(certification);
    }

    @Override
    public List<EmployeeTechCertifications> getAllCertificationsForEmployee(Long employeeId) {
        return employeeTechCertificationsRepository.findByEmployeeId(employeeId);
    }

    @Override
    public Optional<EmployeeTechCertifications> getCertificationById(Long employeeId, Long id) {
        return employeeTechCertificationsRepository.findByEmployeeIdAndId(employeeId, id);
    }

    @Override
    public void deleteCertification(Long id) {
        employeeTechCertificationsRepository.deleteById(id);
    }
}
