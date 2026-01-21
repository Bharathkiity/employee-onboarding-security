package com.ht.employeeonboarding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ht.employeeonboarding.entity.JwtResponse;
import com.ht.employeeonboarding.entity.User;
import com.ht.employeeonboarding.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JwtResponse> registerNewUser(@RequestBody User user) {
        try {
            JwtResponse response = userService.registerNewUser(user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // log for debugging
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<User> getUserProfile() {
        try {
            User user = userService.getCurrentUser();
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> userDashboard() {
        return ResponseEntity.ok("Welcome to User Dashboard");
    }

    

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminDashboard() {
        return ResponseEntity.ok("Welcome to Admin Dashboard");
    }
    
    
    
    
    // 🔽 Add this method for admin to create users for employees
    @PostMapping("/createUserForEmployee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JwtResponse> createUserForEmployee(@RequestBody User user) throws Exception {
        JwtResponse response = userService.registerNewUser(user);
        return ResponseEntity.ok(response);
    }

}