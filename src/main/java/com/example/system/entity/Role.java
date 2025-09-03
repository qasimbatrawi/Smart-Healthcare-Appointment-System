package com.example.system.entity;

import com.example.system.Enum.RoleName;
import jakarta.persistence.*;

@Entity
@Table(name = "_role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private RoleName roleName ;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public RoleName getRoleName() { return roleName; }
    public void setRoleName(RoleName roleName) { this.roleName = roleName; }

}
