package com.vet_care.demo.service;

import com.vet_care.demo.model.PetOwner;
import com.vet_care.demo.repository.PetOwnerRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

/**
 * @author Alish
 */
@Service
public class UserService {

    private final PetOwnerRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(PetOwnerRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public PetOwner registerAndLoginUser(PetOwner user, BindingResult result, HttpServletRequest request) throws ServletException {
        if (userRepository.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "error.owner", "Email already in use");
            return null;
        }
        if (result.hasErrors()) {
            return null;
        }
        String rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        PetOwner savedUser = userRepository.save(user);
        request.login(user.getEmail(), rawPassword);
        return savedUser;
    }
}