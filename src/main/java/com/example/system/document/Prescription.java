package com.example.system.document;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document("Collection = Prescription")
@Data
public class Prescription {

    @Id
    private String id ;

    @NotNull
    private LocalDateTime date ;

    @NotNull
    private int appointmentId ;

    private String notes;

    private List<String> medicines;

    private Map<String, Object> labResults;
}
