package com.medical.appointment.appointment.Model;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.List;

import com.medical.appointment.appointment.Model.enums.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Entity
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Patient extends User{
    
    private List<String> medicaList;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rendezvous> rendezvousList;
    
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Notification> notifications;

    
    public Patient() {
        super.setRole(Role.PATIENT);
    }


    public Patient orElseThrow(Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'orElseThrow'");
    }


}
