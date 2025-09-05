package com.example.system.controller;

import com.example.system.dto.DoctorDTO;
import com.example.system.entity.Doctor;
import com.example.system.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private AdminService adminService ;

    public AdminController (AdminService adminService){
        this.adminService = adminService ;
    }

    @PostMapping("/new_doctor")
    public ResponseEntity<Object> addNewDoctor(@RequestBody DoctorDTO doctor){
        try {
            Doctor newDoctor = adminService.addNewDoctor(doctor);
            return ResponseEntity.ok(newDoctor) ;
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage()) ;
        }
    }

    @GetMapping("/all_doctors")
    public List<Doctor> getAllDoctors(){
        return adminService.getAllDoctors() ;
    }

    @GetMapping("/doctor/{username}")
    public ResponseEntity<Object> getDoctorByUsername(@PathVariable String username){
        try {
            Doctor doctor = adminService.getDoctorByUsername(username);
            return ResponseEntity.ok(doctor) ;
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
}
