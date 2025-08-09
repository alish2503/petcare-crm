package com.vet_care.demo.repository;

import com.vet_care.demo.model.AvailableSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Alish
 */

@Repository
public interface AvailableSlotRepository extends JpaRepository<AvailableSlot, Long> {
    List<AvailableSlot> findAllByDateTimeBeforeAndBookedFalse(LocalDateTime dateTime);
}
