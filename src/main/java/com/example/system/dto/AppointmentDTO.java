package com.example.system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentDTO {

    @NotNull
    private LocalDate date ;

    @NotNull
    private LocalTime startTime ;

    @NotNull
    private LocalTime endTime ;

    @NotNull
    private String patientUsername ;

    @NotNull
    private String doctorUsername ;
}
