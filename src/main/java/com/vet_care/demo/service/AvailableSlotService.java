package com.vet_care.demo.service;

import com.vet_care.demo.dto.DoctorDTO;
import com.vet_care.demo.dto.FlatDoctorSlot;
import com.vet_care.demo.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alish
 */

@Service
public class AvailableSlotService {

    private final DoctorRepository doctorRepository;

    public AvailableSlotService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<DoctorDTO> getDoctorsWithAvailableFutureSlots() {
        List<FlatDoctorSlot> flatList = doctorRepository.findAllFutureDoctorSlotsFlat(LocalDateTime.now());
        Map<Long, DoctorDTO> map = new LinkedHashMap<>();
        for (FlatDoctorSlot row : flatList) {
            DoctorDTO doc = map.computeIfAbsent(row.getDoctorId(), id -> {
                DoctorDTO d = new DoctorDTO();
                d.setId(row.getDoctorId());
                d.setLastName(row.getLastName());
                d.setSpecialization(row.getSpecialization());
                return d;
            });
            doc.getAvailableSlots().add(new DoctorDTO.SlotDTO(
                    row.getSlotId(), row.getDateTime(), row.isBooked()
            ));
        }
        return new ArrayList<>(map.values());
    }
}
