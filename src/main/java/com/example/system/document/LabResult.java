package com.example.system.document;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LabResult {

    private String testName ;
    private LocalDateTime date ;
    private String result ;
}
