package com.paro.hospitalservice.service;

import com.paro.hospitalservice.model.Department;
import com.paro.hospitalservice.model.Hospital;
import com.paro.hospitalservice.model.Patient;
import com.paro.hospitalservice.repository.HospitalRepository;
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
import static org.mockito.Mockito.verify;

class HospitalServiceTest {

    @Mock
    HospitalRepository hospitalRepository;

    @InjectMocks
    HospitalService hospitalService;

    private Hospital hospital1;
    private Hospital hospital2;
    private List<Hospital> hospitalList;
    private Department department1;
    private Department department2;
    private List<Department> departmentList;
    private Patient patient1;
    private Patient patient2;
    private List<Patient> patientList;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        patient1=new Patient(1L, "John", "Adams", 1L, 11L);
        patient2=new Patient(2L, "Mary", "James", 1L, 11L);
        patientList=new ArrayList<>();
        patientList.add(patient1);
        patientList.add(patient2);

        department1=new Department(11L, "Cardiology", 1L, null);
        department2=new Department(12L, "Neurology", 1L, null);
        departmentList= new ArrayList<>();
        departmentList.add(department1);
        departmentList.add(department2);

        hospital1=new Hospital(1L, "Johns Hopkins", "US", null, null);
        hospital2=new Hospital(2L, "Charité", "Germany", null, null);
        hospitalList=new ArrayList<>();
        hospitalList.add(hospital1);
        hospitalList.add(hospital2);

        department1.setPatientList(patientList);
        hospital1.setDepartmentList(departmentList);
        hospital1.setPatientList(patientList);
    }

    @Test
    void getAll() {
        given(hospitalRepository.findAll()).willReturn(hospitalList);
        List<Hospital> hospitalsFound=hospitalService.getAll();
        assertThat(hospitalsFound.size()).isEqualTo(hospitalList.size());
        verify(hospitalRepository).findAll();
    }

    @Test
    void getById() {
        given(hospitalRepository.findById(1L)).willReturn(Optional.ofNullable(hospital1));
        Hospital hospitalFound=hospitalService.getById(1L);
        assertThat(hospitalFound.getName()).isEqualTo("Johns Hopkins");
        verify(hospitalRepository).findById(1L);
    }

    @Test
    void add() {
        given(hospitalRepository.save(any(Hospital.class))).willReturn(hospital1);
        given(hospitalRepository.findById(hospital1.getId())).willReturn(Optional.ofNullable(hospital1));
        Hospital hospitalFound=hospitalService.add(hospital1);
        assertThat(hospitalFound.getId()).isEqualTo(1L);

        verify(hospitalRepository).save(any(Hospital.class));
    }

    @Test
    void getHospitalWithDepartments() {
        given(hospitalRepository.findById(1L)).willReturn(Optional.ofNullable(hospital1));
        Hospital hospitalFound=hospitalService.getById(1L);
        Department department=hospitalFound.getDepartmentList().get(0);
        assertThat(department.getId()).isEqualTo(11L);
        assertThat(department.getName()).isEqualTo("Cardiology");
        verify(hospitalRepository).findById(1L);
    }

    @Test
    void getHospitalWithDepartmentsAndPatients() {
        given(hospitalRepository.findById(1L)).willReturn(Optional.ofNullable(hospital1));
        Hospital hospitalFound=hospitalService.getById(1L);
        Department department=hospitalFound.getDepartmentList().get(0);
        Patient patient=hospitalFound.getDepartmentList().get(0).getPatientList().get(0);
        assertThat(hospitalFound.getDepartmentList().size()).isEqualTo(departmentList.size());
        assertThat(hospitalFound.getDepartmentList().get(0).getPatientList().size()).isEqualTo(patientList.size());
        assertThat(department.getId()).isEqualTo(11L);
        assertThat(department.getName()).isEqualTo("Cardiology");
        assertThat(patient.getFirstname()).isEqualTo(patient1.getFirstname());
        verify(hospitalRepository).findById(1L);
    }

    @Test
    void getHospitalWithPatients() {
        given(hospitalRepository.findById(1L)).willReturn(Optional.ofNullable(hospital1));
        Hospital hospitalFound=hospitalService.getById(1L);
        Patient patient=hospitalFound.getPatientList().get(0);
        assertThat(hospitalFound.getPatientList().size()).isEqualTo(patientList.size());
        assertThat(patient.getFirstname()).isEqualTo(patient1.getFirstname());
        verify(hospitalRepository).findById(1L);
    }
}