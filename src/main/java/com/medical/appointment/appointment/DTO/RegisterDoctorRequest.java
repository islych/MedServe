package com.medical.appointment.appointment.DTO;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.medical.appointment.appointment.Model.enums.Specialty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDoctorRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String ville;
    private String adresse;
    private MultipartFile photo;
    private String photoUrl;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date_naissance;
    private Specialty specialty;
}
