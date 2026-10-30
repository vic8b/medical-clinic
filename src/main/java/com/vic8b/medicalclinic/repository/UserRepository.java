package com.vic8b.medicalclinic.repository;

import com.vic8b.medicalclinic.model.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(@NonNull String email);
    boolean existsByUsername(@NonNull String username);
    boolean existsByEmailIgnoreCase(String email);
}
