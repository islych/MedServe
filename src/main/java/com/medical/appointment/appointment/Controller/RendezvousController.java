
package com.medical.appointment.appointment.Controller;





import com.medical.appointment.appointment.Model.Doctor;
import com.medical.appointment.appointment.Model.Notification;
import com.medical.appointment.appointment.Model.Patient;
import com.medical.appointment.appointment.Model.Rendezvous;
import com.medical.appointment.appointment.Model.enums.Statut;
import com.medical.appointment.appointment.Model.enums.Urgency;
import com.medical.appointment.appointment.Repository.NotificationRepository;
import com.medical.appointment.appointment.Repository.PatientRepository;
import com.medical.appointment.appointment.Service.DoctorService;
import com.medical.appointment.appointment.Service.RendezvousService;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

import org.hibernate.Hibernate;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping("/rendezvous")
@AllArgsConstructor
public class RendezvousController {

    private final RendezvousService rendezvousService;
    private final DoctorService doctorService; 
    private final TestController testController;
    private final PatientRepository patientRepository;
       private final NotificationRepository notificationRepository;
    
    
       // Ajoutez le InitBinder ici ▼
    // @InitBinder
    // public void initBinder(WebDataBinder binder) {
    //     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    //     dateFormat.setLenient(false); // Désactive l'interprétation laxiste des dates
        
    //     binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    // }
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(LocalDateTime.parse(text, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }
        });
    }

    @GetMapping
    public String getAllRendezvous(Model model) {
        model.addAttribute("rendezvousList", rendezvousService.getAllRendezvous());
        return "rendezvous/list";
    }

    // @GetMapping("/{id}")
    // public String getRendezvousById(@PathVariable Long id, Model model) {
    //     Optional<Rendezvous> rendezvous = rendezvousService.getRendezvousById(id);
    //     if (rendezvous.isPresent()) {
    //         model.addAttribute("rendezvous", rendezvous.get());
    //         return "rendezvous/detail";
    //     }
    //     return "redirect:/rendezvous";
    // }
// @GetMapping("/{id}")
// public String getRendezvousById(@PathVariable Long id, Model model) {
//     try {
//         Rendezvous rdv = rendezvousService.getRendezvousById(id)
//             .orElseThrow(() -> new EntityNotFoundException("RDV non trouvé"));
        
//         // Force le chargement des relations si nécessaire
//         Hibernate.initialize(rdv.getPatient()); 
//         Hibernate.initialize(rdv.getDoctor());
        
//         model.addAttribute("rendezvous", rdv);
//         return "rendezvous/detail";
        
//     } catch (EntityNotFoundException e) {
//         return "redirect:/rendezvous?error=not_found"; // Redirection personnalisée
//     }
// }
@GetMapping("/{id}")
public String getRendezvousById(
    @PathVariable Long id, 
    Model model,
    @AuthenticationPrincipal UserDetails userDetails){
    try {
        Rendezvous rdv = rendezvousService.getRendezvousById(id)
            .orElseThrow(() -> new EntityNotFoundException("RDV non trouvé"));

        // Détermine si l'utilisateur est une assistante
        boolean isAssistant = userDetails.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ASSISTANCE"));

        model.addAttribute("isAssistant", isAssistant); // ⚠️ Ajouté
        model.addAttribute("rendezvous", rdv);

        return "rendezvous/modify";

    } catch (EntityNotFoundException e) {
        log.error("RDV introuvable : ID {}", id);
        return "redirect:/error?code=404";
    } catch (Exception e) {
        log.error("Erreur critique sur le RDV {} : {}", id, e.toString()); // Affiche toute exception
        return "redirect:/error?code=500";
    }
}




    // @GetMapping("/new")
    // public String showCreateForm(Model model) {
    //     model.addAttribute("rendezvous", new Rendezvous());
    //     model.addAttribute("statuts", Statut.values());
    //     model.addAttribute("urgencyLevels", Urgency.values());
    //     return "rendezvous/add";
    // }

    @GetMapping("/new")
    public String showCreateForm(@RequestParam Long doctorId, Model model) {
        try {
        
            Doctor doctor = doctorService.getDoctor(doctorId);
            Rendezvous rendezvous = new Rendezvous();
            rendezvous.setDoctor(doctor);
            model.addAttribute("rendezvous", rendezvous);
            model.addAttribute("statuts", Statut.values());
            model.addAttribute("urgencyLevels", Urgency.values());
            return "rendezvous/add";
        } catch (EntityNotFoundException e) {
            // Log l'erreur
            log.error("Médecin introuvable : ID " + doctorId, e);
            return "redirect:/patients/doctors?error=doctor_not_found";
        } catch (Exception e) {
            // Gestion des autres exceptions
            log.error("Erreur inattendue", e);
            return "redirect:/error?code=500";
        }
    }

    // @PostMapping("/save")
    // public String saveRendezvous(@ModelAttribute Rendezvous rendezvous) {
    //     String username = testController.getConnectedUsername();
    //     rendezvous.setPatient(patientRepository.findByEmail(username));
    //     rendezvousService.saveRendezvous(rendezvous);
    //     return "redirect:/patients/doctors";
    // }
  @PostMapping("/save")
