package com.vet_care.demo.service;

import com.vet_care.demo.dto.DoctorDTO;
import com.vet_care.demo.dto.FlatDoctorSlotDto;
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
public class AvailableDoctorsService {

    private final DoctorRepository doctorRepository;

    public AvailableDoctorsService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<DoctorDTO> getDoctorsWithAvailableFutureSlots() {
        List<FlatDoctorSlotDto> flatList = doctorRepository.findAllFutureDoctorSlotsFlat(LocalDateTime.now());
        Map<Long, DoctorDTO> map = new LinkedHashMap<>();
        for (FlatDoctorSlotDto row : flatList) {
            DoctorDTO doc = map.computeIfAbsent(row.doctorId(), id -> {
                DoctorDTO d = new DoctorDTO();
                d.setId(row.doctorId());
                d.setLastName(row.lastName());
                d.setSpecialization(row.specialization());
                return d;
            });
            doc.getAvailableSlots().add(new DoctorDTO.SlotDTO(
                    row.slotId(), row.dateTime(), row.booked()
            ));
        }
        return new ArrayList<>(map.values());
    }
}
