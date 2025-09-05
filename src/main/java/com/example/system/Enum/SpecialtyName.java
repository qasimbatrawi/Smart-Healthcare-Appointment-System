package com.example.system.Enum;

public enum SpecialtyName {
    GOOD,
    BAD ;

    public String getName(SpecialtyName specialtyName){
        return specialtyName.name() ;
    }
}