public String saveRendezvous(@ModelAttribute Rendezvous rendezvous) {
    String username = testController.getConnectedUsername();
    
    // Récupérer le patient avec gestion d'erreur
    Patient patient = patientRepository.findByEmail(username)
        .orElseThrow(() -> new EntityNotFoundException("Patient non trouvé"));
    
    rendezvous.setPatient(patient); // Maintenant un Patient, pas un Optional
    rendezvousService.saveRendezvous(rendezvous);
    return "redirect:/patients/doctors";
}
    // @GetMapping("/edit/{id}")
    // public String showEditForm(@PathVariable Long id, Model model) {
    //     Optional<Rendezvous> rendezvous = rendezvousService.getRendezvousById(id);
    //     if (rendezvous.isPresent()) {
    //         model.addAttribute("rendezvous", rendezvous.get());
    //         model.addAttribute("statuts", Statut.values());
    //         model.addAttribute("urgencyLevels", Urgency.values());
    //         return "rendezvous/modify";
    //     }
    //     return "redirect:/rendezvous";
    // }

    @GetMapping("/edit/{id}")
public String showEditForm(
    @PathVariable Long id, 
    Model model,
    @AuthenticationPrincipal UserDetails userDetails // Récupère l'utilisateur connecté
) {
    Optional<Rendezvous> rendezvous = rendezvousService.getRendezvousById(id);
    if (rendezvous.isPresent()) {
        model.addAttribute("rendezvous", rendezvous.get());
        model.addAttribute("statuts", Statut.values());
        model.addAttribute("urgencyLevels", Urgency.values());
        
        // Vérifie si l'utilisateur est une assistante
        boolean isAssistant = userDetails.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ASSISTANCE"));
        model.addAttribute("isAssistant", isAssistant);
        
        return "rendezvous/modify";
    }
    return "redirect:/rendezvous";
}
    

    // @PostMapping("/update/{id}")
    // public String updateRendezvous(@PathVariable Long id, @ModelAttribute Rendezvous rendezvous) {
    //     rendezvous.setId(id);
    //     rendezvousService.saveRendezvous(rendezvous);
    //     return "redirect:/rendezvous/" + id;
    // }
//     @PostMapping("/update/{id}")
// public String updateRendezvous(
//     @PathVariable Long id, 
//     @ModelAttribute Rendezvous rendezvous,
//     @AuthenticationPrincipal UserDetails userDetails
// ) {
//     // Récupère le RDV existant
//     Rendezvous existingRdv = rendezvousService.getRendezvousById(id)
//         .orElseThrow(() -> new EntityNotFoundException("RDV non trouvé"));

//     // Pour les assistantes : ne met à jour que le statut et la raison
//     if (userDetails.getAuthorities().stream()
//         .anyMatch(a -> a.getAuthority().equals("ROLE_ASSISTANCE"))) {
        
//         existingRdv.setStatut(rendezvous.getStatut());
//         if (rendezvous.getStatut() == Statut.Annule) {
//             existingRdv.setRejectionReason(rendezvous.getRejectionReason());
//         }
//     } 

//     rendezvousService.saveRendezvous(existingRdv);
//     return "redirect:/rendezvous/" + id;
// }

// Méthode à mettre à jour dans RendezvousController.java

