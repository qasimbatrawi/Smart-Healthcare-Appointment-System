package com.example.system.document;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class LabResult {

    private String testName ;
    private LocalDate date ;
    private LocalTime time ;
    private String result ;
}
