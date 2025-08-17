package com.vet_care.demo.dto;

import java.time.LocalDate;

/**
 * @author Alish
 */
public interface MedicalRecordsPageProjection {
    Long getId();
    String getTreatment();
    String getDiagnosis();
    LocalDate getDate();
    String getDoctorLastName();
}
