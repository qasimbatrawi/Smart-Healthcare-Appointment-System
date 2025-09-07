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
        try {
            Doctor doctor = adminService.getDoctorByUsername(username);
            return ResponseEntity.ok(doctor) ;
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage()) ;
        }
    }

    @GetMapping("/doctor_specialty/{specialtyName}")
    public ResponseEntity<Object> getDoctorBySpecialty(@PathVariable String specialtyName){
        try {
            List<Doctor> doctors = adminService.getDoctorBySpecialty(specialtyName);
            return ResponseEntity.ok(doctors) ;
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage()) ;
        }
    }

    @PatchMapping("/doctor/{username}")
    public ResponseEntity<Object> updateDoctorByUsername(@PathVariable String username , @RequestBody DoctorDTO newDoctorDetails){
        try {
            Doctor doctor = adminService.updateDoctorByUsername(username, newDoctorDetails);
            return ResponseEntity.ok(doctor) ;
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage()) ;
        }
    }

    @DeleteMapping("/doctor/{username}")
    public ResponseEntity<Object> deleteDoctor(@PathVariable String username){
        try{
            adminService.deleteDoctorByUsername(username) ;
            return ResponseEntity.noContent().build() ;
        } catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage()) ;
        }
    }

    @GetMapping("/all_patients")
    public ResponseEntity<Object> getAllPatients(){
        try {
            List<Patient> patients = adminService.getAllPatients();
            return ResponseEntity.ok(patients) ;
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage()) ;
        }
    }

    @GetMapping("/patient_username/{username}")
    public ResponseEntity<Object> getPatientByUsername(@PathVariable String username){
        try {
            Patient patient = adminService.getPatientByUsername(username);
            return ResponseEntity.ok(patient) ;
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage()) ;
        }
    }
}
