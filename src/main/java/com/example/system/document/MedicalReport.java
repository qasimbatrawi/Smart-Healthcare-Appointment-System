package com.example.system.document;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document("Collection = MedicalReport")
@Data
public class MedicalReport {

    @Id
    private String id ;

    @NotNull
    private LocalDateTime date ;

    @NotNull
    private Long appointmentId ;

    private Prescription prescription;

    private List<LabResult> labResults;
}
