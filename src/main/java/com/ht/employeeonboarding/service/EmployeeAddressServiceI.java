package com.ht.employeeonboarding.service;

import java.util.List;
import java.util.Optional;

import com.ht.employeeonboarding.entity.EmployeeAddress;

public interface EmployeeAddressServiceI {
    EmployeeAddress saveAddressByEmployeeId(Long employeeId, EmployeeAddress address);
    List<EmployeeAddress> getAllAddresses();
    Optional<EmployeeAddress> getAddressById(Long id);
    EmployeeAddress updatePermanentAddress(Long employeeId, Long addressId, EmployeeAddress updatedAddress);
    EmployeeAddress updateTemporaryAddress(Long employeeId, Long addressId, EmployeeAddress updatedAddress);
    void deleteAddress(Long id);
    
    
	List<EmployeeAddress> getAllAddressesByEmployeeId(Long employeeId);
	void deleteAddressByEmployeeIdAndAddressId(Long employeeId, Long addressId);
}
