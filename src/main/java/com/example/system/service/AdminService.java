package com.example.system.service;

import com.example.system.Enum.SpecialtyName;
import com.example.system.document.MedicalReport;
import com.example.system.dto.DoctorDTO;
import com.example.system.entity.*;
import com.example.system.exception.AccessDeniedException;
import com.example.system.exception.BadRequestException;
import com.example.system.exception.ResourceNotFoundException;
import com.example.system.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final DoctorRepository doctorRepository ;
    private final PatientRepository patientRepository ;
    private final SpecialtyRepository specialtyRepository ;
    private final PasswordEncoder passwordEncoder ;
    private final AppointmentRepository appointmentRepository ;
    private final MedicalReportRepository medicalReportRepository ;

    public List<Doctor> getAllDoctors(){
        return doctorRepository.findAll()
                .stream()
                .filter(doctor -> doctor.getFreeze() == false)
                .toList();
    }

    public Doctor getDoctorByUsername(String username){
        Doctor doctor = doctorRepository.findByDoctorDetails_Username(username)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found.")) ;

        if (doctor.getFreeze() == true){
            throw new AccessDeniedException("Doctor account is frozen.") ;
        }

        return doctor ;
    }

    public Doctor updateDoctorByUsername(String username , DoctorDTO newDoctorDetails){

        Doctor doctor = doctorRepository.findByDoctorDetails_Username(username)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        if (doctor.getFreeze() == true){
            throw new AccessDeniedException("Doctor account is frozen.") ;
        }

        String newUsername = newDoctorDetails.getUsername() ;
        String newEmail = newDoctorDetails.getEmail() ;
        String newName = newDoctorDetails.getName() ;
        String newPassword = passwordEncoder.encode(newDoctorDetails.getPassword()) ;
        LocalTime newWorkDayStart = newDoctorDetails.getWorkDayStart() ;
        LocalTime newWorkDayEnd = newDoctorDetails.getWorkDayEnd() ;

        if (newUsername == null || newEmail == null || newName == null
            || newPassword == null || newWorkDayStart == null || newWorkDayEnd == null){
            throw new BadRequestException("Fields must not be empty.") ;
        }

        if (newWorkDayStart.isAfter(newWorkDayEnd)
                || newWorkDayStart.equals(newWorkDayEnd)){
            throw new BadRequestException("Invalid work hours.") ;
        }

        // map entered specialties to objects
        Set<Specialty> newSpecialties = newDoctorDetails.getSpecialties().stream()
                .map(s -> s.getSpecialtyName()) // map specialtyDTO to string
                .map(s -> {
                    try {
                        return SpecialtyName.valueOf(s.toUpperCase());
                    } catch (Exception e){
                        throw new BadRequestException("Invalid Specialty.") ;
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
            Doctor newDoctor = doctorRepository.save(doctor);

            List<Appointment> appointments = appointmentRepository
                    .findByDoctor_DoctorDetails_Username(newDoctor.getDoctorDetails().getUsername());

            appointments.stream()
                    .filter(appointment ->
                            (appointment.getStartTime().toLocalTime().isBefore(newDoctor.getWorkDayStart()) ||
                                    appointment.getEndTime().toLocalTime().isAfter(newDoctor.getWorkDayEnd()))
                            && !appointment.getStartTime().toLocalDate().isBefore(LocalDate.now())
                    )
                    .forEach(appointment -> {
                        MedicalReport medicalReport = medicalReportRepository.findByAppointmentId(appointment.getId());
                        if (medicalReport != null) {
                            medicalReportRepository.delete(medicalReport);
                        }
                        appointmentRepository.delete(appointment);
                    });

            return newDoctor;

        } catch (Exception e){
            throw new BadRequestException("Invalid email format. Email or username is used.");
        }
    }

    public List<Doctor> getDoctorBySpecialty(String specialtyName){
        try {
            SpecialtyName specialtyEnum = SpecialtyName.valueOf(specialtyName.toUpperCase());
            return doctorRepository.findBySpecialty_SpecialtyName(specialtyEnum)
                    .stream()
                    .filter(doctor -> doctor.getFreeze() == false)
                    .toList();
        } catch (Exception e){
            throw new BadRequestException("Invalid Specialty.") ;
        }
    }

    public String freezeDoctorAccount(String username){
        Doctor doctor = doctorRepository.findByDoctorDetails_Username(username)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        String result ;
        if (doctor.getFreeze() == true){
            result = "Dr. " + username + " account is already frozen." ;
        }
        else{
            result = "Dr. " + username + " account has been frozen." ;
        }
        doctor.setFreeze(true);

        doctorRepository.save(doctor) ;

        return result ;
    }

    public String unFreezeDoctorAccount(String username) {
        Doctor doctor = doctorRepository.findByDoctorDetails_Username(username)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        String result ;
        if (doctor.getFreeze() == false){
            result = "Dr. " + username + " account is already unfrozen." ;
        }
        else{
            result = "Dr. " + username + " account has been unfrozen." ;
        }
        doctor.setFreeze(false);

        doctorRepository.save(doctor) ;

        return result ;
    }

    public void deleteDoctorByUsername(String username){
        Doctor doctor = doctorRepository.findByDoctorDetails_Username(username)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        List<Appointment> appointments = appointmentRepository.findByDoctor_DoctorDetails_Username(username) ;

        appointments.forEach(appointment -> {
            MedicalReport md = medicalReportRepository.findByAppointmentId(appointment.getId()) ;
            medicalReportRepository.delete(md);
        });

        appointments.forEach(appointmentRepository::delete);

        doctorRepository.delete(doctor);
    }

    public List<Patient> getAllPatients(){
        return patientRepository.findAll() ;
    }

    public Patient getPatientByUsername(String username){
        return patientRepository.findByPatientDetails_Username(username)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found")) ;
    }
}