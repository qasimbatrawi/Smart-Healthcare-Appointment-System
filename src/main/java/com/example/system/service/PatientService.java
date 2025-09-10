package com.example.system.service;

import com.example.system.Enum.SpecialtyName;
import com.example.system.document.LabResult;
import com.example.system.document.MedicalReport;
import com.example.system.document.Prescription;
import com.example.system.dto.AppointmentDTO;
import com.example.system.dto.UserDTO;
import com.example.system.entity.Appointment;
import com.example.system.entity.Doctor;
import com.example.system.entity.Patient;
import com.example.system.exception.AccessDeniedException;
import com.example.system.exception.BadRequestException;
import com.example.system.exception.BookingConflictException;
import com.example.system.exception.ResourceNotFoundException;
import com.example.system.repository.AppointmentRepository;
import com.example.system.repository.DoctorRepository;
import com.example.system.repository.MedicalReportRepository;
import com.example.system.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final DoctorRepository doctorRepository ;
    private final AppointmentRepository appointmentRepository ;
    private final PatientRepository patientRepository ;
    private final PasswordEncoder passwordEncoder ;
    private final MedicalReportRepository medicalReportRepository ;

    public Appointment bookAppointment(String patientUsername , AppointmentDTO appointment){

        String doctorUsername = appointment.getDoctorUsername() ;
        LocalDate date = appointment.getDate() ;
        LocalTime start = appointment.getStartTime() ;
        LocalTime end = appointment.getEndTime() ;

        if (doctorUsername == null || date == null || start == null || end == null){
            throw new BadRequestException("Fields must not be empty.") ;
        }

        LocalDateTime dateTimeStart = LocalDateTime.of(date , start) ;

        Doctor doctor = doctorRepository.findByDoctorDetails_Username(doctorUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found.")) ;

        if (doctor.getFreeze() == true){
            throw new ResourceNotFoundException("Doctor not found.") ;
        }

        Patient patient = patientRepository.findByPatientDetails_Username(patientUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found.")) ;

        if (dateTimeStart.isBefore(LocalDateTime.now())){
            throw new BadRequestException("Invalid date.") ;
        }

        LocalDateTime startTime = LocalDateTime.of(date , start) ;
        LocalDateTime endTime = LocalDateTime.of(date , end) ;

        List<Appointment> doctorAppointments = appointmentRepository
                .findByDoctor_DoctorDetails_Username(doctorUsername) ;
        if (!doctor.isAvailable(startTime, endTime, doctorAppointments)) {
            throw new BookingConflictException("Doctor is not available at this time.");
        }

        List<Appointment> patientAppointments = appointmentRepository
                .findByPatient_PatientDetails_Username(patientUsername) ;
        if (!patient.isAvailable(startTime, endTime, patientAppointments)) {
            throw new BookingConflictException("You have another book at this time.");
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
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found")) ;

        if (doctor.getFreeze() == true){
            throw new ResourceNotFoundException("Doctor not found.") ;
        }

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
                start = to.toLocalTime().isAfter(start) ? to.toLocalTime() : start.plusMinutes(5);
            }

            today = today.plusDays(1);
        }

        return available ;
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

    public Patient updatePatient(String username , UserDTO newPatientDetails){

        Patient patient = patientRepository.findByPatientDetails_Username(username)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        String newUsername = newPatientDetails.getUsername() ;
        String newEmail = newPatientDetails.getEmail() ;
        String newName = newPatientDetails.getName() ;
        String newPassword = passwordEncoder.encode(newPatientDetails.getPassword()) ;

        if (newUsername == null || newEmail == null || newName == null
                || newPassword == null){
            throw new BadRequestException("Fields must not be empty.") ;
        }

        patient.getPatientDetails().setUsername(newUsername) ;
        patient.getPatientDetails().setEmail(newEmail) ;
        patient.getPatientDetails().setName(newName) ;
        patient.getPatientDetails().setPassword(newPassword) ;

        try {
            return patientRepository.save(patient);
        } catch (Exception e){
            throw new BadRequestException("Invalid email format. Email or username is used.");
        }
    }

    public void cancelAppointment(String username, Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found.")) ;

        if (!appointment.getPatient().getPatientDetails().getUsername().equals(username)){
            throw new AccessDeniedException("Cannot delete appointment for other patients.") ;
        }

        MedicalReport medicalReport = medicalReportRepository.findByAppointmentId(appointmentId) ;

        if (medicalReport != null){
            medicalReportRepository.delete(medicalReport);
        }

        appointmentRepository.delete(appointment);
    }

    public Prescription getPrescription(String username, Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found.")) ;

        if (appointment.getPatient().getPatientDetails().getName().equals(username)){
            throw new AccessDeniedException("You cannot view other patients appointments.") ;
        }

        MedicalReport medicalReport = medicalReportRepository.findByAppointmentId(appointmentId) ;

        if (medicalReport == null || medicalReport.getPrescription() == null){
            throw new ResourceNotFoundException("Prescription is not ready.") ;
        }

        return medicalReport.getPrescription() ;
    }

    public List<LabResult> getLabResults(String username, Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found.")) ;

        if (appointment.getPatient().getPatientDetails().getName().equals(username)){
            throw new AccessDeniedException("You cannot view other patients appointments.") ;
        }

        MedicalReport medicalReport = medicalReportRepository.findByAppointmentId(appointmentId) ;

        if (medicalReport == null || medicalReport.getLabResults() == null){
            throw new ResourceNotFoundException("Lab results are not ready.") ;
        }

        return medicalReport.getLabResults() ;
    }
}
