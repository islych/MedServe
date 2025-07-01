package com.medical.appointment.appointment.Model;

import com.medical.appointment.appointment.Model.enums.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;



@SuperBuilder
@Entity
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Assistance extends User {

@OneToOne(fetch = FetchType.EAGER) // Forcer le chargement immédiat
    // @JoinColumn(name = "doctor_id")
    @JoinColumn(name = "doctor_id", nullable = false) // Ajouter nullable=false
    private Doctor doctor;

    public Assistance() {
        super.setRole(Role.ASSISTANCE);
    }
}
