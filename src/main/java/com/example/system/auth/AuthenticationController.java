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

    @PostMapping("/register/admin")
    public ResponseEntity<Object> registerAdmin(@RequestBody UserDTO admin){
        return ResponseEntity.ok(authenticationService.registerAdmin(admin));
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<Object> registerDoctor(@RequestBody DoctorDTO doctor){
        return ResponseEntity.ok(authenticationService.registerDoctor(doctor));
    }

    @PostMapping("/register/patient")
    public ResponseEntity<Object> registerPatient(@RequestBody UserDTO patinet){
        return ResponseEntity.ok(authenticationService.registerPatient(patinet));

    }

    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
