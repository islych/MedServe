package com.medical.appointment.appointment.Model;


import jakarta.persistence.Entity;
import lombok.Data;

import java.util.Date;

import jakarta.persistence.*;

@Entity
@Data
public class RevokedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Date expirationDate;
}

