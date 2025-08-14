package com.vet_care.demo.security;

import com.vet_care.demo.model.PetUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Alish
 */
public class CustomUserDetails implements UserDetails {

    private final PetUser user;

    public CustomUserDetails(PetUser user) {
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

    public PetUser getUser() {
        return user;
    }
}
