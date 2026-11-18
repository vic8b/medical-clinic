package com.vic8b.medicalclinic.repository;

import com.vic8b.medicalclinic.model.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Page<Doctor> findByFacilitiesId(Long facilityId, Pageable pageable);
}
