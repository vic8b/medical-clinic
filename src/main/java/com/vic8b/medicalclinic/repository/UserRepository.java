package com.vic8b.medicalclinic.repository;

import com.vic8b.medicalclinic.model.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(@NonNull String username);
}
