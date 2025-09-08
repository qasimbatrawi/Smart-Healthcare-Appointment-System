package com.example.system.controller;

import com.example.system.document.LabResult;
import com.example.system.document.MedicalReport;
import com.example.system.document.Prescription;
import com.example.system.entity.Appointment;
import com.example.system.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService ;

    @GetMapping("/today_appointment")
    public ResponseEntity<Object> getTodayAppointment(){
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            List<Appointment> appointments = doctorService.getTodayAppointment(username);
            return ResponseEntity.ok().body(appointments) ;
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage()) ;
        }
    }

    @PatchMapping("/completed_appointment/{appointmentId}")
    public ResponseEntity<Object> markAppointmentCompletedAndAddPrescription(@PathVariable Long appointmentId){
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Appointment appointment = doctorService.markAppointmentCompleted(username, appointmentId);
            return ResponseEntity.ok().body(appointment) ;
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage()) ;
        }
    }

    @PostMapping("/add_result/{appointmentId}")
    public ResponseEntity<Object> addLabResult(@PathVariable Long appointmentId, @RequestBody LabResult labResult){
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            MedicalReport medicalReport = doctorService.addLabResult(username, appointmentId, labResult);
            return ResponseEntity.ok().body(medicalReport) ;
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage()) ;
        }
    }

    @PostMapping("/new_prescription/{appointmentId}")
    public ResponseEntity<Object> newPrescription(@PathVariable Long appointmentId, @RequestBody Prescription prescription){
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            MedicalReport medicalReport = doctorService.newPrescription(username, appointmentId, prescription);
            return ResponseEntity.ok().body(medicalReport) ;
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage()) ;
        }
    }
}
