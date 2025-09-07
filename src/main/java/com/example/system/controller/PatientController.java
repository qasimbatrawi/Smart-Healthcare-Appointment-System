package com.example.system.controller;

import com.example.system.dto.AppointmentDTO;
import com.example.system.dto.UserDTO;
import com.example.system.entity.Appointment;
import com.example.system.entity.Doctor;
import com.example.system.entity.Patient;
import com.example.system.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService ;

    @PostMapping("/book_appointment")
    public ResponseEntity<Object> bookAppointment(@RequestBody AppointmentDTO appointment){
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName() ;
            Appointment apt = patientService.bookAppointment(username, appointment);
            return ResponseEntity.ok(apt) ;
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage()) ;
        }
    }

    @GetMapping("/doctor_time/{username}")
    public ResponseEntity<Object> getAvailableTimeForDoctorByUsernameThisWeek(@PathVariable String username){
        try {
            Map<LocalDate, List<Map<LocalTime, LocalTime>>> doctor = patientService.getAvailableTimeForDoctorByUsernameThisWeek(username);
            return ResponseEntity.ok(doctor) ;
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage()) ;
        }
    }

    @GetMapping("/doctor_specialty/{specialtyName}")
    public ResponseEntity<Object> getDoctorBySpecialty(@PathVariable String specialtyName){
        try {
            List<Doctor> doctors = patientService.getDoctorBySpecialty(specialtyName);
            return ResponseEntity.ok(doctors) ;
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage()) ;
        }
    }

    @PatchMapping
    public ResponseEntity<Object> updatePatient(@RequestBody UserDTO newPatientDetails){
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName() ;
            Patient patient = patientService.updatePatient(username, newPatientDetails);
            return ResponseEntity.ok(patient) ;
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage()) ;
        }
    }
}
