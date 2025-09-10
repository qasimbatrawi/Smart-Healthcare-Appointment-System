package com.example.system.auth;

import com.example.system.Enum.RoleName;
import com.example.system.Enum.SpecialtyName;
import com.example.system.entity.*;
import com.example.system.exception.BadRequestException;
import com.example.system.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.when;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private SpecialtyRepository specialtyRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PatientRepository patientRepository;

    private Doctor doctor1 ;
    private Doctor doctor2 ;
    private Patient patient1 ;
    private Patient patient2 ;
    private User userDoctor1 ;
    private User userDoctor2 ;
    private User userPatient1 ;
    private User userPatient2 ;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userDoctor1 = new User() ;
        userDoctor1.setUsername("qasim_doctor1");
        userDoctor1.setName("Qasim");
        userDoctor1.setRole(roleRepository.findByRoleName(RoleName.DOCTOR));
        userDoctor1.setEmail("qasim_doctor1@gmail.com");
        userDoctor1.setPassword(passwordEncoder.encode("qasim1234"));

        Set<Specialty> specialties = new HashSet<>() ;
        specialties.add(specialtyRepository.findBySpecialtyName(SpecialtyName.GOOD)) ;

        doctor1 = new Doctor() ;
        doctor1.setDoctorDetails(userDoctor1);
        doctor1.setWorkDayStart(LocalTime.of(9 , 0));
        doctor1.setWorkDayEnd(LocalTime.of(17 , 0));
        doctor1.setSpecialty(specialties);

        userDoctor2 = new User() ;
        userDoctor2.setUsername("qasim_doctor");
        userDoctor2.setName("Qasim");
        userDoctor2.setRole(roleRepository.findByRoleName(RoleName.DOCTOR));
        userDoctor2.setEmail("qasim_doctor2@gmail.com");
        userDoctor2.setPassword(passwordEncoder.encode("qasim1234"));

        doctor2 = new Doctor() ;
        doctor2.setDoctorDetails(userDoctor2);
        doctor2.setWorkDayStart(LocalTime.of(9 , 0));
        doctor2.setWorkDayEnd(LocalTime.of(17 , 0));
        doctor2.setSpecialty(specialties);

        userPatient1 = new User() ;
        userPatient1.setUsername("qasim_patient1");
        userPatient1.setName("Qasim");
        userPatient1.setRole(roleRepository.findByRoleName(RoleName.PATIENT));
        userPatient1.setEmail("qasim_patient1@gmail.com");
        userPatient1.setPassword(passwordEncoder.encode("qasim1234"));

        patient1 = new Patient() ;
        patient1.setPatientDetails(userPatient1);


        userPatient2 = new User() ;
        userPatient2.setUsername("qasim_patient2");
        userPatient2.setName("Qasim");
        userPatient2.setRole(roleRepository.findByRoleName(RoleName.PATIENT));
        userPatient2.setEmail("qasim_patient1@gmail.com");
        userPatient2.setPassword(passwordEncoder.encode("qasim1234"));

        patient2 = new Patient() ;
        patient2.setPatientDetails(userPatient2);

    }

    @Test
    public void testRejectUsedEmail(){

        when(userRepository.save(userDoctor1)).thenReturn(userDoctor1) ;
        when(doctorRepository.save(doctor1)).thenReturn(doctor1) ;

        when(userRepository.save(userDoctor2))
                .thenThrow(new BadRequestException("Invalid email format. Email or username are used."));
    }

    @Test
    public void testRejectUsedUsername(){

        when(userRepository.save(userPatient1)).thenReturn(userPatient1) ;
        when(patientRepository.save(patient1)).thenReturn(patient1) ;

        when(userRepository.save(userPatient2))
                .thenThrow(new BadRequestException("Invalid email format. Email or username are used."));
    }


}