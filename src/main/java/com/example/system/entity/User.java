package com.example.system.entity;

import com.example.system.Enum.RoleName;
import com.example.system.Enum.SpecialtyName;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;

    @Column(unique = true)
    private String username ;

    @Column(unique = true)
    private String email ;

    private String password ;

    private String name ;

    @Column(name = "_role")
    @OneToOne
    private Role role ;

}
