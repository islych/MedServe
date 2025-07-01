// package com.medical.appointment.appointment.Service;

// import java.util.Date;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.medical.appointment.appointment.Model.Doctor;
// import com.medical.appointment.appointment.Model.Patient;
// import com.medical.appointment.appointment.Model.Rendezvous;
// import com.medical.appointment.appointment.Model.enums.Statut;
// import com.medical.appointment.appointment.Model.enums.Urgency;
// import com.medical.appointment.appointment.Repository.DoctorRepository;
// import com.medical.appointment.appointment.Repository.PatientRepository;
// import com.medical.appointment.appointment.Repository.RendezvousRepository;

// import jakarta.persistence.EntityNotFoundException;

// @Service
// public class RendezvousService {

//     @Autowired
//     private RendezvousRepository rendezvousRepository;

//     @Autowired
//     private DoctorRepository doctorRepository;

//     @Autowired
//     private PatientRepository patientRepository;

    
//     public String addRendezvous(Rendezvous rendezvous, Long doctorId, Long patientId, Date date,
//             String object, String description, Urgency urgency) {
        
        
//         Doctor doctor = doctorRepository.findById(doctorId)
//                 .orElseThrow(() -> new EntityNotFoundException("Docteur non trouvé avec l'ID : " + doctorId));
        
//         Patient patient = patientRepository.findById(patientId)
//                 .orElseThrow(() -> new EntityNotFoundException("Patient non trouvé avec l'ID : " + patientId));

        
//         rendezvous.setDate(date);
//         rendezvous.setObject(object);
//         rendezvous.setDescription(description);
//         rendezvous.setUrgency(urgency);
//         rendezvous.setStatut(Statut.En_Attend);  // Statut par défaut
//         rendezvous.setDoctor(doctor);
//         rendezvous.setPatient(patient);

//         // Enregistrer le rendez-vous
//         rendezvousRepository.save(rendezvous);

//         return "Rendez-vous ajouté avec succès pour le " + date.toString();
//     }

//     // Modifier un rendez-vous existant
//     public String modifyRendezvous(Rendezvous rendezvous, Long id, Date newDate, String object,
//             String description, Urgency urgency, Statut statut) {
        
//         // Trouver le rendez-vous existant
//         Rendezvous existingRendezvous = rendezvousRepository.findById(id)
//                 .orElseThrow(() -> new EntityNotFoundException("Rendez-vous non trouvé avec l'ID : " + id));

//         // Mise à jour des champs
//         existingRendezvous.setDate(newDate != null ? newDate : existingRendezvous.getDate());
//         existingRendezvous.setObject(object != null ? object : existingRendezvous.getObject());
//         existingRendezvous.setDescription(description != null ? description : existingRendezvous.getDescription());
//         existingRendezvous.setUrgency(urgency != null ? urgency : existingRendezvous.getUrgency());
//         existingRendezvous.setStatut(statut != null ? statut : existingRendezvous.getStatut());

//         // Sauvegarder les changements
//         rendezvousRepository.save(existingRendezvous);

//         return "Rendez-vous modifié avec succès";
//     }

//     // Supprimer un rendez-vous
//     public String deleteRendezvous(Long id) {
//         Rendezvous rendezvous = rendezvousRepository.findById(id)
//                 .orElseThrow(() -> new EntityNotFoundException("Rendez-vous non trouvé avec l'ID : " + id));

//         // Supprimer le rendez-vous
//         rendezvousRepository.delete(rendezvous);
//         return "Rendez-vous supprimé avec succès";
//     }

//     // Obtenir tous les rendez-vous
//     public List<Rendezvous> getAllRendezvous() {
//         return rendezvousRepository.findAll();
//     }

//     // Obtenir un rendez-vous par son ID
//     public Rendezvous getRendezvous(Long id) {
//         return rendezvousRepository.findById(id)
//                 .orElseThrow(() -> new EntityNotFoundException("Rendez-vous non trouvé avec l'ID : " + id));
//     }

//     // Obtenir les rendez-vous d'un patient
//     public List<Rendezvous> getRendezvousByPatient(Long patientId) {
//         return rendezvousRepository.findByPatientId(patientId);
//     }

//     // Obtenir les rendez-vous d'un médecin
//     public List<Rendezvous> getRendezvousByDoctor(Long doctorId) {
//         return rendezvousRepository.findByDoctorId(doctorId);
//     }
// }

package com.medical.appointment.appointment.Service;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.medical.appointment.appointment.DTO.RendezvousRequestDto;
// import com.medical.appointment.appointment.DTO.RendezvousUpdateDto;
// import com.medical.appointment.appointment.Model.Doctor;
// import com.medical.appointment.appointment.Model.Patient;
// import com.medical.appointment.appointment.Model.Rendezvous;
// import com.medical.appointment.appointment.Model.enums.Statut;

