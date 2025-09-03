package com.example.system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

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

    @ManyToOne
    private Doctor doctor ;

    @ManyToOne
    private Patient patient ;

}
