package com.medical.appointment.appointment.Repository;

import java.util.Date;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.medical.appointment.appointment.Model.Rendezvous;
import com.medical.appointment.appointment.Model.enums.Statut;
import com.medical.appointment.appointment.Model.enums.Urgency;

@Repository
public interface RendezvousRepository extends JpaRepository<Rendezvous, Long> {
    List<Rendezvous> findByPatientId(Long patientId);
    List<Rendezvous> findByDoctorId(Long doctorId);
    List<Rendezvous> findByStatut(Statut statut);
    List<Rendezvous> findByUrgency(Urgency urgency);
    List<Rendezvous> findByDateBetween(Date startDate, Date endDate);
    List<Rendezvous> findByDoctorIdAndStatut(Long doctorId, Statut statut);
    @Query("SELECT r FROM Rendezvous r WHERE r.doctor.id = :doctorId AND r.date BETWEEN :startDate AND :endDate")
    List<Rendezvous> findByDoctorIdAndDateRange(Long doctorId, Date startDate, Date endDate);
    
    @Query("SELECT r FROM Rendezvous r WHERE r.patient.id = :patientId AND r.statut = :statut")
    List<Rendezvous> findByPatientIdAndStatut(Long patientId, Statut statut);
    List<Rendezvous> findByDoctorIdOrderByDateDesc(Long doctorId);

        @Query("SELECT r FROM Rendezvous r LEFT JOIN FETCH r.patient WHERE r.doctor.id = :doctorId ORDER BY r.date DESC")
    List<Rendezvous> findByDoctorIdWithPatient(@Param("doctorId") Long doctorId);
}