package com.vet_care.demo.config;

import com.vet_care.demo.model.*;
import com.vet_care.demo.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Alish
 */
@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initData(
            PetUserRepository petUserRepository,
            DoctorRepository doctorRepository,
            PetRepository petRepository,
            MedicalRecordRepository medicalRecordRepository,
            AvailableSlotRepository availableSlotRepository,
            AppointmentRepository appointmentRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            // --- USERS ---
            PetUser user1 = new PetUser("John", "Doe", "john@example.com",
                    passwordEncoder.encode("password123"));
            PetUser user2 = new PetUser("Emily", "Clark", "emily@example.com",
                    passwordEncoder.encode("mypassword"));
            petUserRepository.save(user1);
            petUserRepository.save(user2);

            // --- DOCTORS ---
            Doctor d1 = new Doctor("Alice", "Smith", "alice@vet.com", "not_used", "Cardiology");
            Doctor d2 = new Doctor("Bob", "Brown", "bob@vet.com", "not_used", "Dermatology");
            Doctor d3 = new Doctor("Clara", "Johnson", "clara@vet.com", "not_used", "Surgery");
            doctorRepository.save(d1);
            doctorRepository.save(d2);
            doctorRepository.save(d3);

            // --- PETS ---
            Pet p1 = new Pet("Fluffy", "Cat", "Female", LocalDate.of(2020, 5, 12), user1);
            Pet p2 = new Pet("Bella", "Dog", "Female", LocalDate.of(2019, 3, 7), user2);
            Pet p3 = new Pet("Rocky", "Dog", "Male", LocalDate.of(2018, 11, 22), user1);
            Pet p4 = new Pet("Milo", "Bird", "Male", LocalDate.of(2021, 9, 10), user2);
            petRepository.save(p1);
            petRepository.save(p2);
            petRepository.save(p3);
            petRepository.save(p4);

            // --- MEDICAL RECORDS ---
            medicalRecordRepository.save(new MedicalRecord("Cold", "Rest and vitamins",
                    LocalDate.of(2025, 8, 1), p1, d1));
            medicalRecordRepository.save(new MedicalRecord("Skin allergy", "Special shampoo",
                    LocalDate.of(2025, 7, 20), p2, d2));
            medicalRecordRepository.save(new MedicalRecord("Broken wing", "Bandage + antibiotics",
                    LocalDate.of(2025, 8, 10), p4, d3));

            // --- AVAILABLE SLOTS ---
            AvailableSlot s1 = new AvailableSlot(LocalDateTime.of(2025, 8, 20, 10, 0), true, d1);
            AvailableSlot s2 = new AvailableSlot(LocalDateTime.of(2025, 8, 20, 11, 0), false, d1);
            AvailableSlot s3 = new AvailableSlot(LocalDateTime.of(2025, 8, 21, 9, 30), true, d2);
            AvailableSlot s4 = new AvailableSlot(LocalDateTime.of(2025, 8, 22, 15, 0), false, d3);
            availableSlotRepository.save(s1);
            availableSlotRepository.save(s2);
            availableSlotRepository.save(s3);
            availableSlotRepository.save(s4);

            // --- APPOINTMENTS ---
            appointmentRepository.save(new Appointment("Regular checkup", p1, d1, s1));
            appointmentRepository.save(new Appointment("Skin irritation follow-up", p2, d2, s3));
        };
    }
}
