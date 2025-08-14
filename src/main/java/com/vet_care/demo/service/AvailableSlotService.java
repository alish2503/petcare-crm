package com.vet_care.demo.service;

import com.vet_care.demo.dto.DoctorsPageProjection;
import com.vet_care.demo.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Alish
 */

@Service
public class AvailableSlotService {

    private final DoctorRepository doctorRepository;

    public AvailableSlotService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<DoctorsPageProjection> getDoctorsWithAvailableFutureSlots() {
        return doctorRepository.findDoctorsWithAvailableFutureSlots(LocalDateTime.now());
    }
}
