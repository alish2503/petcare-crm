package com.vet_care.demo.repository;

import com.vet_care.demo.dto.FlatDoctorSlot;
import com.vet_care.demo.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Alish
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    @Query("""
        SELECT 
            d.id AS doctorId,
            d.lastName AS lastName,
            d.specialization AS specialization,
            s.id AS slotId,
            s.dateTime AS dateTime,
            s.booked AS booked
            FROM Doctor d
            JOIN d.availableSlots s
            WHERE s.dateTime > :now
            ORDER BY d.id, s.dateTime
        """)
    List<FlatDoctorSlot> findAllFutureDoctorSlotsFlat(@Param("now") LocalDateTime now);

}
