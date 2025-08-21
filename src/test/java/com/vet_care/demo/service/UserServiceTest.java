package com.vet_care.demo.service;

import com.vet_care.demo.model.PetOwner;
import com.vet_care.demo.repository.PetOwnerRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Alish
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private PetOwnerRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private UserService userService;

    private PetOwner testUser;

    @BeforeEach
    void initTestUser() {
        testUser = new PetOwner("John", "Doe", "john@example.com", "password123");
    }

    @Test
    void registerAndLoginUser_ShouldRejectIfEmailExists() throws Exception {
        when(userRepository.existsByEmail(testUser.getEmail())).thenReturn(true);
        PetOwner result = userService.registerAndLoginUser(testUser, bindingResult, request);
        assertNull(result);
        verify(bindingResult).rejectValue(eq("email"), eq("error.owner"), eq("Email already in use"));
        verify(userRepository, never()).save(any());
        verify(request, never()).login(anyString(), anyString());
    }

    @Test
    void registerAndLoginUser_ShouldReturnNullIfBindingResultHasErrors() throws Exception {
        when(userRepository.existsByEmail(testUser.getEmail())).thenReturn(false);
        when(bindingResult.hasErrors()).thenReturn(true);
        PetOwner result = userService.registerAndLoginUser(testUser, bindingResult, request);
        assertNull(result);
        verify(userRepository, never()).save(any());
        verify(request, never()).login(anyString(), anyString());
    }

    @Test
    void registerAndLoginUser_ShouldSaveUserAndLogin_WhenValid() throws Exception {
        when(userRepository.existsByEmail(testUser.getEmail())).thenReturn(false);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        PetOwner savedUser = new PetOwner("John", "Doe", "john@example.com", "encodedPassword");
        when(userRepository.save(any(PetOwner.class))).thenReturn(savedUser);
        PetOwner result = userService.registerAndLoginUser(testUser, bindingResult, request);
        assertNotNull(result);
        assertEquals("encodedPassword", result.getPassword());
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(PetOwner.class));
        verify(request).login("john@example.com", "password123");
    }

    @Test
    void registerAndLoginUser_ShouldThrowServletException_WhenLoginFails() throws Exception {
        when(userRepository.existsByEmail(testUser.getEmail())).thenReturn(false);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(PetOwner.class))).thenAnswer(i -> i.getArgument(0));
        doThrow(new ServletException("Login failed"))
                .when(request).login(anyString(), anyString());

        assertThrows(ServletException.class,
                () -> userService.registerAndLoginUser(testUser, bindingResult, request));

        verify(userRepository).save(any(PetOwner.class));
        verify(request).login("john@example.com", "password123");
    }
}
