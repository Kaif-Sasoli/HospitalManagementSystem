package com.example.hospitalmanagementsystem.repository;

import com.example.hospitalmanagementsystem.entity.RefreshToken;
import com.example.hospitalmanagementsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByJti(String jti);

    Optional<RefreshToken> findFirstByUserAndRevokedFalseOrderByCreatedAtDesc(User user);
}
