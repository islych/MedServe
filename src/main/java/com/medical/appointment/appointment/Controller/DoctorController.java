package com.medical.appointment.appointment.Controller;
import java.util.List;
import java.util.stream.Collectors;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Date;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;
import java.util.Objects;
import java.nio.file.Path;

import com.medical.appointment.appointment.DTO.CalendarEventDTO;
import com.medical.appointment.appointment.Model.Doctor;
import com.medical.appointment.appointment.Model.Rendezvous;
import com.medical.appointment.appointment.Model.enums.Specialty;
import com.medical.appointment.appointment.Model.enums.Statut;
import com.medical.appointment.appointment.Repository.DoctorRepository;
import com.medical.appointment.appointment.Service.DoctorService;
import com.medical.appointment.appointment.Service.FileStorageService;
import com.medical.appointment.appointment.Service.RendezvousService;

import io.jsonwebtoken.io.IOException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.util.StringUtils;

import java.util.UUID;
@Slf4j
@Controller
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorRepository doctorRepository;
    private final DoctorService doctorService;
    private final TestController testController;
    private final RendezvousService rendezvousService;
    private final FileStorageService fileStorageService;

    @GetMapping("/appointments")
public String getDoctorAppointments(
    Model model, 
    @AuthenticationPrincipal UserDetails userDetails
) {
    try {
        // 1. Récupérer le médecin connecté
        String email = userDetails.getUsername();
        Doctor doctor = doctorRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("Médecin non trouvé"));

        // 2. Récupérer les rendez-vous du médecin
        List<Rendezvous> appointments = rendezvousService.getRendezvousByDoctorId(doctor.getId());
        
        // 3. Calculer les statistiques (optionnel)
        long rdvEnAttente = appointments.stream()
            .filter(rdv -> rdv.getStatut() == Statut.En_Attend)
            .count();

        // 4. Ajout au modèle
        model.addAttribute("appointments", appointments);
        model.addAttribute("doctor", doctor);
        model.addAttribute("rdvEnAttente", rdvEnAttente);
        
        return "doctor/appointments";

    } catch (EntityNotFoundException e) {
        log.error("Médecin introuvable : {}", e.getMessage());
        return "redirect:/error?code=doctor_not_found";
    } catch (Exception e) {
        log.error("Erreur système : {}", e.getMessage());
        return "redirect:/error?code=500";
    }


}
@GetMapping("/assistance")
public String getAssistanceAccount(
    @AuthenticationPrincipal UserDetails userDetails,
    Model model
) {
    // Vérifier si l'utilisateur est authentifié
    if (userDetails == null) {
        return "redirect:/login"; // Rediriger vers la page de connexion
    }

    Doctor doctor = doctorRepository.findByEmail(userDetails.getUsername())
        .orElseThrow(() -> new RuntimeException("Docteur non trouvé"));
    
    model.addAttribute("assistance", doctor.getAssistance());
    model.addAttribute("doctorId", doctor.getId());
    return "doctor/assistance";
}
@PostMapping("/assistance/delete")
public String deleteAssistance(
    @AuthenticationPrincipal UserDetails userDetails,
    RedirectAttributes redirectAttributes
) {
    try {
        Doctor doctor = doctorRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new EntityNotFoundException("Médecin non trouvé"));
        
        doctorService.deleteAssistance(doctor);
        redirectAttributes.addFlashAttribute("success", "Compte assistant supprimé avec succès");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression : " + e.getMessage());
    }
    return "redirect:/doctor/assistance";
}
    // Afficher tous les médecins
    @GetMapping("/all")
    public String getAllDoctors(Model model) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        return "doctor/list";
    }
    
    // Afficher un médecin par ID
    @GetMapping("/{id}")
    public String getDoctor(@PathVariable Long id, Model model) {
        Doctor doctor = doctorService.getDoctor(id); 
        model.addAttribute("doctor", doctor);
        return "doctor/details";
    }

