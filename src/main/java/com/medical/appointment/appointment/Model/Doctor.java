package com.medical.appointment.appointment.Model;


import java.util.List;

import com.medical.appointment.appointment.Model.enums.Role;
import com.medical.appointment.appointment.Model.enums.Specialty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Entity
@Data
@AllArgsConstructor
// @EqualsAndHashCode(callSuper = true)
@EqualsAndHashCode(callSuper = true, exclude = {"assistance", "rendezvousList"}) // Exclusion des relations
public class Doctor extends User {

    @OneToOne(mappedBy = "doctor", cascade = CascadeType.ALL)
    private Assistance assistance;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true ,fetch = FetchType.LAZY )
    private List<Rendezvous> rendezvousList;
        // private List<Rendezvous> rendezvousList = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    private Specialty specialty;

    public Doctor() {
        super.setRole(Role.DOCTOR);
    }
    @Transient // Champ non persisté
    public String getFullName() {
        return super.getFirstName() + " " + super.getLastName();
    }
}
