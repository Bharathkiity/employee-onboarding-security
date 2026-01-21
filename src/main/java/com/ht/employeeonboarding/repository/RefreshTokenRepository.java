package com.ht.employeeonboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ht.employeeonboarding.entity.RefreshToken;
import com.ht.employeeonboarding.entity.User;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshToken rt WHERE rt.user.id = :userId")
    void deleteByUserId(Long userId);
    
    void deleteByUser(User user);
    
    // Add this method to find all revoked tokens
    List<RefreshToken> findByRevoked(boolean revoked);

    // existing method(s)
   // Optional<RefreshToken> findByToken(String token);

}