//     @GetMapping("/{id}")
// public String getDoctor(@PathVariable Long id, Model model) {
//     Doctor doctor = doctorService.getDoctor(id);
    
//     // Ajouter un objet Rendezvous vide pour afficher le formulaire
//     Rendezvous rendezvous = new Rendezvous();
//     rendezvous.setDoctor(doctor);  // Associer le médecin au rendez-vous
//     model.addAttribute("doctor", doctor);
//     model.addAttribute("rendezvous", rendezvous);  // Ajouter le formulaire de rendez-vous
    
//     return "doctor/details";
// }

    
    // Afficher le formulaire d'ajout
    @GetMapping("/add")
    public String showAddDoctorForm(Model model) {
        model.addAttribute("doctor", new Doctor());
        return "doctor/add";
    }
    
    // Traiter l'ajout d'un médecin
    @PostMapping("/add")
    public String addDoctor(@ModelAttribute Doctor doctor, Model model) {
        try {
            String message = doctorService.addDoctor(doctor);
            model.addAttribute("successMessage", message);
            return "redirect:/doctor/all";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("doctor", doctor);
            return "doctor/add";
        }
    }
    
    // Afficher le formulaire de modification
    @GetMapping("/modify/{id}")
    public String showModifyForm(@PathVariable Long id, Model model) {
        Doctor doctor = doctorService.getDoctor(id);
        model.addAttribute("doctor", doctor);
        return "doctor/modify";
    }
    
    // Traiter la modification d'un médecin
    @PostMapping("/modify/{id}")
    public String modifyDoctor(@ModelAttribute Doctor doctor, @PathVariable Long id, Model model) {
        try {
            String message = doctorService.modifierDoctor(doctor, id);
            model.addAttribute("successMessage", message);
            return "redirect:/doctor/all";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("doctor", doctor);
            return "doctor/modify";
        }
    }
    
    // Traiter la suppression d'un médecin
    @GetMapping("/delete/{id}")
    public String deleteDoctor(@PathVariable Long id, Model model) {
        try {
            String message = doctorService.deleteDoctor(id);
            model.addAttribute("successMessage", message);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/doctor/all";
    }
    @GetMapping("/home")
    public String getMethodName(Model model) {
        if(!testController.getConnectedUsername().isEmpty()){
            model.addAttribute("email", testController.getConnectedUsername());
        }
        return "doctor/home";
    }

    // //test calendrier
    //     @GetMapping("/api/appointments")
    // @ResponseBody
    // public List<CalendarEventDTO> getAppointments(@AuthenticationPrincipal UserDetails userDetails) {
    //     Doctor doctor = doctorRepository.findByEmail(userDetails.getUsername())
    //         .orElseThrow(() -> new EntityNotFoundException("Médecin non trouvé"));
        
    //     return rendezvousService.getRendezvousByDoctorId(doctor.getId()).stream()
    //         .map(rdv -> new CalendarEventDTO(
    //             rdv.getId(),
    //             rdv.getObject(),
    //             rdv.getDate(),
    //             rdv.getStatut().name()
    //         ))
    //         .collect(Collectors.toList());
    //}

    // DoctorController.java
    @GetMapping("/api/appointments")
    @ResponseBody
    public List<CalendarEventDTO> getConfirmedAppointments(@AuthenticationPrincipal UserDetails userDetails) {
        Doctor doctor = doctorRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new EntityNotFoundException("Médecin non trouvé"));
        
        return rendezvousService.getRendezvousByDoctorIdAndStatus(doctor.getId(), Statut.Confirme).stream()
            .map(rdv -> new CalendarEventDTO(
                rdv.getId(),
                rdv.getPatient().getFullName(),
                rdv.getDate(), // Now matches Date type
                rdv.getStatut().name() // Correct field: status instead of urgency
            ))
            .collect(Collectors.toList());
    }
    


    @GetMapping("/profile")
