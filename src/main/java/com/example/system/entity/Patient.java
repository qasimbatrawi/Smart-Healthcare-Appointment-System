package com.example.system.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL) // update other classes when modifying User
    @JoinColumn(name = "user_id")
    private User patientDetails ;

    public boolean isAvailable(LocalDateTime from , LocalDateTime to , List<Appointment> appointments){

        if (from.isAfter(to) || from.isEqual(to)) {
            return false;
        }

        LocalTime startTime = from.toLocalTime() ;
        LocalTime endTime = to.toLocalTime() ;

        for(Appointment appointment : appointments){

            if (from.isBefore(appointment.getEndTime()) && to.isAfter(appointment.getStartTime())){
                return false ;
            }
        }

        return true ;
    }

}
