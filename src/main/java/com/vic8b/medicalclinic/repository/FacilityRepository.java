package com.vic8b.medicalclinic.repository;

import com.vic8b.medicalclinic.model.Facility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
    boolean existsByName(String name);
    Page<Facility> findByDoctorsId(Long doctorId, Pageable pageable);
}
