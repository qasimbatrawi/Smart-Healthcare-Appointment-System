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

    @GetMapping("/doctors")
    public List<Doctor> getAllDoctors(){
        return adminService.getAllDoctors() ;
    }

    @GetMapping("/doctor/username/{username}")
    public ResponseEntity<Object> getDoctorByUsername(@PathVariable String username){
        Doctor doctor = adminService.getDoctorByUsername(username);
        return ResponseEntity.ok(doctor) ;
    }

    @GetMapping("/doctor/specialty/{specialtyName}")
    public ResponseEntity<Object> getDoctorBySpecialty(@PathVariable String specialtyName){
        List<Doctor> doctors = adminService.getDoctorBySpecialty(specialtyName);
        return ResponseEntity.ok(doctors) ;
    }

    @PatchMapping("/doctor/{username}")
    public ResponseEntity<Object> updateDoctorByUsername(@PathVariable String username , @RequestBody DoctorDTO newDoctorDetails){
        Doctor doctor = adminService.updateDoctorByUsername(username, newDoctorDetails);
        return ResponseEntity.ok(doctor) ;
    }

    @PatchMapping("/doctor/freeze/{username}")
    public ResponseEntity<Object> freezeDoctorAccount(@PathVariable String username){
        String result = adminService.freezeDoctorAccount(username);
        return ResponseEntity.ok(result) ;
    }

    @PatchMapping("/doctor/unfreeze/{username}")
    public ResponseEntity<Object> unFreezeDoctorAccount(@PathVariable String username){
        String result = adminService.unFreezeDoctorAccount(username);
        return ResponseEntity.ok(result) ;
    }

    @DeleteMapping("/doctor/{username}")
    public ResponseEntity<Object> deleteDoctor(@PathVariable String username){
        adminService.deleteDoctorByUsername(username) ;
        return ResponseEntity.noContent().build() ;
    }

    @GetMapping("/patients")
    public ResponseEntity<Object> getAllPatients(){
        List<Patient> patients = adminService.getAllPatients();
        return ResponseEntity.ok(patients) ;
    }

    @GetMapping("/patient/username/{username}")
    public ResponseEntity<Object> getPatientByUsername(@PathVariable String username){
        Patient patient = adminService.getPatientByUsername(username);
        return ResponseEntity.ok(patient) ;
    }
}
