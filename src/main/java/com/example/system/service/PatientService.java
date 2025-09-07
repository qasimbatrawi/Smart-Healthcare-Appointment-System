package com.example.system.service;

import com.example.system.Enum.SpecialtyName;
import com.example.system.dto.AppointmentDTO;
import com.example.system.dto.UserDTO;
import com.example.system.entity.Appointment;
import com.example.system.entity.Doctor;
import com.example.system.entity.Patient;
import com.example.system.repository.AppointmentRepository;
import com.example.system.repository.DoctorRepository;
import com.example.system.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PatientService {

    private DoctorRepository doctorRepository ;
    private AppointmentRepository appointmentRepository ;
    private PatientRepository patientRepository ;

    public Appointment bookAppointment(AppointmentDTO appointment){

        String doctorUsername = appointment.getDoctorUsername() ;
        String patientUsername = appointment.getPatientUsername() ; // ***************** SHOULD GET THE USERNAME AUTOMATICALLY
        LocalDate date = appointment.getDate() ;
        LocalTime start = appointment.getStartTime() ;
        LocalTime end = appointment.getEndTime() ;

        if (doctorUsername == null || patientUsername == null || date == null
            || start == null || end == null){
            throw new RuntimeException("Fields must not be empty.") ;
        }

        Doctor doctor = doctorRepository.findByDoctorDetails_Username(doctorUsername)
                .orElseThrow(() -> new RuntimeException("Doctor not found.")) ;

        Patient patient = patientRepository.findByPatientDetails_Username(patientUsername)
                .orElseThrow(() -> new RuntimeException("Patient not found.")) ;

        if (date.isBefore(LocalDate.now().plusDays(1))){
            throw new RuntimeException("Invalid date.") ;
        }

        LocalDateTime startTime = LocalDateTime.of(date , start) ;
        LocalDateTime endTime = LocalDateTime.of(date , end) ;

        List<Appointment> doctorAppointments = appointmentRepository
                .findByDoctor_DoctorDetails_Username(doctorUsername) ;
        if (!doctor.isAvailable(startTime, endTime, doctorAppointments)) {
            throw new RuntimeException("Doctor is not available at this time.");
        }

        List<Appointment> patientAppointments = appointmentRepository
                .findByPatient_PatientDetails_Username(patientUsername) ;
        if (!doctor.isAvailable(startTime, endTime, patientAppointments)) {
            throw new RuntimeException("You have another book at this time.");
        }

        Appointment newAppointment = new Appointment() ;

        newAppointment.setPatient(patient);
        newAppointment.setDoctor(doctor);
        newAppointment.setStartTime(startTime);
        newAppointment.setEndTime(endTime);
        newAppointment.setCompleted(false);

        return appointmentRepository.save(newAppointment) ;
    }

    public Map<LocalDate, List<Map<LocalTime, LocalTime>>> getAvailableTimeForDoctorByUsernameThisWeek(String username){
        Doctor doctor = doctorRepository.findByDoctorDetails_Username(username)
                .orElseThrow(() -> new RuntimeException("Doctor not found")) ;

        LocalDate today = LocalDate.now() ;
        LocalDate weekend = today.plusDays(6) ;
        List<Appointment> appointments = appointmentRepository.findByDoctor_DoctorDetails_Username(username) ;

        Map<LocalDate, List<Map<LocalTime, LocalTime>>> available = new HashMap<>() ;
        while (!today.isAfter(weekend)) {

            LocalTime start = doctor.getWorkDayStart() ;
            LocalTime workEnd = doctor.getWorkDayEnd() ;

            while (start.isBefore(workEnd)) {

                LocalDateTime from = LocalDateTime.of(today, start);
                LocalDateTime to = from;

                while (to.isBefore(LocalDateTime.of(today, workEnd))
                        && doctor.isAvailable(from, to.plusMinutes(5), appointments)){
                    to = to.plusMinutes(5);
                }

                if (from.isBefore(to)){
                    Map<LocalTime, LocalTime> interval = new HashMap<>() ;
                    interval.put(from.toLocalTime() , to.toLocalTime()) ;

                    if (!available.containsKey(today)){
                        available.put(today , new ArrayList<>()) ;
                    }
                    available.get(today).add(interval) ;
                }
                start = to.toLocalTime();
            }

            today = today.plusDays(1);
        }

        return available ;
    }

    public List<Doctor> getDoctorBySpecialty(String specialtyName){
        try {
            SpecialtyName specialtyEnum = SpecialtyName.valueOf(specialtyName.toUpperCase());
            return doctorRepository.findBySpecialty_SpecialtyName(specialtyEnum) ;
        } catch (Exception e){
            throw new RuntimeException("Invalid Specialty.") ;
        }
    }

    public Patient updatePatientByUsername(String username , UserDTO newPatientDetails){

        Patient patient = patientRepository.findByPatientDetails_Username(username)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        String newUsername = newPatientDetails.getUsername() ;
        String newEmail = newPatientDetails.getEmail() ;
        String newName = newPatientDetails.getName() ;
        String newPassword = newPatientDetails.getPassword() ;

        if (newUsername == null || newEmail == null || newName == null
                || newPassword == null){
            throw new RuntimeException("Fields must not be empty.") ;
        }

        patient.getPatientDetails().setUsername(newUsername) ;
        patient.getPatientDetails().setEmail(newEmail) ;
        patient.getPatientDetails().setName(newName) ;
        patient.getPatientDetails().setPassword(newPassword) ;

        try {
            return patientRepository.save(patient);
        } catch (Exception e){
            throw new RuntimeException("Invalid email format. Email or username is used.");
        }
    }
}
