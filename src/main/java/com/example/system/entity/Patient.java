package com.example.system.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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

        for(Appointment appointment : appointments){

            if (from.isBefore(appointment.getEndTime()) && to.isAfter(appointment.getStartTime())){
                return false ;
            }
        }

        return true ;
    }

}
