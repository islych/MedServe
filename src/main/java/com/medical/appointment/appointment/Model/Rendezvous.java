package com.medical.appointment.appointment.Model;


import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.medical.appointment.appointment.Model.enums.Statut;
import com.medical.appointment.appointment.Model.enums.Urgency;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
public class Rendezvous {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private String object;
    private String description;
    private String rejectionReason;

    @Enumerated(EnumType.STRING)
    private Urgency urgency;

    @Enumerated(EnumType.STRING)
    private Statut statut;

    @ManyToOne(fetch = FetchType.EAGER) // Chargement immédiat
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

     @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    public Rendezvous(){
        setStatut(Statut.En_Attend);
    }

    public String getRejectionReason() {
        return this.rejectionReason;
    }

    // public void setRejectionReason(Object rejectionReason) {
    //     if (rejectionReason != null) {
    //         this.rejectionReason = rejectionReason.toString();
    //     }
    // }\
    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}
