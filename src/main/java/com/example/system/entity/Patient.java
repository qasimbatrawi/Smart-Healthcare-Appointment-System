package com.example.system.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;

@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL) // update other classes when modifying User
    @JoinColumn(name = "user_id")
    private User patientDetails ;


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getPatientDetails() { return patientDetails; }
    public void setPatientDetails(User patientDetails) { this.patientDetails = patientDetails; }

}
