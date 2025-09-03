package com.example.system.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL) // update other classes when modifying Doctor
    @JoinColumn(name = "user_id")
    private User doctorDetails ;

    @ManyToMany
    @NotEmpty(message = "Doctor must have at least one specialty")
    private Set<Specialty> specialty = new HashSet<>();


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getDoctorDetails() { return doctorDetails; }
    public void setDoctorDetails(User doctorDetails) { this.doctorDetails = doctorDetails; }

    public Set<Specialty> getSpecialty() { return specialty; }
    public void setSpecialty(Set<Specialty> specialty) { this.specialty = specialty; }

}