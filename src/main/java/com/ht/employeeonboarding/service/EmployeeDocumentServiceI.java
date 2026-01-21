package com.ht.employeeonboarding.service;

import jakarta.annotation.Resource;

import org.springframework.web.multipart.MultipartFile;

import com.ht.employeeonboarding.entity.EmployeeDocument;

import java.util.List;

public interface EmployeeDocumentServiceI {
    EmployeeDocument uploadDocument(Long employeeId, String documentType, MultipartFile file);
    org.springframework.core.io.Resource downloadDocument(Long employeeId, String fileName);
    List<EmployeeDocument> getDocumentsByEmployee(Long employeeId);
    void deleteDocument(Long id);
}
