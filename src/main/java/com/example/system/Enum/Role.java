package com.example.system.Enum;

public enum Role {
    ADMIN(1),
    DOCTOR(2),
    PATIENT(3) ;

    private final int value;

    Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
