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
public class DoctorService {

    @Autowired
    private  DoctorRepository doctorRepository;
    @Autowired
    private AssistanceRepository assistanceRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

   
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

   
    // public  Doctor getDoctor(long id) {
    //     return doctorRepository.findById(id)
    //             .orElseThrow(() -> new EntityNotFoundException("Aucun docteur trouvé avec l'ID : " + id));
    // }
  // Méthode corrigée avec gestion d'erreur
        public Doctor getDoctor(Long id) {
            return doctorRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Médecin introuvable avec l'ID : " + id));
        }
   
    public String addDoctor(Doctor doctor) {
        doctor.setRole(Role.DOCTOR);
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        doctorRepository.save(doctor);
        return "Docteur ajouté avec succès";
    }

    
    public String deleteDoctor(Long id) {
        String name = doctorRepository.findById(id).get().getFullName();
        doctorRepository.deleteById(id);
        return "Docteur " + name + " supprimé avec succès";
    }
    public void deleteAssistance(Doctor doctor) {
    if (doctor.getAssistance() == null) {
        throw new IllegalStateException("Aucun assistant associé");
    }
    
    Assistance assistance = doctor.getAssistance();
    assistance.setDoctor(null); // Rompre la relation bidirectionnelle
    doctor.setAssistance(null);
    
    assistanceRepository.delete(assistance);
    doctorRepository.save(doctor);
}

    
    public String modifierDoctor(Doctor doctor, long id) {
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aucun docteur trouvé avec l'ID : " + id));

        if (doctor.equals(existingDoctor)) {
            return "Aucune modification à appliquer pour le docteur " + doctor.getFullName();
        } else {
           
            if (doctor.getEmail() != null && !doctor.getEmail().equals(existingDoctor.getEmail()) && doctorRepository.findByEmail(doctor.getEmail()).isPresent()) {
                return "L'email existe déjà";
            } else {
                
                if (doctor.getFirstName() != null && !doctor.getFirstName().equals(existingDoctor.getFirstName())) {
                    existingDoctor.setFirstName(doctor.getFirstName());
                }

                if (doctor.getLastName() != null && !doctor.getLastName().equals(existingDoctor.getLastName())) {
                    existingDoctor.setLastName(doctor.getLastName());
                }

                if (doctor.getVille() != null && !doctor.getVille().equals(existingDoctor.getVille())) {
                    existingDoctor.setVille(doctor.getVille());
                }

                if (doctor.getSpecialty() != null && !doctor.getSpecialty().equals(existingDoctor.getSpecialty())) {
                    existingDoctor.setSpecialty(doctor.getSpecialty());
                }

                if (doctor.getPassword() != null && !doctor.getPassword().isEmpty()) {
                    existingDoctor.setPassword(BCrypt.hashpw(doctor.getPassword(), BCrypt.gensalt()));
                }

                
                doctorRepository.save(existingDoctor);
            }
        }

        return "Docteur " + doctor.getFullName() + " modifié avec succès";
    }
}

