package com.example.system.service;

import com.example.system.Enum.RoleName;
import com.example.system.entity.Doctor;
import com.example.system.entity.Role;
import com.example.system.entity.Specialty;
import com.example.system.repository.DoctorRepository;
import com.example.system.repository.PatientRepository;
import com.example.system.repository.RoleRepository;
import com.example.system.repository.SpecialtyRepository;
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

    public AdminService (DoctorRepository doctorRepository ,
                         PatientRepository patientRepository ,
                         RoleRepository roleRepository,
                         SpecialtyRepository specialtyRepository){
        this.doctorRepository = doctorRepository ;
        this.patientRepository = patientRepository ;
        this.roleRepository = roleRepository ;
        this.specialtyRepository = specialtyRepository ;
    }

    public Doctor addNewDoctor(Doctor doctor){

        // Check if the role exists in the db
        Role role = roleRepository.findByRoleName(doctor.getDoctorDetails().getRole().getRoleName())
                .orElseThrow(() -> new RuntimeException("Role not found")); // problem here: exception is not caught

        if (role.getRoleName() != RoleName.DOCTOR){
            throw new RuntimeException("Role must be a Doctor") ;
        }

        doctor.getDoctorDetails().setRole(role);

        // Check if all specialties exist in the db
        Set<Specialty> specialties = doctor.getSpecialty().stream()
                .map(s -> specialtyRepository.findBySpecialtyName(s.getSpecialtyName())
                        .orElseThrow(() -> new NullPointerException("Specialty not found"))) // problem here: exception is not caught
                .collect(Collectors.toSet());

        doctor.setSpecialty(specialties);

        try {
            return doctorRepository.save(doctor);
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

    public Doctor updateDoctorByUsername(String username , Doctor newDoctorDetails){
        Doctor doctor = doctorRepository.findByDoctorDetails_Username(username)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        String newUsername = newDoctorDetails.getDoctorDetails().getUsername() ;
        String newEmail = newDoctorDetails.getDoctorDetails().getEmail() ;
        String newName = newDoctorDetails.getDoctorDetails().getName() ;
        String newPassword = newDoctorDetails.getDoctorDetails().getPassword() ;

        // Check if the role exists
        Role newRole = roleRepository.findByRoleName(newDoctorDetails.getDoctorDetails().getRole().getRoleName())
                .orElseThrow(() -> new NullPointerException("Role not found")); // problem here: exception is not caught

        if (newRole.getRoleName() != RoleName.DOCTOR){
            throw new RuntimeException("Role must be a Doctor") ;
        }

        doctor.getDoctorDetails().setRole(newRole);

        // Check if all specialties exist
        Set<Specialty> newSpecialties = newDoctorDetails.getSpecialty().stream()
                .map(s -> specialtyRepository.findBySpecialtyName(s.getSpecialtyName())
                        .orElseThrow(() -> new NullPointerException("Specialty not found"))) // problem here: exception is not caught
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