package com.example.system.controller;

import com.example.system.document.LabResult;
import com.example.system.document.Prescription;
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

    @PostMapping("/book-appointment")
    public ResponseEntity<Object> bookAppointment(@RequestBody AppointmentDTO appointment){
        String username = SecurityContextHolder.getContext().getAuthentication().getName() ;
        Appointment apt = patientService.bookAppointment(username, appointment);
        return ResponseEntity.ok(apt) ;
    }

    @GetMapping("/doctor-time/{username}")
    public ResponseEntity<Object> getAvailableTimeForDoctorByUsernameThisWeek(@PathVariable String username){
        Map<LocalDate, List<Map<LocalTime, LocalTime>>> doctor = patientService.getAvailableTimeForDoctorByUsernameThisWeek(username);
        return ResponseEntity.ok(doctor) ;
    }

    @GetMapping("/doctor/specialty/{specialtyName}")
    public ResponseEntity<Object> getDoctorBySpecialty(@PathVariable String specialtyName){
        List<Doctor> doctors = patientService.getDoctorBySpecialty(specialtyName);
        return ResponseEntity.ok(doctors) ;
    }

    @GetMapping("/prescription/{appointmentId}")
    public ResponseEntity<Object> getPrescription(@PathVariable Long appointmentId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName() ;
        Prescription prescription = patientService.getPrescription(username, appointmentId);
        return ResponseEntity.ok(prescription) ;
    }

    @GetMapping("/lab-results/{appointmentId}")
    public ResponseEntity<Object> getLabResults(@PathVariable Long appointmentId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName() ;
        List<LabResult> labResults = patientService.getLabResults(username, appointmentId);
        return ResponseEntity.ok(labResults) ;
    }

    @PatchMapping
    public ResponseEntity<Object> updatePatient(@RequestBody UserDTO newPatientDetails){
        String username = SecurityContextHolder.getContext().getAuthentication().getName() ;
        Patient patient = patientService.updatePatient(username, newPatientDetails);
        return ResponseEntity.ok(patient) ;
    }

    @DeleteMapping("/appointment/{appointmentId}")
    public ResponseEntity<Object> cancelAppointment(@PathVariable Long appointmentId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName() ;
        patientService.cancelAppointment(username, appointmentId);
        return ResponseEntity.noContent().build() ;
    }
}
