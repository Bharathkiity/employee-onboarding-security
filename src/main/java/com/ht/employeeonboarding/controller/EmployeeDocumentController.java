package com.ht.employeeonboarding.controller;

import com.ht.employeeonboarding.entity.EmployeeDocument;
import com.ht.employeeonboarding.service.EmployeeDocumentServiceI;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeDocumentController {

    @Autowired
    private EmployeeDocumentServiceI documentService;

    // 🔼 Upload document
    @PostMapping("/{employeeId}/documents")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> uploadDocument(@PathVariable Long employeeId,
                                            @RequestParam("documentType") String documentType,
                                            @RequestParam("file") MultipartFile file) {
        EmployeeDocument doc = documentService.uploadDocument(employeeId, documentType, file);
        return ResponseEntity.ok(doc);
    }

    // 🔽 Download document
    @GetMapping("/{employeeId}/documents/download/{fileName:.+}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long employeeId,
                                                     @PathVariable String fileName,
                                                     HttpServletRequest request) {
        try {
            String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
            Resource resource = documentService.downloadDocument(employeeId, decodedFileName);

            String contentType = getContentType(request, resource);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }

    // 👁️ View document inline (PDF, images, etc.)
    @GetMapping("/{employeeId}/documents/view/{fileName:.+}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Resource> viewDocument(@PathVariable Long employeeId,
                                                 @PathVariable String fileName,
                                                 HttpServletRequest request) {
        try {
            String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
            Resource resource = documentService.downloadDocument(employeeId, decodedFileName);

            // Set correct Content-Type based on file extension
            String contentType;
            if (decodedFileName.toLowerCase().endsWith(".pdf")) {
                contentType = "application/pdf";
            } else if (decodedFileName.toLowerCase().endsWith(".jpg") || decodedFileName.toLowerCase().endsWith(".jpeg")) {
                contentType = "image/jpeg";
            } else if (decodedFileName.toLowerCase().endsWith(".png")) {
                contentType = "image/png";
            } else {
                contentType = getContentType(request, resource); // fallback
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }

    // 📦 List all documents
    @GetMapping("/{employeeId}/documents")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<EmployeeDocument>> getAllDocuments(@PathVariable Long employeeId) {
        return ResponseEntity.ok(documentService.getDocumentsByEmployee(employeeId));
    }

    // ❌ Delete document
    @DeleteMapping("/documents/{documentId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long documentId) {
        documentService.deleteDocument(documentId);
        return ResponseEntity.noContent().build();
    }

    // 🔍 Detect content type
    private String getContentType(HttpServletRequest request, Resource resource) {
        String contentType = "application/octet-stream";
        try {
            String detected = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            if (detected != null) {
                contentType = detected;
            }
        } catch (IOException e) {
            // fallback
        }
        return contentType;
    }
}
