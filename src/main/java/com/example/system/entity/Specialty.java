package com.example.system.entity;

import com.example.system.Enum.SpecialtyName;
import jakarta.persistence.*;

@Entity
public class Specialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private SpecialtyName specialtyName ;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public SpecialtyName getSpecialtyName() { return specialtyName; }
    public void setSpecialtyName(SpecialtyName specialtyName) { this.specialtyName = specialtyName; }

}
