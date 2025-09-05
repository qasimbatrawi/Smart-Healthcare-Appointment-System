package com.example.system.repository;

import com.example.system.Enum.SpecialtyName;
import com.example.system.entity.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialtyRepository extends JpaRepository<Specialty , Long> {
    Specialty findBySpecialtyName(SpecialtyName specialtyName) ;
}
