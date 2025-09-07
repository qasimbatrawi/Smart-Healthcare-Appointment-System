package com.example.system.auth;

import com.example.system.dto.DoctorDTO;
import com.example.system.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService ;

    @PostMapping("/register_admin")
    public ResponseEntity<Object> registerAdmin(@RequestBody UserDTO admin){
        try {
            return ResponseEntity.ok(authenticationService.registerAdmin(admin));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage()) ;
        }
    }

    @PostMapping("/register_doctor")
    public ResponseEntity<Object> registerDoctor(@RequestBody DoctorDTO doctor){
        try {
            return ResponseEntity.ok(authenticationService.registerDoctor(doctor));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage()) ;
        }
    }

    @PostMapping("/register_patient")
    public ResponseEntity<Object> registerPatient(@RequestBody UserDTO patinet){
        try {
            return ResponseEntity.ok(authenticationService.registerPatient(patinet));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage()) ;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequest request){
        try {
            return ResponseEntity.ok(authenticationService.authenticate(request));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage()) ;
        }
    }
}
