package com.example.system.service;

import com.example.system.Enum.SpecialtyName;
import com.example.system.entity.Doctor;
import com.example.system.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class PatientService {

    private DoctorRepository doctorRepository ;

    public PatientService (DoctorRepository doctorRepository){
        this.doctorRepository = doctorRepository ;
    }

    public Map<LocalDate, List<Map<LocalTime, LocalTime>>> getAvailableTimeForDoctorByUsernameThisWeek(String username){
        Doctor doctor = doctorRepository.findByDoctorDetails_Username(username)
                .orElseThrow(() -> new RuntimeException("Doctor not found")) ;

        LocalDate today = LocalDate.now() ;
        LocalDate weekend = today.plusDays(6) ;

        Map<LocalDate, List<Map<LocalTime, LocalTime>>> available = new HashMap<>() ;
        while (!today.isAfter(weekend)) {

            LocalTime start = doctor.getWorkDayStart() ;
            LocalTime workEnd = doctor.getWorkDayEnd() ;

            while (start.isBefore(workEnd)) {

                LocalDateTime from = LocalDateTime.of(today, start);
                LocalDateTime to = from;

                while (to.isBefore(LocalDateTime.of(today, workEnd)) && doctor.isAvailable(from, to.plusMinutes(5))){
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
}
