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
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AppointmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails("john@example.com")
    void addAppointment_asOwnerForOwnPet_shouldRedirect() throws Exception {
        mockMvc.perform(post("/appointments")
                        .param("reason", "Checkup")
                        .param("petId", "1")
                        .param("doctorId", "3")
                        .param("slotId", "2")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/appointments"));
    }

    @Test
    @WithUserDetails("john@example.com")
    void addAppointment_asOwnerForOtherPet_shouldReturnError() throws Exception {
        mockMvc.perform(post("/appointments")
                        .param("reason", "Checkup")
                        .param("petId", "2")
                        .param("doctorId", "3")
                        .param("slotId", "2")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorMessage", containsString(
                        "You are not the owner of this pet")));
    }

    @Test
    @WithUserDetails("alice@vet.com")
    void addAppointment_asDoctor_shouldReturnError() throws Exception {
        mockMvc.perform(post("/appointments")
                        .param("reason", "Checkup")
                        .param("petId", "1")
                        .param("doctorId", "3")
                        .param("slotId", "2")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorMessage", containsString("Access Denied")));
    }

    @Test
    @WithUserDetails("john@example.com")
    void deleteAppointment_asOwnerForOwnAppointment_shouldRedirect() throws Exception {
        mockMvc.perform(delete("/appointments/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/appointments"));
    }

    @Test
    @WithUserDetails("john@example.com")
    void deleteAppointment_asOwnerForOtherAppointment_shouldReturnError() throws Exception {
        mockMvc.perform(delete("/appointments/2")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorMessage", containsString(
                        "You are not allowed to delete this appointment")));
    }

    @Test
    @WithUserDetails("alice@vet.com") // doctor не может удалить
    void deleteAppointment_asDoctor_shouldReturnError() throws Exception {
        mockMvc.perform(delete("/appointments/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorMessage", containsString("Access Denied")));
    }

    @Test
    @WithUserDetails("john@example.com")
    void listAppointments_asOwner_shouldReturnAppointmentsPage() throws Exception {
        mockMvc.perform(get("/appointments"))
                .andExpect(status().isOk())
                .andExpect(view().name("appointments"))
                .andExpect(model().attributeExists("appointmentsPageProjections"));
    }

    @Test
    @WithUserDetails("alice@vet.com")
    void listAppointments_asDoctor_shouldReturnAppointmentsPage() throws Exception {
        mockMvc.perform(get("/appointments"))
                .andExpect(status().isOk())
                .andExpect(view().name("appointments"))
                .andExpect(model().attributeExists("appointmentsPageProjections"));
    }
}
