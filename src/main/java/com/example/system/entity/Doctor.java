package com.example.system.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL) // update other classes when modifying Doctor
    @JoinColumn(name = "user_id")
    private User doctorDetails;

    @ManyToMany
    @NotEmpty(message = "Doctor must have at least one specialty")
    private Set<Specialty> specialty = new HashSet<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private Set<Appointment> appointments = new HashSet<>();

    @Column(nullable = false)
    private LocalTime workDayStart ;

    @Column(nullable = false)
    private LocalTime workDayEnd ;

    public boolean isAvailable(LocalDateTime from , LocalDateTime to){

        LocalTime startTime = from.toLocalTime() ;
        LocalTime endTime = to.toLocalTime() ;

        boolean available = true ;

        if (startTime.isBefore(this.getWorkDayStart()) ||
            startTime.isAfter(this.getWorkDayEnd()) ||
            endTime.isBefore(this.getWorkDayStart()) ||
            endTime.isAfter(this.getWorkDayEnd())){
            available = false ;
        }

        for(Appointment appointment : this.appointments){
            if (appointment.getStartTime().isBefore(from)
                && appointment.getEndTime().isAfter(to)) {
                available = false;
                break;
            }
        }

        return available ;
    }

}