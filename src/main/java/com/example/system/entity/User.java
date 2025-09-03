package com.example.system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
@Entity
@Table(name = "_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;

    @Column(unique = true, nullable = false)
    private String username ;

    @Column(unique = true, nullable = false)
    @Email(message = "Invalid email format")
    private String email ;

    @Column(nullable = false)
    private String password ;

    @Column(nullable = false)
    private String name ;

    @ManyToOne // each user has one role, and many users can share the same role
    @JoinColumn(name = "role_id", nullable = false)
    private Role role ;


}
