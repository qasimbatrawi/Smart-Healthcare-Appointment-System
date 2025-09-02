package com.example.system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Doctor {

    @OneToOne
    private User doctorDetails ;

    @ManyToMany
    private Set<Specialty> specialty = new HashSet<>();

}