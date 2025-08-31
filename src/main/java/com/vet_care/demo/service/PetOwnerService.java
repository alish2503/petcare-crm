package com.vet_care.demo.service;

import com.vet_care.demo.model.PetOwner;
import com.vet_care.demo.model.Role;
import com.vet_care.demo.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

/**
 * @author Alish
 */
@Service
public class PetOwnerService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PetOwnerService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public PetOwner registerAndLoginUser(PetOwner user, BindingResult result, HttpServletRequest request) throws ServletException {
        user.setEmail(user.getEmail().toLowerCase());
        if (userRepository.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "error.owner", "Email already in use");
            return null;
        }
        if (result.hasErrors()) {
            return null;
        }
        String rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_PET_OWNER);
        PetOwner savedUser = userRepository.save(user);
        request.login(user.getEmail(), rawPassword);
        return savedUser;
    }
}