public String doctorProfile(
    @AuthenticationPrincipal UserDetails userDetails,
    Model model
) {
    try {
        Doctor doctor = doctorRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new EntityNotFoundException("Médecin non trouvé"));
        
        model.addAttribute("doctor", doctor);
        return "doctor/profile";
        
    } catch (EntityNotFoundException e) {
        log.error("Profil médecin introuvable : {}", e.getMessage());
        return "redirect:/error?code=doctor_not_found";
    }
}

@GetMapping("/profile/edit")
public String showEditProfileForm(
    @AuthenticationPrincipal UserDetails userDetails,
    Model model
) {
    Doctor doctor = doctorRepository.findByEmail(userDetails.getUsername())
        .orElseThrow(() -> new EntityNotFoundException("Médecin non trouvé"));
    
    model.addAttribute("doctor", doctor);
    model.addAttribute("specialties", Specialty.values());
         model.addAttribute("villes", getMarocCities());
    return "doctor/edit-profile";
}

@PostMapping("/profile/update")
public String updateProfile(
    @ModelAttribute Doctor updatedDoctor,
    @RequestParam(value = "photo", required = false) MultipartFile newPhoto,
    @RequestParam("currentPhoto") String currentPhotoUrl,
    @AuthenticationPrincipal UserDetails userDetails,
    RedirectAttributes redirectAttributes) {

    try {
        Doctor existingDoctor = doctorRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new EntityNotFoundException("Médecin non trouvé"));

        // Mise à jour des champs de base
        existingDoctor.setFirstName(updatedDoctor.getFirstName());
        existingDoctor.setLastName(updatedDoctor.getLastName());
        existingDoctor.setAdresse(updatedDoctor.getAdresse());
        existingDoctor.setVille(updatedDoctor.getVille());
        existingDoctor.setSpecialty(updatedDoctor.getSpecialty());

        // Gestion de la photo
        if (newPhoto != null && !newPhoto.isEmpty() && !newPhoto.getOriginalFilename().isEmpty()) {
            // Génération d'un nom de fichier sécurisé
            String sanitizedFileName = StringUtils.cleanPath(Objects.requireNonNull(newPhoto.getOriginalFilename()));
            String newFileName = UUID.randomUUID() + "_" + sanitizedFileName;
            
            // Stockage du fichier
            Path uploadPath = Paths.get("uploads").toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);
            Path targetLocation = uploadPath.resolve(newFileName);
            Files.copy(newPhoto.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            // Encodage URL du nom de fichier
            String encodedFileName = UriUtils.encode(newFileName, StandardCharsets.UTF_8);
            existingDoctor.setPhotoUrl("/uploads/" + encodedFileName);

            // Suppression de l'ancienne photo
            if (StringUtils.hasText(currentPhotoUrl) && !currentPhotoUrl.startsWith("/images/")) {
                String oldFileName = currentPhotoUrl.replace("/uploads/", "");
                Path oldFilePath = uploadPath.resolve(UriUtils.decode(oldFileName, "UTF-8"));
                if (Files.exists(oldFilePath)) {
                    Files.delete(oldFilePath);
                }
            }
        }

        doctorRepository.save(existingDoctor);
        redirectAttributes.addFlashAttribute("success", "Profil mis à jour avec succès !");

    } catch (IOException e) {
        log.error("Erreur fichier : {}", e.getMessage());
        redirectAttributes.addFlashAttribute("error", "Erreur lors du traitement de la photo");
    } catch (Exception e) {
        log.error("Erreur générale : {}", e.getMessage());
        redirectAttributes.addFlashAttribute("error", "Erreur lors de la mise à jour");
    }

    return "redirect:/doctor/profile";
}
@GetMapping("/{id}/profile")
public String viewDoctorProfile(@PathVariable Long id, Model model) {
    Doctor doctor = doctorService.getDoctor(id);
    model.addAttribute("doctor", doctor);
    return "doctor/public-profile";
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