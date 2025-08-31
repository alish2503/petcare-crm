package com.vet_care.demo.repository;

import com.vet_care.demo.model.AbstractUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * @author Alish
 */
public interface UserRepository extends JpaRepository<AbstractUser, Long> {
    boolean existsByEmail(String email);
    Optional<AbstractUser> findByEmail(String email);
}
