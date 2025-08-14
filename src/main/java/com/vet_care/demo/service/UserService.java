package com.vet_care.demo.service;

import com.vet_care.demo.model.PetUser;
import com.vet_care.demo.repository.PetUserRepository;
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

    private final PetUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(PetUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public PetUser registerAndLoginUser(PetUser user, BindingResult result, HttpServletRequest request) throws ServletException {

        if (emailExists(user.getEmail())) {
            result.rejectValue("email", "error.owner", "Email already in use");
            return null;
        }

        if (result.hasErrors()) {
            return null;
        }

        String rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        PetUser savedUser = userRepository.save(user);

        request.login(user.getEmail(), rawPassword);

        return savedUser;
    }


    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}