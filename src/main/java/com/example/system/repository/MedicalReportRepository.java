package com.example.system.repository;

import com.example.system.document.MedicalReport;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MedicalReportRepository extends MongoRepository<MedicalReport, String> {
    MedicalReport findByAppointmentId(Long id) ;
}
