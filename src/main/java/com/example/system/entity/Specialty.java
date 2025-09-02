package com.example.system.entity;

import com.example.system.Enum.SpecialtyName;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Specialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Enumerated(EnumType.STRING)
    private SpecialtyName specialtyName ;

}
