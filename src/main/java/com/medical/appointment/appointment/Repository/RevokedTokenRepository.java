package com.medical.appointment.appointment.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.medical.appointment.appointment.Model.RevokedToken;

import java.util.Optional;

public interface RevokedTokenRepository extends JpaRepository<RevokedToken, Long> {
    Optional<RevokedToken> findByToken(String token);
}
