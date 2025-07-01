package com.medical.appointment.appointment.Controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.medical.appointment.appointment.Model.Assistance;
import com.medical.appointment.appointment.Model.Doctor;
import com.medical.appointment.appointment.Model.Rendezvous;
import com.medical.appointment.appointment.Repository.AssistanceRepository;
import com.medical.appointment.appointment.Service.AssistanceService;
import com.medical.appointment.appointment.Service.DoctorService;
import com.medical.appointment.appointment.Service.RendezvousService;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/assistance")
public class AssistanceController {
    
    private final AssistanceService assistanceService;
    private final DoctorService doctorService;
    private final TestController testController;
     private final RendezvousService rendezvousService;
     private final AssistanceRepository assistanceRepository;

      // Nouvelle méthode pour afficher les rendez-vous
    // @GetMapping("/appointments")
    // public String getDoctorAppointments(Model model) {
    //     String username = testController.getConnectedUsername();
        
    //     // Récupérer l'assistante connectée
    //     Assistance assistance = assistanceRepository.findByEmail(username)
    //         .orElseThrow(() -> new EntityNotFoundException("Assistante non trouvée"));
        
    //     // Récupérer le médecin associé
    //     Doctor doctor = assistance.getDoctor();
        
    //     // Récupérer les rendez-vous
    //     List<Rendezvous> appointments = rendezvousService.getRendezvousByDoctorId(doctor.getId());
        
    //     model.addAttribute("appointments", appointments);
    //     model.addAttribute("doctor", doctor);
    //     return "assistance/appointments";
    // }

    @GetMapping("/appointments")
public String getDoctorAppointments(Model model ,Authentication authentication) {
    try {
        String username = authentication.getName(); 
        // String username = testController.getConnectedUsername();
        log.info("Récupération des rendez-vous pour l'assistante : {}", username);
        
        Assistance assistance = assistanceRepository.findByEmail(username)
            .orElseThrow(() -> new EntityNotFoundException("Assistante non trouvée"));
        
        if(assistance.getDoctor() == null) {
            log.error("Aucun médecin associé à l'assistante {}", username);
            return "redirect:/error?code=no_doctor_linked";
        }
        
        List<Rendezvous> appointments = rendezvousService.getRendezvousByDoctorId(assistance.getDoctor().getId());
        log.info("Nombre de rendez-vous trouvés : {}", appointments.size());
        
        model.addAttribute("appointments", appointments);
        model.addAttribute("doctor", assistance.getDoctor());
        return "assistance/appointments";
        
    } catch (Exception e) {
        log.error("Erreur lors de la récupération des rendez-vous", e);
        return "redirect:/error?code=500";
    }
}


    // Afficher tous les assistants
    @GetMapping("/all")
    public String getAllAssistances(Model model) {
        List<Assistance> assistances = assistanceService.getAllAssistances();
        model.addAttribute("assistances", assistances);
        return "assistance/list";
    }
    
    // Afficher un assistant par ID
    @GetMapping("/{id}")
    public String getAssistance(@PathVariable Long id, Model model) {
        Assistance assistance = assistanceService.getAssistance(id);
        model.addAttribute("assistance", assistance);
        return "assistance/details";
    }
    
    // Afficher le formulaire d'ajout
    @GetMapping("/add")
    public String showAddAssistanceForm(Model model) {
        model.addAttribute("assistance", new Assistance());
        // Récupérer la liste des docteurs pour le dropdown
        List<Doctor> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        return "assistance/add";
    }
    
    // Traiter l'ajout d'un assistant
    @PostMapping("/add")
    public String addAssistance(@ModelAttribute Assistance assistance, @RequestParam Long doctorId, Model model) {
        try {
            String message = assistanceService.addAssistance(assistance, doctorId);
            model.addAttribute("successMessage", message);
            return "redirect:/assistance/all";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("assistance", assistance);
            List<Doctor> doctors = doctorService.getAllDoctors();
            model.addAttribute("doctors", doctors);
            return "assistance/add";
        }
    }
    
    // Afficher le formulaire de modification
    @GetMapping("/modify/{id}")
    public String showModifyForm(@PathVariable Long id, Model model) {
        Assistance assistance = assistanceService.getAssistance(id);
        model.addAttribute("assistance", assistance);
        return "assistance/modify";
    }
    
    // Traiter la modification d'un assistant
    @PostMapping("/modify/{id}")
    public String modifyAssistance(@ModelAttribute Assistance assistance, @PathVariable Long id, Model model) {
        try {
            String message = assistanceService.modifierAssistance(assistance, id);
            model.addAttribute("successMessage", message);
            return "redirect:/assistance/all";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("assistance", assistance);
            return "assistance/modify";
        }
    }
    
    // Traiter la suppression d'un assistant
    @GetMapping("/delete/{id}")
    public String deleteAssistance(@PathVariable Long id, Model model) {
        try {
            String message = assistanceService.deleteAssistance(id);
            model.addAttribute("successMessage", message);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/assistance/all";
    }
    @GetMapping("/home")
    public String getMethodName(Model model) {
        if(!testController.getConnectedUsername().isEmpty()){
            model.addAttribute("email", testController.getConnectedUsername());
        }
        return "assistance/home";
    }


// Ajouter ces nouvelles méthodes dans AssistanceController.java

@GetMapping("/profile")
public String viewProfile(Authentication authentication, Model model) {
    try {
        String email = authentication.getName();
        Assistance assistance = assistanceRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("Assistante non trouvée"));
        
        model.addAttribute("assistance", assistance);
        return "assistance/profile"; // Nom de la template HTML
        
    } catch (Exception e) {
        log.error("Erreur d'accès au profil", e);
        return "redirect:/error?code=profile_error";
    }
}

@GetMapping("/profile/edit")
public String editProfileForm(Authentication authentication, Model model) {
    try {
        String email = authentication.getName();
        Assistance assistance = assistanceRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("Assistante non trouvée"));
        
        model.addAttribute("assistance", assistance);
        return "assistance/edit_profile"; // Nom de la template HTML
        
    } catch (Exception e) {
        log.error("Erreur d'édition du profil", e);
        return "redirect:/error?code=edit_profile_error";
    }
}

@PostMapping("/profile/update")
public String updateProfile(
    @ModelAttribute("assistance") Assistance updatedAssistance,
    Authentication authentication,
    Model model
) {
    try {
        String email = authentication.getName();
        Assistance existingAssistance = assistanceRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("Assistante non trouvée"));

        // Mise à jour des champs modifiables
        existingAssistance.setFirstName(updatedAssistance.getFirstName());
        existingAssistance.setLastName(updatedAssistance.getLastName());
        existingAssistance.setAdresse(updatedAssistance.getAdresse());
        existingAssistance.setVille(updatedAssistance.getVille());
        
        assistanceRepository.save(existingAssistance);
        
        model.addAttribute("successMessage", "Profil mis à jour avec succès");
        return "redirect:/assistance/profile";
        
    } catch (Exception e) {
        log.error("Erreur de mise à jour du profil", e);
        model.addAttribute("errorMessage", "Erreur lors de la mise à jour : " + e.getMessage());
        return "assistance/assistance-edit-profile";
    }
}
    
}