package com.paro.hospitalservice.repository;

import com.paro.hospitalservice.HospitalServiceApplication;
import com.paro.hospitalservice.model.Department;
import com.paro.hospitalservice.model.Hospital;
import com.paro.hospitalservice.model.Patient;
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
@ContextConfiguration(classes = {HospitalConfig.class}, loader = AnnotationConfigContextLoader.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PatientunittestdemoApplication.class)
@Transactional
@SpringBootTest(classes = {HospitalServiceApplication.class, HospitalConfig.class })
@DirtiesContext
class HospitalRepositoryTest {
    @Resource
    private HospitalRepository hospitalRepository;

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
    void findById() {
        hospitalRepository.save(hospital1);
        Hospital hospitalFound=hospitalRepository.findById(1L).orElse(null);
        assertThat(hospitalFound.getName()).isEqualTo("Johns Hopkins");
    }

    @Test
    void findAll(){
        hospitalRepository.save(hospital1);
        hospitalRepository.save(hospital2);
        List<Hospital> hospitalList=hospitalRepository.findAll();
        assertThat(hospitalList.size()).isEqualTo(2);
    }

    @Test
    void save(){
        hospitalRepository.save(hospital1);
        Hospital hospitalAdded= hospitalRepository.findById(hospital1.getId()).orElse(null);
        assertThat(hospitalAdded.getId()).isEqualTo(1L);
    }
}