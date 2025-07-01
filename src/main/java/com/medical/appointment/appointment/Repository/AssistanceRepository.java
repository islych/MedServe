package com.medical.appointment.appointment.Repository;

import com.medical.appointment.appointment.Model.Assistance;
import com.medical.appointment.appointment.Model.Doctor;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AssistanceRepository extends JpaRepository<Assistance, Long>{
     public Optional<Assistance> findByEmail(String email);
    
}
