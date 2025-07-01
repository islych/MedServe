package com.medical.appointment.appointment.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.medical.appointment.appointment.Model.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor,Long> {
    
    public Optional<Doctor> findByEmail(String email);
    
    @Query("SELECT d FROM Doctor d JOIN d.assistance a WHERE a.id = :assistanceId")
    Optional<Doctor> findDoctorByAssistanceId(@Param("assistanceId") Long assistanceId);
}
