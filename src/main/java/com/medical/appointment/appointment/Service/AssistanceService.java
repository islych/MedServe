package com.medical.appointment.appointment.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.medical.appointment.appointment.Model.Assistance;
import com.medical.appointment.appointment.Model.Doctor;
import com.medical.appointment.appointment.Model.enums.Role;
import com.medical.appointment.appointment.Repository.AssistanceRepository;
import com.medical.appointment.appointment.Repository.DoctorRepository;

import jakarta.persistence.EntityNotFoundException;



@Service
public class AssistanceService {

    @Autowired
    private AssistanceRepository assistanceRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    
    public List<Assistance> getAllAssistances() {
        return assistanceRepository.findAll();
    }

   
    public Assistance getAssistance(long id) {
        return assistanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aucune assistance trouvée avec l'ID : " + id));
    }

    
    public String addAssistance(Assistance assistance, long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Aucun docteur trouvé avec l'ID : " + doctorId));

        assistance.setDoctor(doctor);
        assistance.setRole(Role.ASSISTANCE);
        assistance.setPassword(passwordEncoder.encode(assistance.getPassword()));
        assistanceRepository.save(assistance);

        return "Assistance ajoutée avec succès";
    }

    
    public String deleteAssistance(Long id) {
        String name = assistanceRepository.findById(id).get().getFullName();
        assistanceRepository.deleteById(id);
        return "Assistance " + name + " supprimée avec succès";
    }

    
    public String modifierAssistance(Assistance assistance, long id) {
        Assistance existingAssistance = assistanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aucune assistance trouvée avec l'ID : " + id));

        if (assistance.equals(existingAssistance)) {
            return "Aucune modification à appliquer pour l'assistance " + assistance.getFullName();
        } else {
            
            if (assistance.getEmail() != null && !assistance.getEmail().equals(existingAssistance.getEmail()) && assistanceRepository.findByEmail(assistance.getEmail()).isPresent()) {
                return "L'email existe déjà";
            } else {
                
                if (assistance.getFirstName() != null && !assistance.getFirstName().equals(existingAssistance.getFirstName())) {
                    existingAssistance.setFirstName(assistance.getFirstName());
                }

                if (assistance.getLastName() != null && !assistance.getLastName().equals(existingAssistance.getLastName())) {
                    existingAssistance.setLastName(assistance.getLastName());
                }

                if (assistance.getVille() != null && !assistance.getVille().equals(existingAssistance.getVille())) {
                    existingAssistance.setVille(assistance.getVille());
                }

              
                if (assistance.getPassword() != null && !assistance.getPassword().isEmpty()) {
                    existingAssistance.setPassword(BCrypt.hashpw(assistance.getPassword(), BCrypt.gensalt()));
                }

               
                if (assistance.getDoctor() != null && !assistance.getDoctor().equals(existingAssistance.getDoctor())) {
                    existingAssistance.setDoctor(assistance.getDoctor());
                }

                assistanceRepository.save(existingAssistance);
            }
        }

        return "Assistance " + assistance.getFullName() + " modifiée avec succès";
    }
    public Doctor getDoctorByAssistantEmail(String email) {
        return assistanceRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("Assistante non trouvée"))
            .getDoctor();
    }
    public void updateAssistanceProfile(Assistance updatedAssistance) {
        assistanceRepository.save(updatedAssistance);
    }
}
