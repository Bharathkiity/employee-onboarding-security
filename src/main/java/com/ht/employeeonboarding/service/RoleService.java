package com.ht.employeeonboarding.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ht.employeeonboarding.entity.Role;
import com.ht.employeeonboarding.repository.RoleRepository;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
 

    public Role createRole(String roleName) {
        Role role = new Role();
        role.setRoleName(roleName);  // Ensure roleName is set before saving
        return roleRepository.save(role);
    }
}