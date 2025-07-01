package com.medical.appointment.appointment.Repository;

import com.medical.appointment.appointment.Model.Notification;
import com.medical.appointment.appointment.Model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    // Trouver toutes les notifications d'un patient
    List<Notification> findByPatient(Patient patient);
    
    // Trouver les notifications non lues d'un patient
    List<Notification> findByPatientAndIsReadFalse(Patient patient);
    
    // Trouver les notifications par statut de lecture
    List<Notification> findByIsRead(boolean isRead);
}