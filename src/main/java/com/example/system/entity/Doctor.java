package com.example.system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class Doctor {

    @OneToOne
    private User doctorDetails ;

    @OneToMany
    private Specialty specialty ;

}