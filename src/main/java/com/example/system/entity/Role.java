package com.example.system.entity;

import com.example.system.Enum.RoleName;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "_role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private RoleName roleName ;

}
