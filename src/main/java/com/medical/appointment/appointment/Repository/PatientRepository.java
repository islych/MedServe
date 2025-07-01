package com.medical.appointment.appointment.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.medical.appointment.appointment.Model.Patient;

public interface PatientRepository extends JpaRepository<Patient,Long> {
     public Optional<Patient> findByEmail(String email);
     // public Patient findByEmail(String email);

}
