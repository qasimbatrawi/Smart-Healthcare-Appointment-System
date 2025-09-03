package com.example.system.dto;

import com.example.system.entity.Specialty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class DoctorDTO {

    @NotNull
    private String username ;

    @NotNull
    private String email ;

    @NotNull
    private String password ;

    @NotNull
    private String name ;

    @NotEmpty
    private Set<Specialty> specialty = new HashSet<>();

}
