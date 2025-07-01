package com.medical.appointment.appointment.Model.enums;

public enum Specialty {
    ANESTHESIOLOGIST("Anesthesiologist"),
    CARDIOLOGIST("Cardiologist"),
    DENTIST("Dentist"),
    DERMATOLOGIST("Dermatologist"),
    ENT_SPECIALIST("ENT Specialist"),
    GENERAL_PRACTITIONER("General Practitioner"),
    GYNECOLOGIST("Gynecologist"),
    NEUROLOGIST("Neurologist"),
    ONCOLOGIST("Oncologist"),
    OPHTHALMOLOGIST("Ophthalmologist"),
    ORTHOPEDIST("Orthopedist"),
    PEDIATRICIAN("Pediatrician"),
    PSYCHIATRIST("Psychiatrist"),
    RADIOLOGIST("Radiologist"),
    UROLOGIST("Urologist");

    private final String displayName;

    Specialty(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
