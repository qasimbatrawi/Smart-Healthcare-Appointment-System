package com.example.system.service;

import com.example.system.Enum.RoleName;
import com.example.system.Enum.SpecialtyName;
import com.example.system.entity.Doctor;
import com.example.system.entity.Patient;
import com.example.system.entity.Specialty;
import com.example.system.entity.User;
import com.example.system.exception.ResourceNotFoundException;
import com.example.system.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AdminServiceTest {

    @Mock
    private UserRepository userRepository ;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private SpecialtyRepository specialtyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository ;

    @InjectMocks
    private AdminService adminService;

    private Doctor doctor1 ;
    private Doctor doctor2 ;
    private Patient patient1 ;
    private Patient patient2 ;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        User userDoctor1 = new User() ;
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

        User userDoctor2 = new User() ;
        userDoctor2.setUsername("qasim_doctor2");
        userDoctor2.setName("Qasim");
        userDoctor2.setRole(roleRepository.findByRoleName(RoleName.DOCTOR));
        userDoctor2.setEmail("qasim_doctor2@gmail.com");
        userDoctor2.setPassword(passwordEncoder.encode("qasim1234"));

        doctor2 = new Doctor() ;
        doctor2.setDoctorDetails(userDoctor2);
        doctor2.setWorkDayStart(LocalTime.of(9 , 0));
        doctor2.setWorkDayEnd(LocalTime.of(17 , 0));
        doctor2.setSpecialty(specialties);

        User userPatient1 = new User() ;
        userPatient1.setUsername("qasim_patient1");
        userPatient1.setName("Qasim");
        userPatient1.setRole(roleRepository.findByRoleName(RoleName.PATIENT));
        userPatient1.setEmail("qasim_patient1@gmail.com");
        userPatient1.setPassword(passwordEncoder.encode("qasim1234"));

        patient1 = new Patient() ;
        patient1.setPatientDetails(userPatient1);


        User userPatient2 = new User() ;
        userPatient2.setUsername("qasim_patient2");
        userPatient2.setName("Qasim");
        userPatient2.setRole(roleRepository.findByRoleName(RoleName.PATIENT));
        userPatient2.setEmail("qasim_patient2@gmail.com");
        userPatient2.setPassword(passwordEncoder.encode("qasim1234"));

        patient2 = new Patient() ;
        patient2.setPatientDetails(userPatient2);

        userRepository.save(userDoctor1) ;
        userRepository.save(userDoctor2) ;
        userRepository.save(userPatient1) ;
        userRepository.save(userPatient2) ;

        doctorRepository.save(doctor1) ;
        doctorRepository.save(doctor2) ;

        patientRepository.save(patient1) ;
        patientRepository.save(patient2) ;

    }

    @Test
    public void testGetAllPatients(){

        when(patientRepository.findAll()).thenReturn(List.of(patient1 , patient2));

        List<Patient> allPatients = adminService.getAllPatients();

        assertEquals(2, allPatients.size());
        assertEquals("qasim_patient1", allPatients.get(0).getPatientDetails().getUsername());
        assertEquals("qasim_patient2", allPatients.get(1).getPatientDetails().getUsername());
    }

    @Test
    public void testGetPatientByExistingUsername(){

        when(patientRepository.findByPatientDetails_Username("qasim_patient1"))
                .thenReturn(Optional.of(patient1));

        Patient result = adminService.getPatientByUsername("qasim_patient1");

        assertNotNull(result);
        assertEquals("qasim_patient1", result.getPatientDetails().getUsername());
    }

    @Test
    public void testGetPatientByNonExistingUsername(){

        when(patientRepository.findByPatientDetails_Username("qasim"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> adminService.getPatientByUsername("qasim"));
    }

    @Test
    public void testGetAllDoctors(){

        when(doctorRepository.findAll()).thenReturn(List.of(doctor1 , doctor2));

        List<Doctor> allDoctors = adminService.getAllDoctors();

        assertEquals(2, allDoctors.size());
        assertEquals("qasim_doctor1", allDoctors.get(0).getDoctorDetails().getUsername());
        assertEquals("qasim_doctor2", allDoctors.get(1).getDoctorDetails().getUsername());
    }

    @Test
    public void testGetDoctorByExistingUsername(){

        when(doctorRepository.findByDoctorDetails_Username("qasim_doctor1"))
                .thenReturn(Optional.of(doctor1));

        Doctor result = adminService.getDoctorByUsername("qasim_doctor1");

        assertNotNull(result);
        assertEquals("qasim_doctor1", result.getDoctorDetails().getUsername());
    }

    @Test
    public void testGetDoctorByNonExistingUsername(){

        when(doctorRepository.findByDoctorDetails_Username("qasim"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> adminService.getDoctorByUsername("qasim"));
    }

}