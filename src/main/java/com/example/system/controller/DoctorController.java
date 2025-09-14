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

    @GetMapping("/today-appointment")
    public ResponseEntity<Object> getTodayAppointment(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Appointment> appointments = doctorService.getTodayAppointment(username);
        return ResponseEntity.ok().body(appointments) ;
    }

    @PatchMapping("/completed-appointment/{appointmentId}")
    public ResponseEntity<Object> markAppointmentCompleted(@PathVariable Long appointmentId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Appointment appointment = doctorService.markAppointmentCompleted(username, appointmentId);
        return ResponseEntity.ok().body(appointment) ;
    }

    @PostMapping("/add-result/{appointmentId}")
    public ResponseEntity<Object> addLabResult(@PathVariable Long appointmentId, @RequestBody LabResult labResult){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        MedicalReport medicalReport = doctorService.addLabResult(username, appointmentId, labResult);
        return ResponseEntity.ok().body(medicalReport) ;
    }

    @PostMapping("/new-prescription/{appointmentId}")
    public ResponseEntity<Object> newPrescription(@PathVariable Long appointmentId, @RequestBody Prescription prescription){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        MedicalReport medicalReport = doctorService.newPrescription(username, appointmentId, prescription);
        return ResponseEntity.ok().body(medicalReport) ;
    }
}
