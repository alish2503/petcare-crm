package com.vet_care.demo.controller;

import com.vet_care.demo.model.PetOwner;
import com.vet_care.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Alish
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository ownerRepository;

    @Test
    void registerUser_withValidData_shouldRedirectToDashboard_andLoginUser() throws Exception {
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "Tomas")
                        .param("lastName", "Smith")
                        .param("email", "tomas@example.com")
                        .param("password", "password123")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"))
                .andExpect(authenticated().withUsername("tomas@example.com"));

        PetOwner saved = (PetOwner)ownerRepository.findByEmail("tomas@example.com").orElseThrow();
        assertEquals("Tomas", saved.getFirstName());
        assertEquals("Smith", saved.getLastName());
    }

    @Test
    void registerUser_withExistingEmail_shouldReturnFormAndErrors() throws Exception {
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "Bob")
                        .param("lastName", "Brown")
                        .param("email", "john@example.com")
                        .param("password", "newpass")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("register-form"))
                .andExpect(model().attributeHasFieldErrors("owner", "email"))
                .andExpect(unauthenticated());

    }

    @Test
    void registerUser_withInvalidData_shouldReturnFormAndErrors() throws Exception {

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "")
                        .param("lastName", "Smith")
                        .param("email", "new@example.com")
                        .param("password", "password123")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("register-form"))
                .andExpect(model().attributeHasFieldErrors("owner", "firstName"))
                .andExpect(unauthenticated());
    }
}
