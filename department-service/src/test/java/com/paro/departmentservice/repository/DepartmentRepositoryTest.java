package com.paro.departmentservice.repository;

import com.paro.departmentservice.DepartmentServiceApplication;
import com.paro.departmentservice.model.Department;
import com.paro.departmentservice.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@PropertySource("classpath:application-test.properties")
@ActiveProfiles("test")
@ContextConfiguration(classes = {DepartmentConfig.class}, loader = AnnotationConfigContextLoader.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PatientunittestdemoApplication.class)
@Transactional
@SpringBootTest(classes = {DepartmentServiceApplication.class, DepartmentConfig.class })
@DirtiesContext
class DepartmentRepositoryTest {

    @Resource
    private DepartmentRepository departmentRepository;

    private Department department1;
    private Department department2;
    private List<Department> departmentList;
    private List<Patient> patientList;
    private Patient patient1;
    private Patient patient2;

    @BeforeEach
    void setUp() {
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
    void findById() {
        departmentRepository.save(department1);
        Department departmentFound=departmentRepository.findById(11L).orElse(null);
        assertThat(departmentFound.getName()).isEqualTo("Cardiology");
    }

    @Test
    void findAll(){
        departmentRepository.save(department1);
        departmentRepository.save(department2);
        List<Department> departmentList=departmentRepository.findAll();
        assertThat(departmentList.size()).isEqualTo(2);
    }

    @Test
    void save(){
        departmentRepository.save(department1);
        Department departmentAdded= departmentRepository.findById(department1.getId()).orElse(null);
        assertThat(departmentAdded.getId()).isEqualTo(11L);
    }

    @Test
    void findByHospitalId() {
        departmentRepository.save(department1);
        departmentRepository.save(department2);
        List<Department> departmentList=departmentRepository.findByHospitalId(department1.getHospitalId());
        assertThat(departmentList.size()).isEqualTo(2);
    }
}