package com.medical.appointment.appointment.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.medical.appointment.appointment.Model.User;

public interface Userreposirory extends JpaRepository<User,Long>{

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);
        
    boolean existsByEmail(String email);
}