package com.example.system.document;

import lombok.Data;

import java.util.List;

@Data
public class Prescription {

    private String notes;

    private List<String> medicines;
}
