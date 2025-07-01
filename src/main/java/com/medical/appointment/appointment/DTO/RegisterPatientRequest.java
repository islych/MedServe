package com.medical.appointment.appointment.DTO;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterPatientRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String ville;
    private String adresse;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date_naissance;
    private List<String> medicaList;
}