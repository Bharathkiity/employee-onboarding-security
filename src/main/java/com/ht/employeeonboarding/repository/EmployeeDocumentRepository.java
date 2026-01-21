package com.ht.employeeonboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ht.employeeonboarding.entity.EmployeeDocument;

import java.util.List;

public interface EmployeeDocumentRepository extends JpaRepository<EmployeeDocument, Long> {
    List<EmployeeDocument> findByEmployeeId(Long employeeId);
    List<EmployeeDocument> findByEmployeeIdAndDocumentType(Long employeeId, String documentType);
}