package com.ht.employeeonboarding.service;

import com.ht.employeeonboarding.entity.Employee;
import com.ht.employeeonboarding.entity.EmployeeDocument;
import com.ht.employeeonboarding.repository.EmployeeDocumentRepository;
import com.ht.employeeonboarding.repository.EmployeeRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public class EmployeeDocumentServiceImpl implements EmployeeDocumentServiceI {

    @Autowired
    private EmployeeDocumentRepository documentRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public EmployeeDocument uploadDocument(Long employeeId, String documentType, MultipartFile file) {
        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        String fileName = fileStorageService.storeFile(file);

        EmployeeDocument document = new EmployeeDocument();
        document.setFileName(fileName);
        document.setFileType(file.getContentType());
        document.setFileSize(file.getSize());
        document.setDocumentType(documentType.toUpperCase());
        document.setFilePath("/api/employees/" + employeeId + "/documents/download/" + fileName); // ✅ Use in frontend to download
        document.setEmployee(employee);

        return documentRepo.save(document);
    }

    @Override
    public Resource downloadDocument(Long employeeId, String fileName) {
        return fileStorageService.loadFileAsResource(fileName);
    }


    @Override
    public List<EmployeeDocument> getDocumentsByEmployee(Long employeeId) {
        return documentRepo.findByEmployeeId(employeeId);
    }

    @Override
    public void deleteDocument(Long id) {
        EmployeeDocument document = documentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        try {
            Files.deleteIfExists(Paths.get("C:/uploads/" + document.getFileName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        documentRepo.deleteById(id);
    }
}
