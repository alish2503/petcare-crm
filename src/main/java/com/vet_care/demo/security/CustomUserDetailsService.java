package com.vet_care.demo.security;

import com.vet_care.demo.model.PetUser;
import com.vet_care.demo.repository.PetUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Alish
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PetUserRepository petUserRepository;

    public CustomUserDetailsService(PetUserRepository petUserRepository) {
        this.petUserRepository = petUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        PetUser user = petUserRepository.findByEmailWithPets(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(user);
    }
}
