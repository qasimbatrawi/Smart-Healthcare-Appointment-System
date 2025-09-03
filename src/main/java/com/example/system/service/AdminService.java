package com.example.system.service;

import com.example.system.Enum.RoleName;
import com.example.system.dto.DoctorDTO;
import com.example.system.entity.*;
import com.example.system.repository.*;
import org.springframework.stereotype.Service;

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

        Doctor newDoctor = new Doctor() ;
        User newUser = new User() ;

        // Check if all specialties exist in the db
        Set<Specialty> specialties = doctor.getSpecialty().stream()
                .map(s -> {
                    return specialtyRepository.findBySpecialtyName(s.getSpecialtyName());
                })
                .collect(Collectors.toSet());

        Role role = roleRepository.findByRoleName(RoleName.DOCTOR) ;

        newUser.setUsername(doctor.getUsername());
        newUser.setName(doctor.getName());
        newUser.setPassword(doctor.getPassword());
        newUser.setEmail(doctor.getEmail());
        newUser.setRole(role);

        newDoctor.setSpecialty(specialties);
        newDoctor.setDoctorDetails(newUser);

        try {
            userRepository.save(newUser) ;
            return doctorRepository.save(newDoctor);
        } catch (Exception e){
            throw new RuntimeException("Invalid email format. Email or username is used. Fields must not be empty.");
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

        // Check if all specialties exist
        Set<Specialty> newSpecialties = doctor.getSpecialty().stream()
                .map(s -> {
                    return specialtyRepository.findBySpecialtyName(s.getSpecialtyName());
                })
                .collect(Collectors.toSet());

        doctor.setSpecialty(newSpecialties);
        doctor.getDoctorDetails().setUsername(newUsername) ;
        doctor.getDoctorDetails().setEmail(newEmail) ;
        doctor.getDoctorDetails().setName(newName) ;
        doctor.getDoctorDetails().setPassword(newPassword) ;

        try {
            return doctorRepository.save(doctor);
        } catch (Exception e){
            throw new RuntimeException("Invalid email format. Email or username is used. Fields must not be empty.");
        }
    }

    public void deleteDoctorByUsername(String username){
        Doctor doctor = doctorRepository.findByDoctorDetails_Username(username)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        doctorRepository.delete(doctor);
    }
}