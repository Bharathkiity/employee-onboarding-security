package com.ht.employeeonboarding.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ht.employeeonboarding.entity.JwtResponse;
import com.ht.employeeonboarding.entity.RefreshToken;
import com.ht.employeeonboarding.entity.Role;
import com.ht.employeeonboarding.entity.User;
import com.ht.employeeonboarding.exception.ResourceNotFoundException;
import com.ht.employeeonboarding.repository.RoleRepository;
import com.ht.employeeonboarding.repository.UserRepository;
import com.ht.employeeonboarding.util.JwtUtil;

import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Transactional
    public JwtResponse registerNewUser(User user) throws Exception {
        if (userRepository.existsByUserEmail(user.getUserEmail())) {
            throw new IllegalArgumentException("Username already exists");
        }

        Role userRole = roleRepository.findByRoleName("USER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRoleName("USER");
                    return roleRepository.save(newRole);
                });

        user.setRole(userRole);
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        User savedUser = userRepository.save(user);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
            savedUser.getUserEmail(),
            savedUser.getUserPassword(),
            getAuthorities(savedUser)
        );

        String accessToken = jwtUtil.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedUser.getId());

        return JwtResponse.builder()
                .userEmail(savedUser.getUserEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .role(savedUser.getRole().getRoleName())
                .build();
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUserEmail(),
                user.getUserPassword(),
                getAuthorities(user)
        );
    }

    private Set<SimpleGrantedAuthority> getAuthorities(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName()));
        return authorities;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // retrieves logged-in username
        return userRepository.findByUserEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User"));
    }

    // ======= Added/Fixed Admin Functions Below ========

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(String username) {
        return userRepository.findByUserEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    public User updateUser(String username, User updatedUser) {
        User existingUser = userRepository.findByUserEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        // Update fields as needed
        existingUser.setUserEmail(updatedUser.getUserEmail() != null ? updatedUser.getUserEmail() : existingUser.getUserEmail());
        existingUser.setUserPassword(updatedUser.getUserPassword() != null ?
                passwordEncoder.encode(updatedUser.getUserPassword()) : existingUser.getUserPassword());
        if (updatedUser.getRole() != null) {
            Role role = roleRepository.findByRoleName(updatedUser.getRole().getRoleName())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + updatedUser.getRole().getRoleName()));
            existingUser.setRole(role);
        }

        return userRepository.save(existingUser);
    }

    @Transactional
    public void deleteUser(String username) {
        User user = userRepository.findByUserEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        // Delete refresh token if it exists
        refreshTokenService.deleteByUser(user);

        // Then delete the user
        userRepository.delete(user);
    }
}
