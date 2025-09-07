package com.example.system.auth;

import com.example.system.Enum.RoleName;
import com.example.system.Enum.SpecialtyName;
import com.example.system.config.JwtService;
import com.example.system.dto.DoctorDTO;
import com.example.system.dto.SpecialtyDTO;
import com.example.system.dto.UserDTO;
import com.example.system.entity.*;
import com.example.system.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository ;
    private final PasswordEncoder passwordEncoder ;
    private final RoleRepository roleRepository ;
    private final SpecialtyRepository specialtyRepository ;
    private final DoctorRepository doctorRepository ;
    private final PatientRepository patientRepository ;
    private final JwtService jwtService ;
    private final AuthenticationManager authenticationManager ;

    public Object registerAdmin(UserDTO admin) {

        String name = admin.getName();
        String email = admin.getEmail();
        String username = admin.getUsername();
        String password = admin.getPassword();

        if (name == null || email == null || username == null || password == null){
            throw new RuntimeException("Fields must not be empty") ;
        }

        User user = new User() ;
        user.setName(name);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        Role role  = roleRepository.findByRoleName(RoleName.ADMIN) ;
        user.setRole(role);

        try {
            userRepository.save(user);
        } catch (Exception e){
            throw new RuntimeException("Invalid email format. Email or username are used.") ;
        }

        var jwt = jwtService.generateToken(user) ;
        return AuthenticationResponse.builder()
                .token(jwt)
                .build() ;
    }

    public AuthenticationResponse registerDoctor(DoctorDTO request) {

        String name = request.getName();
        String email = request.getEmail();
        String username = request.getUsername();
        String password = request.getPassword();
        Set<SpecialtyDTO> specialties = request.getSpecialties() ;
        LocalTime workStart = request.getWorkDayStart() ;
        LocalTime workEnd = request.getWorkDayEnd() ;

        if (name == null || email == null || username == null || password == null){
            throw new RuntimeException("Fields must not be empty") ;
        }

        User user = new User() ;
        user.setName(name);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        Role role  = roleRepository.findByRoleName(RoleName.DOCTOR) ;
        user.setRole(role);

        Doctor doctor = new Doctor() ;
        doctor.setDoctorDetails(user);

        if (workStart == null || workEnd == null || specialties == null || specialties.isEmpty()){
            throw new RuntimeException("Fields must not be empty") ;
        }

        if (doctor.getWorkDayStart().isAfter(doctor.getWorkDayEnd())
                || doctor.getWorkDayStart().equals(doctor.getWorkDayEnd())){
            throw new RuntimeException("Invalid work hours.") ;
        }

        Set<Specialty> newSpecialties = specialties.stream()
                .map(s -> s.getSpecialtyName()) // map specialtyDTO to string
                .map(s -> {
                    try {
                        return SpecialtyName.valueOf(s.toUpperCase());
                    } catch (Exception e){
                        throw new RuntimeException("Invalid Specialty.") ;
                    }
                }) // try to map to Enum
                .map(specialty -> specialtyRepository.findBySpecialtyName(specialty)) // map to Specialty object
                .collect(Collectors.toSet());

        doctor.setSpecialty(newSpecialties);
        doctor.setWorkDayEnd(workEnd);
        doctor.setWorkDayStart(workStart);

        try {
            userRepository.save(user);
            doctorRepository.save(doctor) ;
        } catch (Exception e){
            throw new RuntimeException("Invalid email format. Email or username are used.") ;
        }

        var jwt = jwtService.generateToken(user) ;
        return AuthenticationResponse.builder()
                .token(jwt)
                .build() ;
    }

    public Object registerPatient(UserDTO patinet) {

        String name = patinet.getName();
        String email = patinet.getEmail();
        String username = patinet.getUsername();
        String password = patinet.getPassword();

        if (name == null || email == null || username == null || password == null){
            throw new RuntimeException("Fields must not be empty") ;
        }

        User user = new User() ;
        user.setName(name);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        Role role  = roleRepository.findByRoleName(RoleName.PATIENT) ;
        user.setRole(role);

        Patient patient = new Patient() ;
        patient.setPatientDetails(user);

        try {
            userRepository.save(user);
            patientRepository.save(patient) ;
        } catch (Exception e){
            throw new RuntimeException("Invalid email format. Email or username are used.") ;
        }

        var jwt = jwtService.generateToken(user) ;
        return AuthenticationResponse.builder()
                .token(jwt)
                .build() ;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        )) ;
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found.")) ;

        var jwt = jwtService.generateToken(user) ;
        return AuthenticationResponse.builder()
                .token(jwt)
                .build() ;
    }
}
