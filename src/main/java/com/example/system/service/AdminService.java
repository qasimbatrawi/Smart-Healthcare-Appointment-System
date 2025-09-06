package com.example.system.service;

import com.example.system.Enum.RoleName;
import com.example.system.Enum.SpecialtyName;
import com.example.system.dto.DoctorDTO;
import com.example.system.dto.UserDTO;
import com.example.system.entity.*;
import com.example.system.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class AdminService {

    private DoctorRepository doctorRepository ;
    private PatientRepository patientRepository ;
    private RoleRepository roleRepository ;
    private SpecialtyRepository specialtyRepository ;
    private UserRepository userRepository ;

    public AdminService (DoctorRepository doctorRepository ,
                         PatientRepository patientRepository ,
                         RoleRepository roleRepository,
                         SpecialtyRepository specialtyRepository,
                         UserRepository userRepository){
        this.doctorRepository = doctorRepository ;
        this.patientRepository = patientRepository ;
        this.roleRepository = roleRepository ;
        this.specialtyRepository = specialtyRepository ;
        this.userRepository = userRepository ;
    }

    public Doctor addNewDoctor(DoctorDTO doctor){

        if (doctor.getWorkDayStart() == null || doctor.getWorkDayEnd() == null
                || doctor.getName() == null || doctor.getPassword() == null
                || doctor.getEmail() == null || doctor.getUsername() == null
                || doctor.getSpecialty() == null || doctor.getSpecialty().isEmpty()){
            throw new RuntimeException("Fields must not be empty.") ;
        }

        Doctor newDoctor = new Doctor() ;
        User newUser = new User() ;

        // map entered specialties to objects
        Set<Specialty> specialties = doctor.getSpecialty().stream()
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

        Role role = roleRepository.findByRoleName(RoleName.DOCTOR) ;

        newUser.setUsername(doctor.getUsername());
        newUser.setName(doctor.getName());
        newUser.setPassword(doctor.getPassword());
        newUser.setEmail(doctor.getEmail());
        newUser.setRole(role);

        newDoctor.setSpecialty(specialties);
        newDoctor.setDoctorDetails(newUser);
        newDoctor.setWorkDayStart(doctor.getWorkDayStart());
        newDoctor.setWorkDayEnd(doctor.getWorkDayEnd());

        try {
            userRepository.save(newUser) ;
            return doctorRepository.save(newDoctor);
        } catch (Exception e){
            throw new RuntimeException("Invalid email format. Email or username is used.");
        }
    }

    public List<Doctor> getAllDoctors(){
        return doctorRepository.findAll() ;
    }

    public Doctor getDoctorByUsername(String username){
        return doctorRepository.findByDoctorDetails_Username(username)
                .orElseThrow(() -> new RuntimeException("Doctor not found")) ;
    }

    public Doctor updateDoctorByUsername(String username , DoctorDTO newDoctorDetails){

        Doctor doctor = doctorRepository.findByDoctorDetails_Username(username)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        String newUsername = newDoctorDetails.getUsername() ;
        String newEmail = newDoctorDetails.getEmail() ;
        String newName = newDoctorDetails.getName() ;
        String newPassword = newDoctorDetails.getPassword() ;
        LocalTime newWorkDayStart = newDoctorDetails.getWorkDayStart() ;
        LocalTime newWorkDayEnd = newDoctorDetails.getWorkDayEnd() ;

        if (newUsername == null || newEmail == null || newName == null
            || newPassword == null || newWorkDayStart == null || newWorkDayEnd == null){
            throw new RuntimeException("Fields must not be empty.") ;
        }

        // map entered specialties to objects
        Set<Specialty> newSpecialties = newDoctorDetails.getSpecialty().stream()
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
        doctor.getDoctorDetails().setUsername(newUsername) ;
        doctor.getDoctorDetails().setEmail(newEmail) ;
        doctor.getDoctorDetails().setName(newName) ;
        doctor.getDoctorDetails().setPassword(newPassword) ;
        doctor.setWorkDayStart(newWorkDayStart);
        doctor.setWorkDayEnd(newWorkDayEnd);

        try {
            return doctorRepository.save(doctor);
        } catch (Exception e){
            throw new RuntimeException("Invalid email format. Email or username is used.");
        }
    }

    public List<Doctor> getDoctorBySpecialty(String specialtyName){
        try {
            SpecialtyName specialtyEnum = SpecialtyName.valueOf(specialtyName.toUpperCase());
            return doctorRepository.findBySpecialty_SpecialtyName(specialtyEnum) ;
        } catch (Exception e){
            throw new RuntimeException("Invalid Specialty.") ;
        }
    }

    public void deleteDoctorByUsername(String username){
        Doctor doctor = doctorRepository.findByDoctorDetails_Username(username)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        doctorRepository.delete(doctor);
    }

    public Patient addNewPatient(UserDTO patient){

        if (patient.getName() == null || patient.getPassword() == null
            || patient.getEmail() == null || patient.getUsername() == null){
            throw new RuntimeException("Fields must not be empty.") ;
        }

        Patient newPatient = new Patient() ;
        User newUser = new User() ;

        Role role = roleRepository.findByRoleName(RoleName.PATIENT) ;

        newUser.setUsername(patient.getUsername());
        newUser.setName(patient.getName());
        newUser.setPassword(patient.getPassword());
        newUser.setEmail(patient.getEmail());
        newUser.setRole(role);

        newPatient.setPatientDetails(newUser);

        try {
            userRepository.save(newUser) ;
            return patientRepository.save(newPatient) ;
        } catch (Exception e){
            throw new RuntimeException("Invalid email format. Email or username is used.");
        }
    }
}