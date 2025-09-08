package com.example.system.service;

import com.example.system.document.LabResult;
import com.example.system.document.MedicalReport;
import com.example.system.document.Prescription;
import com.example.system.entity.Appointment;
import com.example.system.entity.Doctor;
import com.example.system.repository.AppointmentRepository;
import com.example.system.repository.MedicalReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final AppointmentRepository appointmentRepository ;
    private final MedicalReportRepository medicalReportRepository;
    
    public Appointment markAppointmentCompleted(String username, Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found")) ;

        if (!appointment.getDoctor().getDoctorDetails().getUsername().equals(username)){
            throw new RuntimeException("Cannot view appointments for other doctors.") ;
        }

        appointment.setCompleted(true);

        return appointmentRepository.save(appointment) ;
    }

    public MedicalReport addLabResult(String username, Long appointmentId, LabResult labResult) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found")) ;

        if (!appointment.getDoctor().getDoctorDetails().getUsername().equals(username)){
            throw new RuntimeException("Cannot view appointments for other doctors.") ;
        }

        if (labResult.getResult() == null || labResult.getDate() == null || labResult.getTestName() == null){
            throw new RuntimeException("Field must not be empty.") ;
        }

        MedicalReport medicalReport = medicalReportRepository.findByAppointmentId(appointmentId) ;

        if (medicalReport == null){
            medicalReport = new MedicalReport() ;
            medicalReport.setAppointmentId(appointmentId);
            medicalReport.setLabResults(new ArrayList<>());
        }

        if (medicalReport.getLabResults() == null){
            medicalReport.setLabResults(new ArrayList<>());
        }
        medicalReport.getLabResults().add(labResult) ;

        return medicalReportRepository.save(medicalReport) ;
    }

    public MedicalReport newPrescription(String username, Long appointmentId, Prescription prescription) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found")) ;

        if (!appointment.getDoctor().getDoctorDetails().getUsername().equals(username)){
            throw new RuntimeException("Cannot view appointments for other doctors.") ;
        }

        if (prescription.getNotes() == null){
            throw new RuntimeException("Prescription must have notes.") ;
        }

        MedicalReport medicalReport = medicalReportRepository.findByAppointmentId(appointmentId) ;

        if (medicalReport == null){
            medicalReport = new MedicalReport() ;
            medicalReport.setAppointmentId(appointmentId);
        }

        // each appointment must have exactly one medical report
        medicalReport.setPrescription(prescription) ;

        return medicalReportRepository.save(medicalReport) ;
    }

    public List<Appointment> getTodayAppointment(String username) {
        
        List<Appointment> appointments = appointmentRepository.findByDoctor_DoctorDetails_Username(username) ;

        List<Appointment> todayAppointments = appointments.stream()
                .filter(appointment -> appointment
                        .getStartTime()
                        .toLocalDate()
                        .equals(LocalDate.now())
                )
                .toList() ;

        return todayAppointments ;
    }
}
