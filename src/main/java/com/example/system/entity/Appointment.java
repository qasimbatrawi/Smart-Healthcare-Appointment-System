package com.example.system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @NotNull
    private LocalDateTime startTime ;

    @NotNull
    private LocalDateTime endTime ;

    @NotNull
    private boolean completed ;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor ;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient ;

}
