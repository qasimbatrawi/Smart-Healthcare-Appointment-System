package com.example.system.controller;

import com.example.system.dto.DoctorDTO;
import com.example.system.entity.Doctor;
import com.example.system.entity.Patient;
import com.example.system.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService ;

    @GetMapping("/all_doctors")
    public List<Doctor> getAllDoctors(){
        return adminService.getAllDoctors() ;
    }

    @GetMapping("/doctor_username/{username}")
    public ResponseEntity<Object> getDoctorByUsername(@PathVariable String username){
        Doctor doctor = adminService.getDoctorByUsername(username);
        return ResponseEntity.ok(doctor) ;
    }

    @GetMapping("/doctor_specialty/{specialtyName}")
    public ResponseEntity<Object> getDoctorBySpecialty(@PathVariable String specialtyName){
        List<Doctor> doctors = adminService.getDoctorBySpecialty(specialtyName);
        return ResponseEntity.ok(doctors) ;
    }

    @PatchMapping("/doctor/{username}")
    public ResponseEntity<Object> updateDoctorByUsername(@PathVariable String username , @RequestBody DoctorDTO newDoctorDetails){
        Doctor doctor = adminService.updateDoctorByUsername(username, newDoctorDetails);
        return ResponseEntity.ok(doctor) ;
    }

    @DeleteMapping("/doctor/{username}")
    public ResponseEntity<Object> deleteDoctor(@PathVariable String username){
        adminService.deleteDoctorByUsername(username) ;
        return ResponseEntity.noContent().build() ;
    }

    @GetMapping("/all_patients")
    public ResponseEntity<Object> getAllPatients(){
        List<Patient> patients = adminService.getAllPatients();
        return ResponseEntity.ok(patients) ;
    }

    @GetMapping("/patient_username/{username}")
    public ResponseEntity<Object> getPatientByUsername(@PathVariable String username){
        Patient patient = adminService.getPatientByUsername(username);
        return ResponseEntity.ok(patient) ;
    }
}
