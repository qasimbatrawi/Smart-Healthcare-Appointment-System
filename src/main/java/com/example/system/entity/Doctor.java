package com.example.system.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;

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
    private Set<Specialty> specialty = new HashSet<>();


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getDoctorDetails() { return doctorDetails; }
    public void setDoctorDetails(User doctorDetails) { this.doctorDetails = doctorDetails; }

    public Set<Specialty> getSpecialty() { return specialty; }
    public void setSpecialty(Set<Specialty> specialty) { this.specialty = specialty; }

}