// import com.medical.appointment.appointment.Repository.DoctorRepository;
// import com.medical.appointment.appointment.Repository.PatientRepository;
// import com.medical.appointment.appointment.Repository.RendezvousRepository;

// import jakarta.persistence.EntityNotFoundException;

// @Service
// public class RendezvousService {

//     @Autowired
//     private RendezvousRepository rendezvousRepository;

//     @Autowired
//     private DoctorRepository doctorRepository;

//     @Autowired
//     private PatientRepository patientRepository;

//     // Nouvelle version de addRendezvous avec username
//     // public Rendezvous addRendezvous(RendezvousRequestDto dto, String username) {
//     //     Patient patient = patientRepository.findByusername(username)
//     //             .orElseThrow(() -> new EntityNotFoundException("Patient non trouvé avec l'username : " + username));

//     //     Doctor doctor = doctorRepository.findById(dto.getDoctorId())
//     //             .orElseThrow(() -> new EntityNotFoundException("Docteur non trouvé avec l'ID : " + dto.getDoctorId()));

//     //     Rendezvous rendezvous = new Rendezvous();
//     //     rendezvous.setDate(dto.getDate());
//     //     rendezvous.setObject(dto.getObject());
//     //     rendezvous.setDescription(dto.getDescription());
//     //     rendezvous.setUrgency(dto.getUrgency());
//     //     rendezvous.setStatut(Statut.En_Attend);
//     //     rendezvous.setPatient(patient);
//     //     rendezvous.setDoctor(doctor);

//     //     return rendezvousRepository.save(rendezvous);
//     // }
//     public Rendezvous addRendezvous(RendezvousRequestDto dto, String username) {
//         System.out.println("Début méthode addRendezvous");
//         System.out.println("DTO reçu : " + dto);
//         System.out.println("Username patient : " + username);
    
//         Patient patient = patientRepository.findByusername(username)
//                 .orElseThrow(() -> new EntityNotFoundException("Patient non trouvé avec l'username : " + username));
//         System.out.println("Patient trouvé : " + patient.getId());
    
//         Doctor doctor = doctorRepository.findById(dto.getDoctorId())
//                 .orElseThrow(() -> new EntityNotFoundException("Docteur non trouvé avec l'ID : " + dto.getDoctorId()));
//         System.out.println("Docteur trouvé : " + doctor.getId());
    
//         Rendezvous rendezvous = new Rendezvous();
//         rendezvous.setDate(dto.getDate());
//         rendezvous.setObject(dto.getObject());
//         rendezvous.setDescription(dto.getDescription());
//         rendezvous.setUrgency(dto.getUrgency());
//         rendezvous.setStatut(Statut.En_Attend);
//         rendezvous.setPatient(patient);
//         rendezvous.setDoctor(doctor);
    
//         return rendezvousRepository.save(rendezvous);
//     }
    

//     // public String modifyRendezvous(Rendezvous rendezvous, Long id, Date newDate, String object,
//     //                                String description, Urgency urgency, Statut statut) {

//     //     Rendezvous existingRendezvous = rendezvousRepository.findById(id)
//     //             .orElseThrow(() -> new EntityNotFoundException("Rendez-vous non trouvé avec l'ID : " + id));

//     //     existingRendezvous.setDate(newDate != null ? newDate : existingRendezvous.getDate());
//     //     existingRendezvous.setObject(object != null ? object : existingRendezvous.getObject());
//     //     existingRendezvous.setDescription(description != null ? description : existingRendezvous.getDescription());
//     //     existingRendezvous.setUrgency(urgency != null ? urgency : existingRendezvous.getUrgency());
//     //     existingRendezvous.setStatut(statut != null ? statut : existingRendezvous.getStatut());

//     //     rendezvousRepository.save(existingRendezvous);

//     //     return "Rendez-vous modifié avec succès";
//     // }

//     public Rendezvous modifyRendezvous(Long id, RendezvousUpdateDto dto) {
//         Rendezvous existing = rendezvousRepository.findById(id)
//                 .orElseThrow(() -> new EntityNotFoundException("Rendez-vous non trouvé avec l'ID : " + id));
    
//         if (dto.getDate() != null) existing.setDate(dto.getDate());
//         if (dto.getObject() != null) existing.setObject(dto.getObject());
//         if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
//         if (dto.getUrgency() != null) existing.setUrgency(dto.getUrgency());
//         if (dto.getStatut() != null) existing.setStatut(dto.getStatut());
    
//         return rendezvousRepository.save(existing);
//     }
    

//     public String deleteRendezvous(Long id) {
//         Rendezvous rendezvous = rendezvousRepository.findById(id)
//                 .orElseThrow(() -> new EntityNotFoundException("Rendez-vous non trouvé avec l'ID : " + id));

//         rendezvousRepository.delete(rendezvous);
//         return "Rendez-vous supprimé avec succès";
//     }

