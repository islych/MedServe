package com.medical.appointment.appointment.Auth;


import com.medical.appointment.appointment.Model.Doctor;
import com.medical.appointment.appointment.Model.Patient;
import com.medical.appointment.appointment.Model.User;
import com.medical.appointment.appointment.Conf.JwtService;
import com.medical.appointment.appointment.DTO.AuthenticationRequest;
import com.medical.appointment.appointment.DTO.AuthenticationResponse;
import com.medical.appointment.appointment.DTO.RegisterAssistanceRequest;
import com.medical.appointment.appointment.DTO.RegisterDoctorRequest;
import com.medical.appointment.appointment.DTO.RegisterPatientRequest;
import com.medical.appointment.appointment.Model.Assistance;
import com.medical.appointment.appointment.Model.enums.Role;
import com.medical.appointment.appointment.Repository.DoctorRepository;
import com.medical.appointment.appointment.Repository.PatientRepository;
import com.medical.appointment.appointment.Repository.Userreposirory;
import com.medical.appointment.appointment.Repository.AssistanceRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final Userreposirory userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AssistanceRepository assistanceRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponse registerPatient(RegisterPatientRequest request) {
        var patient = Patient.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .ville(request.getVille())
                .adresse(request.getAdresse())
         
                .date_naissance(request.getDate_naissance())
                .medicaList(request.getMedicaList())
                .role(Role.PATIENT)
                .build();

                
        
                patientRepository.save(patient);
        patientRepository.save(patient);
        var jwtToken = jwtService.generateToken(patient);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(patient)
                .build();
    }

    @Transactional
    public AuthenticationResponse registerDoctor(RegisterDoctorRequest request) {
        var doctor = Doctor.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .ville(request.getVille())
                .adresse(request.getAdresse())
                .photoUrl(request.getPhotoUrl()) 
                .date_naissance(request.getDate_naissance())
                .specialty(request.getSpecialty())
                .role(Role.DOCTOR)
                .build();
        doctorRepository.save(doctor);
        var jwtToken = jwtService.generateToken(doctor);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(doctor)
                .build();
    }

    @Transactional
    public AuthenticationResponse registerAssistance(RegisterAssistanceRequest request) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        
        var assistance = Assistance.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .ville(request.getVille())
                .adresse(request.getAdresse())
               
                .date_naissance(request.getDate_naissance())
                .doctor(doctor)
                .role(Role.ASSISTANCE)
                .build();
        assistanceRepository.save(assistance);
        var jwtToken = jwtService.generateToken(assistance);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(assistance)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Authentification via Spring Security
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        return AuthenticationResponse.builder()
            .token(jwtService.generateToken(user))
            .user(user)
            .build();
    }
}