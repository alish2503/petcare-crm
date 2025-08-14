package com.vet_care.demo.repository;

import com.vet_care.demo.dto.AppointmentsPageProjection;
import com.vet_care.demo.model.Appointment;
import com.vet_care.demo.model.Doctor;
import com.vet_care.demo.model.PetUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Alish
 */
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByDateTimeAndDoctor(LocalDateTime dateTime, Doctor veterinarian);

    List<AppointmentsPageProjection> findByPetOwnerAndSlotDateTimeAfterOrderBySlotDateTimeAsc(
            PetUser owner,
            LocalDateTime dateTime
    );

}
