package com.ht.employeeonboarding.controller;

import com.ht.employeeonboarding.entity.EmployeeAddress;
import com.ht.employeeonboarding.service.EmployeeAddressServiceI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:4200") // Allow access from Angular frontend
@RestController
@RequestMapping("/api/employees/{employeeId}/addresses")
public class EmployeeAddressController {

    @Autowired
    private EmployeeAddressServiceI employeeAddressService;

    /**
     * Add an address to a specific employee
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Secures all methods in this controller
    public ResponseEntity<EmployeeAddress> addAddress(
            @PathVariable Long employeeId,
            @RequestBody EmployeeAddress address) {
        EmployeeAddress savedAddress = employeeAddressService.saveAddressByEmployeeId(employeeId, address);
        return ResponseEntity.ok(savedAddress);
    }
 
    /**
     * Get all addresses for a specific employee
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Secures all methods in this controller
    public ResponseEntity<List<EmployeeAddress>> getAllAddresses(
            @PathVariable Long employeeId) {
        List<EmployeeAddress> addresses = employeeAddressService.getAllAddressesByEmployeeId(employeeId);
        return ResponseEntity.ok(addresses);
    }

    /**
     * Get a specific address by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Secures all methods in this controller
    public ResponseEntity<EmployeeAddress> getAddressById(@PathVariable Long id) {
        Optional<EmployeeAddress> address = employeeAddressService.getAddressById(id);
        return address.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Update an existing permanent address
     */
    @PutMapping("/{addressId}/permanent")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Secures all methods in this controller
    public ResponseEntity<EmployeeAddress> updatePermanentAddress(
            @PathVariable Long employeeId,
            @PathVariable Long addressId,
            @RequestBody EmployeeAddress updatedAddress) {
        EmployeeAddress address = employeeAddressService.updatePermanentAddress(employeeId, addressId, updatedAddress);
        return ResponseEntity.ok(address);
    }

    /**
     * Update an existing temporary address
     */
    @PutMapping("/{addressId}/temporary")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Secures all methods in this controller
    public ResponseEntity<EmployeeAddress> updateTemporaryAddress(
            @PathVariable Long employeeId,
            @PathVariable Long addressId,
            @RequestBody EmployeeAddress updatedAddress) {
        EmployeeAddress address = employeeAddressService.updateTemporaryAddress(employeeId, addressId, updatedAddress);
        return ResponseEntity.ok(address);
    }

    /**
     * Delete an address
     */
    @DeleteMapping("/{addressId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long employeeId,
            @PathVariable Long addressId) {

        employeeAddressService.deleteAddressByEmployeeIdAndAddressId(employeeId, addressId);
        return ResponseEntity.noContent().build();
    }

    
}
