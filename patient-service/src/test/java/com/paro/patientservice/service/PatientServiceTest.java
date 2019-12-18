package com.paro.patientservice.service;

import com.paro.patientservice.model.Patient;
import com.paro.patientservice.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;


class PatientServiceTest {
    @Mock
    PatientRepository patientRepository;

    @InjectMocks
    PatientService patientService;          //@InjectMocks creates an instance of the class and injects the mocks that are created with the @Mock annotations into this instance

    private Patient patient1;
    private Patient patient2;
    private List<Patient> patientList;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);
        patient1=new Patient(1L, "John", "Adams", 2L, 23L);
        patient2=new Patient(2L, "Mary", "James", 2L, 23L);
        patientList= new ArrayList<>();
        patientList.add(patient1);
        patientList.add(patient2);
    }

    @Test
    void getAll() {
        given(patientRepository.findAll()).willReturn(patientList);
        List<Patient> patientsFound=patientService.getAll();
        assertThat(patientsFound.size()).isEqualTo(patientList.size());

        verify(patientRepository).findAll();                           //Verifying if the mockBean was called
    }

    @Test

    void getById() {
        given(patientRepository.findById(1L)).willReturn(Optional.ofNullable(patient1));
        //when(patientRepository.findById(1L)).thenReturn(Optional.ofNullable(patient));
        Patient patientFound=patientService.getById(1L);
        assertThat(patientFound.getFirstname()).isEqualTo("John");

        verify(patientRepository).findById(1L);
    }

    @Test
    void add() {

        given(patientRepository.save(any(Patient.class))).willReturn(patient1);
        given(patientRepository.findById(patient1.getId())).willReturn(Optional.ofNullable(patient1));
        Patient patientFound=patientService.add(patient1);
        assertThat(patientFound.getId()).isEqualTo(1L);

        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void getByDepartmentId() {
        given(patientRepository.findByDepartmentId(23L)).willReturn(patientList);
        List<Patient> patientsFound=patientService.getByDepartmentId(23L);
        assertThat(patientsFound.size()).isEqualTo(patientList.size());

        verify(patientRepository).findByDepartmentId(23L);
    }

    @Test
    void getByHospitalId() {
        given(patientRepository.findByHospitalId(2L)).willReturn(patientList);
        List<Patient> patientsFound=patientService.getByHospitalId(2L);
        assertThat(patientsFound.size()).isEqualTo(patientList.size());

        verify(patientRepository).findByHospitalId(2L);
    }
}