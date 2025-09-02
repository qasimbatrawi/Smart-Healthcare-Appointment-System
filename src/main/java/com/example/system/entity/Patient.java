package com.example.system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class Patient {

    @OneToOne
    private User patientDetails ;

}
