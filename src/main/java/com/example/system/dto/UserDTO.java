package com.example.system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDTO {

    @NotNull
    private String username ;

    @NotNull
    private String email ;

    @NotNull
    private String password ;

    @NotNull
    private String name ;
}
