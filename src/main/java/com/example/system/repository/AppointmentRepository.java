package com.example.system.repository;

import com.example.system.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctor_DoctorDetails_Username(String username) ;
    List<Appointment> findByPatient_PatientDetails_Username(String username) ;
}
