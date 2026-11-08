package com.vic8b.medicalclinic.repository;

import com.vic8b.medicalclinic.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Set<Doctor> findByFacilitiesId(Long facilityId);
}
