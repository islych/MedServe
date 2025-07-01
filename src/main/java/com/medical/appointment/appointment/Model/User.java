package com.medical.appointment.appointment.Model;

import jakarta.persistence.Column;



import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import jakarta.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.medical.appointment.appointment.Model.enums.Role;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true, nullable = false)
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String photoUrl; // Ajouter cette ligne
    private String firstName;
    private String lastName;
    private String ville;
    private String adresse;
    private int age;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date_naissance;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.getRole().name()));
    }
    
    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
    
    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }
    
    @Override
    public String getPassword() {
        return this.password;
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
        
    @Transient
    public int calculateAge() {
        if (this.date_naissance == null) return 0;
        LocalDate birthDate = this.date_naissance.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    @PrePersist
    @PreUpdate
    private void setAgeFromBirthDate() {
        this.age = this.calculateAge();
    }
}