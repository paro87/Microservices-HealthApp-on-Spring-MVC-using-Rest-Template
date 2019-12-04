package com.paro.departmentservice.service;

import com.paro.departmentservice.model.Department;
import com.paro.departmentservice.model.Patient;
import com.paro.departmentservice.repository.DepartmentRepository;
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

class DepartmentServiceTest {

    @Mock
    DepartmentRepository departmentRepository;

    @InjectMocks
    DepartmentService departmentService;          //@InjectMocks creates an instance of the class and injects the mocks that are created with the @Mock annotations into this instance

    private Department department1;
    private Department department2;
    private List<Department> departmentList;
    private List<Patient> patientList;
    private Patient patient1;
    private Patient patient2;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);
        departmentList= new ArrayList<>();
        department1=new Department(11L, "Cardiology", 1L, null);
        department2=new Department(12L, "Neurology", 1L, null);
        departmentList.add(department1);
        departmentList.add(department2);
        patient1=new Patient(1L, "John", "Adams", 1L, 11L);
        patient2=new Patient(2L, "Mary", "James", 1L, 11L);
        patientList=new ArrayList<>();
        patientList.add(patient1);
        patientList.add(patient2);

        department1.setPatientList(patientList);
    }

    @Test
    void getAll() {
        given(departmentRepository.findAll()).willReturn(departmentList);
        List<Department> departmentsFound=departmentService.getAll();
        assertThat(departmentsFound.size()).isEqualTo(patientList.size());
        verify(departmentRepository).findAll();
    }

    @Test
    void getById() {
        given(departmentRepository.findById(11L)).willReturn(Optional.ofNullable(department1));
        Department departmentFound=departmentService.getById(11L);
        assertThat(departmentFound.getName()).isEqualTo("Cardiology");
        verify(departmentRepository).findById(11L);
    }

    @Test
    void add() {
        given(departmentRepository.save(any(Department.class))).willReturn(department1);
        given(departmentRepository.findById(department1.getId())).willReturn(Optional.ofNullable(department1));
        Department departmentFound=departmentService.add(department1);
        assertThat(departmentFound.getId()).isEqualTo(11L);

        verify(departmentRepository).save(any(Department.class));
    }

    @Test
    void getByHospitalId() {
        given(departmentRepository.findByHospitalId(1L)).willReturn(departmentList);
        List<Department> departmentsFound=departmentService.getByHospitalId(1L);
        assertThat(departmentsFound.size()).isEqualTo(departmentList.size());

        verify(departmentRepository).findByHospitalId(1L);
    }

    @Test
    void getByHospitalWithPatients() {
        given(departmentRepository.findByHospitalId(1L)).willReturn(departmentList);
        List<Department> departmentsFound=departmentService.getByHospitalId(1L);
        Patient patient=departmentsFound.get(0).getPatientList().get(0);
        assertThat(departmentsFound.size()).isEqualTo(departmentList.size());
        assertThat(departmentsFound.get(0).getPatientList().size()).isEqualTo(patientList.size());
        assertThat(patient.getFirstname()).isEqualTo(patient1.getFirstname());
        verify(departmentRepository).findByHospitalId(1L);
    }
}