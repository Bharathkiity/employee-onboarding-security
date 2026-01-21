package com.ht.employeeonboarding.service;


import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ht.employeeonboarding.entity.Role;
import com.ht.employeeonboarding.entity.User;
import com.ht.employeeonboarding.repository.RoleRepository;
import com.ht.employeeonboarding.repository.UserRepository;

@Component
public class RoleInitializerService implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RoleInitializerService(RoleRepository roleRepository, 
                           UserRepository userRepository, 
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
        initializeAdminUser();
    }

    // Method to initialize default roles
    private void initializeRoles() {
        if (!roleRepository.existsByRoleName("ADMIN")) {
            Role adminRole = new Role();
            adminRole.setRoleName("ADMIN");
            roleRepository.save(adminRole);
            System.out.println("✅ ROLE_ADMIN created successfully.");
        } else {
            System.out.println("✅ ROLE_ADMIN already exists.");
        }

        if (!roleRepository.existsByRoleName("USER")) {
            Role userRole = new Role();
            userRole.setRoleName("USER");
            roleRepository.save(userRole);
            System.out.println("✅ ROLE_USER created successfully.");
        } else {
            System.out.println("✅ ROLE_USER already exists.");
        }
    }

    // Method to initialize the default admin user
    private void initializeAdminUser() {
        if (!userRepository.existsByUserEmail("admin@gmail.com")) {
            User adminUser = new User();
            adminUser.setUserEmail("admin@gmail.com");
            adminUser.setUserPassword(passwordEncoder.encode("admin123"));

            Role adminRole = roleRepository.findByRoleName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("❌ ADMIN role not found."));

            adminUser.setRole(adminRole);
            userRepository.save(adminUser);
            System.out.println("✅ Admin user (admin@gmail.com) created successfully.");
        } else {
            System.out.println("✅ Admin user (admin@gmail.com) already exists.");
        }
    }

}
