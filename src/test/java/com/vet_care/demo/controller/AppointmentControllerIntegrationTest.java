package com.vet_care.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Alish
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AppointmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails("john@example.com")
    void testListAppointments() throws Exception {
        mockMvc.perform(get("/appointments"))
                .andExpect(status().isOk())
                .andExpect(view().name("appointments"))
                .andExpect(model().attributeExists("appointmentsPageProjections"))
                .andExpect(content().string(containsString("appointments")));
    }

    @Test
    @WithUserDetails("john@example.com")
    void testAddAppointment() throws Exception {
        mockMvc.perform(post("/appointments")
                        .param("reason", "Vaccination")
                        .param("petId", "1")
                        .param("doctorId", "1")
                        .param("slotId", "2")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/appointments"));
    }

    @Test
    @WithUserDetails("emily@example.com")
    void testSecondUserCannotBookAlreadyTakenSlot() throws Exception {
        mockMvc.perform(post("/appointments")
                        .param("reason", "Vaccination")
                        .param("petId", "2")
                        .param("doctorId", "1")
                        .param("slotId", "1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(content().string(containsString(
                        "Appointment already exists for this slot and doctor"
                )));
    }

    @Test
    @WithUserDetails("john@example.com")
    void testDeleteAppointment() throws Exception {
        mockMvc.perform(delete("/appointments/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/appointments"));
    }
}