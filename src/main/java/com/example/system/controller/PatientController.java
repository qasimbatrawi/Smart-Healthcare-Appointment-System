package com.example.system.controller;

import com.example.system.entity.Doctor;
import com.example.system.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private PatientService patientService ;

    public PatientController (PatientService patientService){
        this.patientService = patientService ;
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
}
