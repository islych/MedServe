package com.medical.appointment.appointment.Service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.medical.appointment.appointment.Model.Patient;
import com.medical.appointment.appointment.Model.enums.Role;
import com.medical.appointment.appointment.Repository.PatientRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    
    public List<Patient> getAllPatients(){
        return patientRepository.findAll();
    }

    public Patient getPatient(long id){
        return patientRepository.findById(id).get();
    }
    
    public String addPatient(Patient patient) {
        patient.setRole(Role.PATIENT);
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        patientRepository.save(patient);
        return "Patient ajouter avec Succes";
    }

    public String deletePatient(Long id){
        String name = patientRepository.findById(id).get().getFullName();
        patientRepository.deleteById(id);
        return "Patient " + name + " Supprimer avec Succes";
    }

    public String modifierPatient(Patient patient , long id){
        Patient exisPatient = patientRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Aucun Patient trouvé avec l'ID : " + id));
            if(patient == exisPatient ){
                return "Aucun modification a appliqué pour Patient " + patient.getFullName();
            }else{
                    if(patient.getEmail() != null && (patient.getEmail() != exisPatient.getEmail()) && patientRepository.findByEmail(patient.getEmail()) != null){
                        return "L'email existe déjà";
                    }else{
                        exisPatient.setAdresse(patient.getAdresse());
                        exisPatient.setAge(patient.getAge());
                        exisPatient.setDate_naissance(patient.getDate_naissance());


                        if (patient.getMedicaList() != null && !patient.getMedicaList().isEmpty()) {
                            exisPatient.setMedicaList(patient.getMedicaList());
                        }
                        
                        if (patient.getRendezvousList() != null && !patient.getRendezvousList().isEmpty()) {
                            exisPatient.setRendezvousList(patient.getRendezvousList());
                        }

                        if (patient.getFirstName() != null && !patient.getFirstName().equals(exisPatient.getFirstName())) {
                            exisPatient.setFirstName(patient.getFirstName());
                        }

                        if (patient.getLastName() != null && !patient.getLastName().equals(exisPatient.getLastName())) {
                            exisPatient.setLastName(patient.getLastName());
                        }

                        if (patient.getVille() != null && !patient.getVille().equals(exisPatient.getVille())) {
                            exisPatient.setVille(patient.getVille());
                        }
                        if (patient.getPassword() != null && !patient.getPassword().isEmpty()) {
                            exisPatient.setPassword(BCrypt.hashpw(patient.getPassword(), BCrypt.gensalt()));
                            
                        }
                    }
                    patientRepository.save(exisPatient);
            }
        return "Patient " + patient.getFullName() + "modifier aveec Succes";
    }


}
