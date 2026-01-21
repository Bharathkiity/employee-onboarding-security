package com.ht.employeeonboarding.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ht.employeeonboarding.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserEmail(String userEmail);     // ✅ updated

    void deleteByUserEmail(String userEmail);             // ✅ updated

    boolean existsByUserEmail(String userEmail);          // ✅ updated
}
