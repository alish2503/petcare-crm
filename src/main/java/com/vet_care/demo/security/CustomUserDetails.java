package com.vet_care.demo.security;

import com.vet_care.demo.model.PetOwner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Alish
 */
public class CustomUserDetails implements UserDetails {

    private final PetOwner user;

    public CustomUserDetails(PetOwner user) {
        this.user = user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    public PetOwner getUser() {
        return user;
    }
}
