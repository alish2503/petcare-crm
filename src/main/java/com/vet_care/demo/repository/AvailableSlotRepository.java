package com.vet_care.demo.repository;

import com.vet_care.demo.model.AvailableSlot;
import com.vet_care.demo.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Alish
 */

@Repository
public interface AvailableSlotRepository extends JpaRepository<AvailableSlot, Long> {

    List<AvailableSlot> findAllBySlotTimeBeforeAndBookedFalse(LocalDateTime dateTime);
    List<AvailableSlot> findByDoctorOrderBySlotTimeAsc(Doctor doctor);

    @Transactional
    @Modifying()
    @Query("UPDATE AvailableSlot s SET s.booked = false WHERE s.id IN " +
            "(SELECT a.slot.id FROM Appointment a WHERE a.pet.id = :petId)")
    void resetSlotsForPet(@Param("petId") Long petId);

    boolean existsByDoctorAndSlotTime(Doctor currentDoctor, LocalDateTime dt);
}
