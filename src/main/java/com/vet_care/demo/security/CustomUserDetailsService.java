package com.vet_care.demo.security;

import com.vet_care.demo.model.PetOwner;
import com.vet_care.demo.repository.PetOwnerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Alish
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PetOwnerRepository petUserRepository;

    public CustomUserDetailsService(PetOwnerRepository petUserRepository) {
        this.petUserRepository = petUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        PetOwner user = petUserRepository.findByEmailWithPets(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(user);
    }
}
