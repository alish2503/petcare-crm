package com.vet_care.demo.service;

import com.vet_care.demo.dto.DoctorDto;
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

    public List<DoctorDto> getDoctorsWithAvailableFutureSlots() {
        List<FlatDoctorSlotDto> flatList = doctorRepository.findAllFutureDoctorSlotsFlat(LocalDateTime.now());
        Map<Long, DoctorDto> map = new LinkedHashMap<>();
        for (FlatDoctorSlotDto row : flatList) {
            DoctorDto doc = map.computeIfAbsent(row.doctorId(), id -> {
                DoctorDto d = new DoctorDto();
                d.setId(row.doctorId());
                d.setLastName(row.lastName());
                d.setSpecialization(row.specialization().name());
                return d;
            });
            doc.getAvailableSlots().add(new DoctorDto.SlotDTO(
                    row.slotId(), row.dateTime(), row.booked()
            ));
        }
        return new ArrayList<>(map.values());
    }
}