//     public List<Rendezvous> getAllRendezvous() {
//         return rendezvousRepository.findAll();
//     }

//     public Rendezvous getRendezvous(Long id) {
//         return rendezvousRepository.findById(id)
//                 .orElseThrow(() -> new EntityNotFoundException("Rendez-vous non trouvé avec l'ID : " + id));
//     }

//     public List<Rendezvous> getRendezvousByPatient(Long patientId) {
//         return rendezvousRepository.findByPatientId(patientId);
//     }

//     public List<Rendezvous> getRendezvousByDoctor(Long doctorId) {
//         return rendezvousRepository.findByDoctorId(doctorId);
//     }
// }

import org.springframework.stereotype.Service;


import com.medical.appointment.appointment.Model.Rendezvous;
import com.medical.appointment.appointment.Model.enums.Statut;
import com.medical.appointment.appointment.Model.enums.Urgency;
import com.medical.appointment.appointment.Repository.RendezvousRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.Data;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Data
@Service
public class RendezvousService {

    private final RendezvousRepository rendezvousRepository;

    
    public List<Rendezvous> getAllRendezvous() {
        return rendezvousRepository.findAll();
    }

    public Optional<Rendezvous> getRendezvousById(Long id) {
        return rendezvousRepository.findById(id);
    }

    public Rendezvous saveRendezvous(Rendezvous rendezvous) {
        if (rendezvous.getStatut() == null) {
            rendezvous.setStatut(Statut.En_Attend);
        }
        return rendezvousRepository.save(rendezvous);
    }
    // public Rendezvous saveRendezvous(Rendezvous rendezvous, Long doctorId, Long patientId) {
    //     Doctor doctor = doctorRepository.findById(doctorId)
    //             .orElseThrow(() -> new EntityNotFoundException("Docteur non trouvé avec l'ID : " + doctorId));
    //     Patient patient = patientRepository.findById(patientId)
    //             .orElseThrow(() -> new EntityNotFoundException("Patient non trouvé avec l'ID : " + patientId));
    //     rendezvous.setDoctor(doctor);
    //     rendezvous.setPatient(patient);
    //     return rendezvousRepository.save(rendezvous);
    // }

    public void deleteRendezvous(Long id) {
        rendezvousRepository.deleteById(id);
    }

    public Rendezvous updateRendezvousStatut(Long id, Statut statut) {
        Optional<Rendezvous> optionalRendezvous = rendezvousRepository.findById(id);
        if (optionalRendezvous.isPresent()) {
            Rendezvous rendezvous = optionalRendezvous.get();
            rendezvous.setStatut(statut);
            return rendezvousRepository.save(rendezvous);
        }
        return null;
    }

    public List<Rendezvous> getRendezvousByPatientId(Long patientId) {
        return rendezvousRepository.findByPatientId(patientId);
    }

    // public List<Rendezvous> getRendezvousByDoctorId(Long doctorId) {
    //     return rendezvousRepository.findByDoctorId(doctorId);
    // }
    // public List<Rendezvous> getRendezvousByDoctorId(Long doctorId) {
    //     return rendezvousRepository.findByDoctorIdOrderByDateDesc(doctorId);
    // }

    public List<Rendezvous> getRendezvousByDoctorId(Long doctorId) {
        return rendezvousRepository.findByDoctorIdWithPatient(doctorId);
    }

    public List<Rendezvous> getRendezvousByStatut(Statut statut) {
        return rendezvousRepository.findByStatut(statut);
    }

    public List<Rendezvous> getRendezvousByUrgency(Urgency urgency) {
        return rendezvousRepository.findByUrgency(urgency);
    }

    public List<Rendezvous> getRendezvousByDateRange(Date startDate, Date endDate) {
        return rendezvousRepository.findByDateBetween(startDate, endDate);
    }

    public List<Rendezvous> getRendezvousByDoctorIdAndDateRange(Long doctorId, Date startDate, Date endDate) {
        return rendezvousRepository.findByDoctorIdAndDateRange(doctorId, startDate, endDate);
    }
      

    //test 
    public List<Rendezvous> getRendezvousByDoctorIdAndStatus(Long doctorId, Statut status) {
        return rendezvousRepository.findByDoctorIdAndStatut(doctorId, status);
    }
    
    public List<Rendezvous> getRendezvousByPatientIdAndStatut(Long patientId, Statut statut) {
        return rendezvousRepository.findByPatientIdAndStatut(patientId, statut);
    }
    public List<Rendezvous> searchRendezvous(Statut statut, Urgency urgency, Date startDate, Date endDate) {
        if (statut != null) {
            return getRendezvousByStatut(statut);
        } else if (urgency != null) {
            return getRendezvousByUrgency(urgency);
        } else if (startDate != null && endDate != null) {
            return getRendezvousByDateRange(startDate, endDate);
        }
        return Collections.emptyList();
    }
}