package com.medical.appointment.appointment.Auth;


import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.medical.appointment.appointment.DTO.AuthenticationRequest;
import com.medical.appointment.appointment.DTO.AuthenticationResponse;
import com.medical.appointment.appointment.DTO.RegisterAssistanceRequest;
import com.medical.appointment.appointment.DTO.RegisterDoctorRequest;
import com.medical.appointment.appointment.DTO.RegisterPatientRequest;
import com.medical.appointment.appointment.Model.Doctor;
import com.medical.appointment.appointment.Model.enums.Specialty;
import com.medical.appointment.appointment.Repository.DoctorRepository;
import com.medical.appointment.appointment.Service.FileStorageService;
// Ajouter en haut du fichier
import java.util.UUID;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final FileStorageService fileStorageService;

    private final AuthenticationService service;
    @Autowired
private DoctorRepository doctorRepository;



    @GetMapping("/register/patient")
    public String showRegisterPatientForm(Model model) {
        model.addAttribute("patientRequest", new RegisterPatientRequest());
        model.addAttribute("villes", getMarocCities());
        return "auth/register_patient";
    }

    @PostMapping("/register/patient")
    public String registerPatient(@ModelAttribute("patientRequest") RegisterPatientRequest request, Model model) {
        service.registerPatient(request); // on appelle juste la méthode
        model.addAttribute("message", "Patient registered successfully!");
        
        return "redirect:/auth/login";
    }
    // @PostMapping("/register/patient")
    // public String registerPatient(
    //     @Valid @ModelAttribute("patientRequest") RegisterPatientRequest request,
    //     BindingResult bindingResult,
    //     Model model
    // ) {
    //     if (bindingResult.hasErrors()) {
    //         return "register_patient";
    //     }
        
    //     try {
    //         service.registerPatient(request);
    //         model.addAttribute("message", "Inscription réussie !");
    //         return "redirect:/auth/login";
    //     } catch (Exception e) {
    //         model.addAttribute("error", "Erreur lors de l'inscription");
    //         return "register_patient";
    //     }
    // }


    @GetMapping("/register/doctor")
    public String showRegisterDoctorForm(Model model) {
        model.addAttribute("doctorRequest", new RegisterDoctorRequest());
        model.addAttribute("specialties", Specialty.values()); // Ajout des valeurs
        model.addAttribute("villes", getMarocCities());
        return "auth/register_doctor";
    }

    // @PostMapping("/register/doctor")
    // public String registerDoctor(@ModelAttribute("doctorRequest") RegisterDoctorRequest request, Model model) {
    //     service.registerDoctor(request);
    //     model.addAttribute("message", "Doctor registered successfully!");
    //     return "redirect:/auth/login";
    // }

// Modifier la méthode registerDoctor
@PostMapping("/register/doctor")
public String registerDoctor(
    @ModelAttribute("doctorRequest") RegisterDoctorRequest request,
    Model model
) {
    try {
        if (request.getPhoto() != null && !request.getPhoto().isEmpty()) {
            // 🧼 Nettoyer le nom du fichier
            String originalFilename = request.getPhoto().getOriginalFilename();
            String sanitizedFilename = UUID.randomUUID() + "_" + originalFilename.replaceAll("[^a-zA-Z0-9.-]", "_");

            // 📁 Enregistrer le fichier avec un nom propre
            String fileName = fileStorageService.storeFile(request.getPhoto(), sanitizedFilename);

            // 📌 Ajouter l'URL de l'image au modèle
            request.setPhotoUrl("/uploads/" + fileName);
        }

        service.registerDoctor(request);
        model.addAttribute("message", "Médecin enregistré avec succès !");
    } catch (Exception e) {
        model.addAttribute("error", "Erreur lors de l'enregistrement");
    }

    return "redirect:/auth/login";
}


    @GetMapping("/register/assistance")
    public String showRegisterAssistanceForm(Model model) {
        model.addAttribute("assistanceRequest", new RegisterAssistanceRequest());
        model.addAttribute("villes", getMarocCities());
        return "auth/register_assistance";
    }

    // @PostMapping("/register/assistance")
    // public String registerAssistance(@ModelAttribute("assistanceRequest") RegisterAssistanceRequest request, Model model) {
    //     service.registerAssistance(request);
    //     model.addAttribute("message", "Assistance registered successfully!");
    //     return "redirect:/auth/login";
    // }
@PostMapping("/register/assistance")
public String registerAssistance(@ModelAttribute("assistanceRequest") RegisterAssistanceRequest request,
                                Model model,
                                Principal principal) {
    
    // Récupération du médecin avec gestion de l'Optional
    Doctor doctor = doctorRepository.findByEmail(principal.getName())
        .orElseThrow(() -> new EntityNotFoundException("Médecin non trouvé !"));

    // Injection des informations du médecin
    request.setDoctorId(doctor.getId());
    request.setVille(doctor.getVille());
    request.setAdresse(doctor.getAdresse());

    service.registerAssistance(request);
    model.addAttribute("message", "Assistant enregistré avec succès !");
    
     return "redirect:/doctor/home";
}


    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("authRequest", new AuthenticationRequest());
        return "auth/login";
    }

    @PostMapping("/login")
    public String authenticate(
        @Validated @ModelAttribute("authRequest") AuthenticationRequest request,
        BindingResult bindingResult,
        Model model,
        HttpServletResponse response
    ) {
        if (bindingResult.hasErrors()) {
            return "auth/login";
        }
        
        try {
            AuthenticationResponse authResponse = service.authenticate(request);
            
            // Configuration du cookie JWT
            Cookie jwtCookie = new Cookie("JWT", authResponse.getToken());
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(true); // En production
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(3600);
            response.addCookie(jwtCookie);

            // Redirection selon le rôle
            return switch(authResponse.getUser().getRole()) {
                case PATIENT -> "redirect:/patients/home";
                case DOCTOR -> "redirect:/doctor/home";
                case ASSISTANCE -> "redirect:/assistance/home";
                default -> "redirect:/home";
            };
            
        } catch (AuthenticationException e) {
            model.addAttribute("error", "Email ou mot de passe invalide");
            return "auth/login";
        }
    }
    @ModelAttribute("cities")
    private List<String> getMarocCities() {
        return Arrays.asList(
            "Agadir", "Al Hoceïma", "Beni Mellal", "Berkane", "Casablanca",
            "Chefchaouen", "El Jadida", "Errachidia", "Essaouira", "Fès",
            "Guelmim", "Ifrane", "Kénitra", "Khouribga", "Laâyoune",
            "Marrakech", "Meknès", "Mohammédia", "Nador", "Ouarzazate",
            "Oujda", "Rabat", "Safi", "Salé", "Tanger",
            "Tétouan", "Tiznit", "Zagora"
        );
    }
    
}
