package com.example.system.service;

import com.example.system.Enum.SpecialtyName;
import com.example.system.dto.DoctorDTO;
import com.example.system.entity.*;
import com.example.system.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private DoctorRepository doctorRepository ;
    private PatientRepository patientRepository ;
    private SpecialtyRepository specialtyRepository ;

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

        if (newWorkDayStart.isAfter(newWorkDayEnd)
                || newWorkDayStart.equals(newWorkDayEnd)){
            throw new RuntimeException("Invalid work hours.") ;
        }

        // map entered specialties to objects
        Set<Specialty> newSpecialties = newDoctorDetails.getSpecialties().stream()
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

    public List<Patient> getAllPatients(){
        return patientRepository.findAll() ;
    }

    public Patient getPatientByUsername(String username){
        return patientRepository.findByPatientDetails_Username(username)
                .orElseThrow(() -> new RuntimeException("Patient not found")) ;
    }
}