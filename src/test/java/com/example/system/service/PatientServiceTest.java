package com.example.system.service;

import com.example.system.Enum.RoleName;
import com.example.system.Enum.SpecialtyName;
import com.example.system.dto.AppointmentDTO;
import com.example.system.entity.*;
import com.example.system.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PatientServiceTest {

    @InjectMocks
    private PatientService patientService;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private SpecialtyRepository specialtyRepository;

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

        appointmentRepository.deleteAll();

    }

    @Test
    public void testBookAppointmentSuccess() {

        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setDoctorUsername("qasim_doctor1");
        appointmentDTO.setDate(LocalDate.now().plusDays(1));
        appointmentDTO.setStartTime(LocalTime.of(10, 0));
        appointmentDTO.setEndTime(LocalTime.of(10, 30));

        when(doctorRepository.findByDoctorDetails_Username("qasim_doctor1"))
                .thenReturn(Optional.of(doctor1));

        when(patientRepository.findByPatientDetails_Username("qasim_patient1"))
                .thenReturn(Optional.of(patient1));

        when(appointmentRepository.findByDoctor_DoctorDetails_Username("qasim_doctor1"))
                .thenReturn(new ArrayList<>());

        when(appointmentRepository.findByPatient_PatientDetails_Username("qasim_patient1"))
                .thenReturn(new ArrayList<>());

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor1);
        appointment.setPatient(patient1);
        appointment.setStartTime(LocalDateTime.of(appointmentDTO.getDate(), appointmentDTO.getStartTime()));
        appointment.setEndTime(LocalDateTime.of(appointmentDTO.getDate(), appointmentDTO.getEndTime()));
        appointment.setCompleted(false);

        when(appointmentRepository.save(appointment))
                .thenReturn(appointment);

        Appointment savedOne = patientService.bookAppointment("qasim_patient1", appointmentDTO);

        assertNotNull(savedOne);
        assertEquals("qasim_patient1", savedOne.getPatient().getPatientDetails().getUsername());
        assertEquals("qasim_doctor1", savedOne.getDoctor().getDoctorDetails().getUsername());
        assertEquals(LocalDateTime.of(appointmentDTO.getDate(), appointmentDTO.getStartTime()), savedOne.getStartTime());
        assertEquals(LocalDateTime.of(appointmentDTO.getDate(), appointmentDTO.getEndTime()), savedOne.getEndTime());
    }

    @Test
    public void testDoctorAvailableTimeSuccess(){

        when(doctorRepository.findByDoctorDetails_Username("qasim_doctor1"))
                .thenReturn(Optional.of(doctor1));

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor1);
        appointment.setPatient(patient1);
        appointment.setStartTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(10, 0)));
        appointment.setEndTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(10, 30)));
        appointment.setCompleted(false);

        appointmentRepository.save(appointment) ;

        when(appointmentRepository.findByDoctor_DoctorDetails_Username("qasim_doctor1"))
                .thenReturn(Collections.singletonList(appointment));


        Map<LocalDate, List<Map<LocalTime, LocalTime>>> result = patientService.getAvailableTimeForDoctorByUsernameThisWeek("qasim_doctor1") ;

        assertNotNull(result);
        assertTrue(result.containsKey(LocalDate.now().plusDays(1)));

        Map<LocalTime, LocalTime> slot = result.get(LocalDate.now().plusDays(1)).getFirst();

        LocalTime firstAvailableTimeStart = slot.keySet().iterator().next();
        LocalTime firstAvailableTimeEnd = slot.values().iterator().next();

        assertEquals(LocalTime.of(9, 0) , firstAvailableTimeStart);
        assertEquals(LocalTime.of(10, 0) , firstAvailableTimeEnd);
    }

    @Test
    public void testTwoPatientsBookForTheSameDoctorWithOverlappingTimeRejected(){

        AppointmentDTO appointmentDTO1 = new AppointmentDTO();
        appointmentDTO1.setDoctorUsername("qasim_doctor1");
        appointmentDTO1.setDate(LocalDate.now().plusDays(1));
        appointmentDTO1.setStartTime(LocalTime.of(10, 0));
        appointmentDTO1.setEndTime(LocalTime.of(10, 30));

        when(doctorRepository.findByDoctorDetails_Username("qasim_doctor1"))
                .thenReturn(Optional.of(doctor1));

        when(patientRepository.findByPatientDetails_Username("qasim_patient1"))
                .thenReturn(Optional.of(patient1));

        when(appointmentRepository.findByDoctor_DoctorDetails_Username("qasim_doctor1"))
                .thenReturn(new ArrayList<>());

        when(appointmentRepository.findByPatient_PatientDetails_Username("qasim_patient1"))
                .thenReturn(new ArrayList<>());

        Appointment appointment1 = new Appointment();
        appointment1.setDoctor(doctor1);
        appointment1.setPatient(patient1);
        appointment1.setStartTime(LocalDateTime.of(appointmentDTO1.getDate(), appointmentDTO1.getStartTime()));
        appointment1.setEndTime(LocalDateTime.of(appointmentDTO1.getDate(), appointmentDTO1.getEndTime()));
        appointment1.setCompleted(false);

        when(appointmentRepository.save(appointment1))
                .thenReturn(appointment1);

        Appointment savedAppointment1 = patientService.bookAppointment("qasim_patient1", appointmentDTO1);

        // test appointment 1 saved
        assertNotNull(savedAppointment1);
        assertEquals("qasim_patient1", savedAppointment1.getPatient().getPatientDetails().getUsername());
        assertEquals("qasim_doctor1", savedAppointment1.getDoctor().getDoctorDetails().getUsername());
        assertEquals(LocalDateTime.of(appointmentDTO1.getDate(), appointmentDTO1.getStartTime()), savedAppointment1.getStartTime());
        assertEquals(LocalDateTime.of(appointmentDTO1.getDate(), appointmentDTO1.getEndTime()), savedAppointment1.getEndTime());



        AppointmentDTO appointmentDTO2 = new AppointmentDTO();
        appointmentDTO2.setDoctorUsername("qasim_doctor1");
        appointmentDTO2.setDate(LocalDate.now().plusDays(1));
        appointmentDTO2.setStartTime(LocalTime.of(10, 10));
        appointmentDTO2.setEndTime(LocalTime.of(10, 40));

        when(doctorRepository.findByDoctorDetails_Username("qasim_doctor1"))
                .thenReturn(Optional.of(doctor1));

        when(patientRepository.findByPatientDetails_Username("qasim_patient2"))
                .thenReturn(Optional.of(patient2));

        when(appointmentRepository.findByDoctor_DoctorDetails_Username("qasim_doctor1"))
                .thenReturn(List.of(savedAppointment1));

        when(appointmentRepository.findByPatient_PatientDetails_Username("qasim_patient2"))
                .thenReturn(new ArrayList<>());


        Appointment appointment2 = new Appointment();
        appointment2.setDoctor(doctor1);
        appointment2.setPatient(patient2);
        appointment2.setStartTime(LocalDateTime.of(appointmentDTO2.getDate(), appointmentDTO2.getStartTime()));
        appointment2.setEndTime(LocalDateTime.of(appointmentDTO2.getDate(), appointmentDTO2.getEndTime()));
        appointment2.setCompleted(false);

        when(appointmentRepository.save(appointment2))
                .thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                patientService.bookAppointment("qasim_patient2", appointmentDTO2)
        );

        assertEquals("Doctor is not available at this time.", ex.getMessage());
    }

    @Test
    public void testSamePatientBooksForTwoDoctorsWithOverlappingTimeRejected(){

        AppointmentDTO appointmentDTO1 = new AppointmentDTO();
        appointmentDTO1.setDoctorUsername("qasim_doctor1");
        appointmentDTO1.setDate(LocalDate.now().plusDays(1));
        appointmentDTO1.setStartTime(LocalTime.of(10, 0));
        appointmentDTO1.setEndTime(LocalTime.of(10, 30));

        when(doctorRepository.findByDoctorDetails_Username("qasim_doctor1"))
                .thenReturn(Optional.of(doctor1));

        when(patientRepository.findByPatientDetails_Username("qasim_patient1"))
                .thenReturn(Optional.of(patient1));

        when(appointmentRepository.findByDoctor_DoctorDetails_Username("qasim_doctor1"))
                .thenReturn(new ArrayList<>());

        when(appointmentRepository.findByPatient_PatientDetails_Username("qasim_patient1"))
                .thenReturn(new ArrayList<>());

        Appointment appointment1 = new Appointment();
        appointment1.setDoctor(doctor1);
        appointment1.setPatient(patient1);
        appointment1.setStartTime(LocalDateTime.of(appointmentDTO1.getDate(), appointmentDTO1.getStartTime()));
        appointment1.setEndTime(LocalDateTime.of(appointmentDTO1.getDate(), appointmentDTO1.getEndTime()));
        appointment1.setCompleted(false);

        when(appointmentRepository.save(appointment1))
                .thenReturn(appointment1);

        Appointment savedAppointment1 = patientService.bookAppointment("qasim_patient1", appointmentDTO1);

        // test appointment 1 saved
        assertNotNull(savedAppointment1);
        assertEquals("qasim_patient1", savedAppointment1.getPatient().getPatientDetails().getUsername());
        assertEquals("qasim_doctor1", savedAppointment1.getDoctor().getDoctorDetails().getUsername());
        assertEquals(LocalDateTime.of(appointmentDTO1.getDate(), appointmentDTO1.getStartTime()), savedAppointment1.getStartTime());
        assertEquals(LocalDateTime.of(appointmentDTO1.getDate(), appointmentDTO1.getEndTime()), savedAppointment1.getEndTime());



        AppointmentDTO appointmentDTO2 = new AppointmentDTO();
        appointmentDTO2.setDoctorUsername("qasim_doctor2");
        appointmentDTO2.setDate(LocalDate.now().plusDays(1));
        appointmentDTO2.setStartTime(LocalTime.of(10, 10));
        appointmentDTO2.setEndTime(LocalTime.of(10, 40));

        when(doctorRepository.findByDoctorDetails_Username("qasim_doctor2"))
                .thenReturn(Optional.of(doctor2));

        when(patientRepository.findByPatientDetails_Username("qasim_patient1"))
                .thenReturn(Optional.of(patient1));

        when(appointmentRepository.findByDoctor_DoctorDetails_Username("qasim_doctor2"))
                .thenReturn(new ArrayList<>());

        when(appointmentRepository.findByPatient_PatientDetails_Username("qasim_patient1"))
                .thenReturn(List.of(savedAppointment1));


        Appointment appointment2 = new Appointment();
        appointment2.setDoctor(doctor2);
        appointment2.setPatient(patient1);
        appointment2.setStartTime(LocalDateTime.of(appointmentDTO2.getDate(), appointmentDTO2.getStartTime()));
        appointment2.setEndTime(LocalDateTime.of(appointmentDTO2.getDate(), appointmentDTO2.getEndTime()));
        appointment2.setCompleted(false);

        when(appointmentRepository.save(appointment2))
                .thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                patientService.bookAppointment("qasim_patient1", appointmentDTO2)
        );
        System.out.println(ex.getMessage());

        assertEquals("You have another book at this time.", ex.getMessage());
    }

}