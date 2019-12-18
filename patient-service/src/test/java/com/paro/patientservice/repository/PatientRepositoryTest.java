package com.paro.patientservice.repository;

import com.paro.patientservice.PatientServiceApplication;
import com.paro.patientservice.model.Patient;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@PropertySource("classpath:application-test.properties")
@ActiveProfiles("test")
@ContextConfiguration(classes = {PatientConfig.class}, loader = AnnotationConfigContextLoader.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PatientunittestdemoApplication.class)
@Transactional
@SpringBootTest(classes = {PatientServiceApplication.class, PatientConfig.class })
@DirtiesContext
class PatientRepositoryTest {

    @Resource
    private PatientRepository patientRepository;

    private Patient patient1;
    private Patient patient2;

    @BeforeEach
    void setUp() {
        patient1=new Patient(1L, "John", "Adams", 2L, 23L);
        patient2=new Patient(2L, "Mary", "James", 2L, 7L);
    }

    @Test
    void findById() {
        patientRepository.save(patient1);
        Patient patientFound=patientRepository.findById(1L).orElse(null);
        assert patientFound != null;
        assertThat(patientFound.getFirstname()).isEqualTo("John");
    }

    @Test
    void findAll(){

        patientRepository.save(patient1);
        patientRepository.save(patient2);
        List<Patient> patientList=patientRepository.findAll();
        assertThat(patientList.size()).isEqualTo(2);
    }

    @Test
    void save(){
        patientRepository.save(patient1);
        Patient patientAdded= patientRepository.findById(patient1.getId()).orElse(null);
        assert patientAdded != null;
        assertThat(patientAdded.getSurname()).isEqualTo("Adams");
    }

    @Test
    void findByDepartmentId() {
        patientRepository.save(patient1);
        patientRepository.save(patient2);
        List<Patient> patientList=patientRepository.findByDepartmentId(patient1.getDepartmentId());
        assertThat(patientList.size()).isEqualTo(1);
    }

    @Test
    void findByHospitalId() {
        patientRepository.save(patient1);
        patientRepository.save(patient2);
        List<Patient> patientList=patientRepository.findByHospitalId(patient2.getHospitalId());
        assertThat(patientList.size()).isEqualTo(2);
    }
}