@PostMapping("/update/{id}")
public String updateRendezvous(
    @PathVariable Long id, 
    @ModelAttribute Rendezvous rendezvous,
    @AuthenticationPrincipal UserDetails userDetails
) {
    try {
        // Récupère le RDV existant
        Rendezvous existingRdv = rendezvousService.getRendezvousById(id)
            .orElseThrow(() -> new EntityNotFoundException("RDV non trouvé"));

        // Pour les assistantes : ne met à jour que le statut et la raison d'annulation si nécessaire
        if (userDetails.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ASSISTANCE"))) {
            
            // Vérifie si le statut change
            if (existingRdv.getStatut() != rendezvous.getStatut()) {
                log.info("Mise à jour du statut du RDV {} : {} -> {}", 
                         id, existingRdv.getStatut(), rendezvous.getStatut());
                
                existingRdv.setStatut(rendezvous.getStatut());
                
                // Si le statut est annulé, enregistre la raison
                if (rendezvous.getStatut() == Statut.Annule) {
                    existingRdv.setRejectionReason(rendezvous.getRejectionReason());
                    log.info("Raison d'annulation : {}", rendezvous.getRejectionReason());
                }
            }
             // Créer une notification
             Notification notification = new Notification();
             notification.setMessage("Votre rendez-vous du " 
                 + existingRdv.getDate() + " a été annulé. Raison : " 
                 + existingRdv.getRejectionReason());
             notification.setPatient(existingRdv.getPatient());
             
             notificationRepository.save(notification); // Injectez le repository
         
        } else {
            // Pour les autres utilisateurs : mise à jour complète si autorisé
            // Vous pouvez ajouter d'autres logiques selon les besoins
            log.info("Mise à jour complète du RDV {} par {}", id, userDetails.getUsername());
            
            // Conserver les relations existantes
            existingRdv.setDate(rendezvous.getDate());
            existingRdv.setObject(rendezvous.getObject());
            existingRdv.setUrgency(rendezvous.getUrgency());
            existingRdv.setStatut(rendezvous.getStatut());
            existingRdv.setRejectionReason(rendezvous.getRejectionReason());
            // Conservez les relations patient et docteur
        }

        // Sauvegarde les modifications
        rendezvousService.saveRendezvous(existingRdv);
        
        // Redirige vers la page appropriée
        if (userDetails.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ASSISTANCE"))) {
            return "redirect:/assistance/appointments"; // Redirection vers la liste des RDV pour les assistantes
        } else {
            return "redirect:/rendezvous/" + id; // Redirection vers la page de détail du RDV
        }
    } catch (EntityNotFoundException e) {
        log.error("RDV introuvable : ID {}", id);
        return "redirect:/error?code=404&message=RDV+introuvable";
    } catch (Exception e) {
        log.error("Erreur lors de la mise à jour du RDV {} : {}", id, e.getMessage(), e);
        return "redirect:/error?code=500&message=Erreur+lors+de+la+mise+à+jour";
    }
}

    @PostMapping("/delete/{id}")
    public String deleteRendezvous(@PathVariable Long id) {
        rendezvousService.deleteRendezvous(id);
        return "redirect:/rendezvous";
    }

    @GetMapping("/patient/{patientId}")
    public String getRendezvousByPatientId(@PathVariable Long patientId, Model model) {
        model.addAttribute("rendezvousList", rendezvousService.getRendezvousByPatientId(patientId));
        return "rendezvous/list";
    }

    @GetMapping("/doctor/{doctorId}")
    public String getRendezvousByDoctorId(@PathVariable Long doctorId, Model model) {
        model.addAttribute("rendezvousList", rendezvousService.getRendezvousByDoctorId(doctorId));
        return "rendezvous/list";
    }

    @GetMapping("/search")
    public String searchRendezvous(
            @RequestParam(required = false) Statut statut,
            @RequestParam(required = false) Urgency urgency,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            Model model) {

        List<Rendezvous> results = new ArrayList<>();

        if (statut != null) {
            results = rendezvousService.getRendezvousByStatut(statut);
        } else if (urgency != null) {
            results = rendezvousService.getRendezvousByUrgency(urgency);
        } else if (startDate != null && endDate != null) {
            results = rendezvousService.getRendezvousByDateRange(startDate, endDate);
        }

        model.addAttribute("rendezvousList", results);
        return "rendezvous/list";
    }
}