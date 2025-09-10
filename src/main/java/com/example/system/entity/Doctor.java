package com.example.system.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL) // update other classes when modifying Doctor
    @JoinColumn(name = "user_id")
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private User doctorDetails;

    @ManyToMany
    @NotEmpty(message = "Doctor must have at least one specialty")
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Specialty> specialty = new HashSet<>();

    @Column(nullable = false)
    private LocalTime workDayStart ;

    @Column(nullable = false)
    private LocalTime workDayEnd ;

    @Column(nullable = false)
    private Boolean freeze = false;

    public boolean isAvailable(LocalDateTime from , LocalDateTime to , List<Appointment> appointments){

        if (from.isAfter(to) || from.isEqual(to)) {
            return false;
        }

        LocalTime startTime = from.toLocalTime() ;
        LocalTime endTime = to.toLocalTime() ;

        if (startTime.isBefore(this.workDayStart) || endTime.isAfter(this.workDayEnd)){
            return false ;
        }

        for(Appointment appointment : appointments){

            if (from.isBefore(appointment.getEndTime()) && to.isAfter(appointment.getStartTime())){
                return false ;
            }
        }

        return true ;
    }

}