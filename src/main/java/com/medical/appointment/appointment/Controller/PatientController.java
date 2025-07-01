package com.medical.appointment.appointment.Controller;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.medical.appointment.appointment.Model.Assistance;
import com.medical.appointment.appointment.Model.Doctor;
import com.medical.appointment.appointment.Model.Notification;
import com.medical.appointment.appointment.Model.Patient;
import com.medical.appointment.appointment.Model.Rendezvous;
import com.medical.appointment.appointment.Repository.DoctorRepository;
import com.medical.appointment.appointment.Repository.NotificationRepository;
import com.medical.appointment.appointment.Repository.PatientRepository;
import com.medical.appointment.appointment.Service.DoctorService;
import com.medical.appointment.appointment.Service.PatientService;
import com.medical.appointment.appointment.Service.RendezvousService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {
    
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final TestController testController;

@Autowired
private DoctorRepository doctorRepository;

    private final PatientRepository patientRepository;
    private final RendezvousService rendezvousService;
     private final NotificationRepository notificationRepository;
    // Afficher tous les patients
    // @GetMapping("/all")
    // public String getAllPatients(Model model) {
    //     List<Doctor> doctors = doctorService.getAllDoctors();
    //     model.addAttribute("doctors", doctors);
    //     return "patient/list";
    // }

    @GetMapping("/doctors")
    public String getAllDoctors(Model model) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        return "patient/doctors_list";
    }

    @GetMapping("/appointments")
    public String getPatientAppointments(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            log.info("Tentative de récupération des RDV pour le patient : {}", email); // Log critique
    
            Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Patient non trouvé avec l'email : {}", email);
                    return new EntityNotFoundException("Patient non trouvé");
                });
    
            List<Rendezvous> appointments = rendezvousService.getRendezvousByPatientId(patient.getId());
            log.debug("RDV trouvés : {}", appointments); // Vérifiez les données ici
    
            model.addAttribute("appointments", appointments);
            return "patient/appointments";
        } catch (EntityNotFoundException e) {
            // Log spécifique
            return "error/patient-not-found";
        } catch (Exception e) {
            log.error("Erreur inattendue : ", e); // Stack trace complète
            return "error/generic-error";
        }
    }


        @GetMapping("/all")
    public String getAllPatients(Model model) {
        List<Patient> patients = patientService.getAllPatients(); // Récupère la liste des patients
        model.addAttribute("patients", patients); // Ajoute la liste au modèle
        return "patient/list"; // Retourne la vue
    }
    
    // Afficher un patient par ID
    @GetMapping("/{id}")
    public String getPatient(@PathVariable Long id, Model model) {
        Patient patient = patientService.getPatient(id);
        model.addAttribute("patient", patient);
        return "patient/details";
    }
    
    // Afficher le formulaire d'ajout
    @GetMapping("/add")
    public String showAddPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "patient/add";
    }
    
    // Traiter l'ajout d'un patient
    @PostMapping("/add")
    public String addPatient(@ModelAttribute Patient patient, Model model) {
        try {
            String message = patientService.addPatient(patient);
            model.addAttribute("successMessage", message);
            return "redirect:/patients/";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("patient", patient);
            return "patient/add";
        }
    }
    
    // Afficher le formulaire de modification
    @GetMapping("/modify/{id}")
    public String showModifyForm(@PathVariable Long id, Model model) {
        Patient patient = patientService.getPatient(id);
        model.addAttribute("patient", patient);
        return "patient/modify";
    }
    
    // Traiter la modification d'un patient
    @PostMapping("/modify/{id}")
    public String modifyPatient(@ModelAttribute Patient patient, @PathVariable Long id, Model model) {
        try {
            String message = patientService.modifierPatient(patient, id);
            model.addAttribute("successMessage", message);
            return "redirect:/patients/";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("patient", patient);
            return "patient/modify";
        }
    }
    
    // Traiter la suppression d'un patient
    @GetMapping("/delete/{id}")
    public String deletePatient(@PathVariable Long id, Model model) {
        try {
            String message = patientService.deletePatient(id);
            model.addAttribute("successMessage", message);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/patients/";
    }
    @GetMapping("/home")
    public String getMethodName(Model model) {
        if(!testController.getConnectedUsername().isEmpty()){
            model.addAttribute("email", testController.getConnectedUsername());
        }
        return "patient/home";
    }
// @GetMapping("/doctor/home")
// public String doctorHome(Model model, Principal principal) {
//     // Récupération du médecin connecté
//     Doctor doctor = doctorRepository.findByEmail(principal.getName())
//                           .orElseThrow(() -> new UsernameNotFoundException("Docteur non trouvé"));
//      Hibernate.initialize(doctor.getAssistance());

//     model.addAttribute("doctor", doctor);
//     model.addAttribute("email", principal.getName()); // conservation de l'email si nécessaire
    
//     return "doctor/home"; // correspond au template home.html
// }

    @GetMapping("/profile")
public String patientProfile(
    @AuthenticationPrincipal UserDetails userDetails, 
    Model model
) {
    try {
        String email = userDetails.getUsername();
        Patient patient = patientRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("Patient non trouvé"));
        
        model.addAttribute("patient", patient);
        return "patient/profile";
        
    } catch (EntityNotFoundException e) {
        log.error("Profil patient introuvable : {}", e.getMessage());
        return "redirect:/error?code=patient_not_found";
    }
}

@GetMapping("/profile/edit")
public String showEditProfileForm(
    @AuthenticationPrincipal UserDetails userDetails,
    Model model
) {
    Patient patient = patientRepository.findByEmail(userDetails.getUsername())
        .orElseThrow(() -> new EntityNotFoundException("Patient non trouvé"));
    
    model.addAttribute("patient", patient);
    return "patient/edit-profile";
}

@PostMapping("/profile/update")
public String updateProfile(
    @ModelAttribute Patient updatedPatient,
    @AuthenticationPrincipal UserDetails userDetails
) {
    try {
        Patient existingPatient = patientRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new EntityNotFoundException("Patient non trouvé"));
        
        // Mise à jour des champs modifiables
        existingPatient.setFirstName(updatedPatient.getFirstName());
        existingPatient.setLastName(updatedPatient.getLastName());
        existingPatient.setAdresse(updatedPatient.getAdresse());
        existingPatient.setVille(updatedPatient.getVille());
        
        patientRepository.save(existingPatient);
        return "redirect:/patients/profile?success";
        
    } catch (Exception e) {
        log.error("Erreur mise à jour profil : {}", e.getMessage());
        return "redirect:/patients/profile?error";
    }
}

//     @GetMapping("/patients/home")
// public String patientHome(
//     @AuthenticationPrincipal UserDetails userDetails, 
//     Model model
// ) {
//     // Récupérer le patient connecté
//     Patient patient = patientRepository.findByEmail(userDetails.getUsername())
//         .orElseThrow(() -> new EntityNotFoundException("Patient non trouvé"));

//     // Récupérer les notifications non lues
//     List<Notification> notifications = notificationRepository.findByPatientAndIsReadFalse(patient);
    
//     model.addAttribute("notifications", notifications);
//     model.addAttribute("patient", patient);
    
//     return "patient/home";
// }  
    @ModelAttribute("cities")
    public List<String> getMarocCities() {
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