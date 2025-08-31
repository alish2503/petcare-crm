package com.vet_care.demo.controller;

import com.vet_care.demo.model.PetOwner;
import com.vet_care.demo.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Alish
 */
@SpringBootTest
@AutoConfigureMockMvc
class PetsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private PetRepository petRepository;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void clearCaches() {
        cacheManager.getCacheNames().forEach(n -> cacheManager.getCache(n).clear());
        reset(petRepository);
    }

    @Test
    @WithUserDetails("john@example.com")
    void listPets_shouldUseCache() throws Exception {
        mockMvc.perform(get("/pets"))
                .andExpect(status().isOk())
                .andExpect(view().name("pets"))
                .andExpect(model().attributeExists("pets"));

        verify(petRepository, times(1)).findPetByOwner(any(PetOwner.class));

        mockMvc.perform(get("/pets"))
                .andExpect(status().isOk())
                .andExpect(view().name("pets"));

        verify(petRepository, times(1)).findPetByOwner(any(PetOwner.class));
    }

    @Test
    @WithUserDetails("john@example.com")
    void createPet_shouldEvictCache() throws Exception {
        mockMvc.perform(get("/pets"));
        verify(petRepository, times(1)).findPetByOwner(any(PetOwner.class));

        mockMvc.perform(post("/pets")
                        .with(csrf())
                        .param("name", "Snowball")
                        .param("species", "CAT")
                        .param("gender", "MALE")
                        .param("birthDate", "2022-02-02"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pets"));

        mockMvc.perform(get("/pets"))
                .andExpect(status().isOk());

        verify(petRepository, times(2)).findPetByOwner(any(PetOwner.class));
    }

    @Test
    @WithUserDetails("john@example.com")
    void updatePet_shouldEvictCache() throws Exception {
        mockMvc.perform(get("/pets"));
        verify(petRepository, times(1)).findPetByOwner(any(PetOwner.class));

        mockMvc.perform(put("/pets/{id}", 1L)
                        .with(csrf())
                        .param("name", "UpdatedFluffy")
                        .param("species", "CAT")
                        .param("gender", "FEMALE")
                        .param("birthDate", "2020-05-12"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pets"));

        mockMvc.perform(get("/pets"));
        verify(petRepository, times(2)).findPetByOwner(any(PetOwner.class));
    }

    @Test
    @WithUserDetails("john@example.com")
    void deletePet_shouldEvictCache() throws Exception {
        mockMvc.perform(get("/pets"));
        verify(petRepository, times(1)).findPetByOwner(any(PetOwner.class));

        mockMvc.perform(delete("/pets/{id}", 3L).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pets"));

        mockMvc.perform(get("/pets"));
        verify(petRepository, times(2)).findPetByOwner(any(PetOwner.class));
    }
}

