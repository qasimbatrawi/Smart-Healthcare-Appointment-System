package com.example.system.document;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("Collection = Prescription")
@Data
public class Prescription {

    @Id
    private String id ;

    @NotNull
    private LocalDateTime date ;

    @NotNull
    private int appointmentId ;

    @NotNull
    private String notes ;
}
