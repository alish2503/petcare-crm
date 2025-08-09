package com.vet_care.demo.repository;

import com.vet_care.demo.model.Appointment;
import com.vet_care.demo.model.Veterinarian;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

/**
 * @author Alish
 */
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    boolean existsByDateTimeAndVeterinarian(LocalDateTime dateTime, Veterinarian veterinarian);
}
