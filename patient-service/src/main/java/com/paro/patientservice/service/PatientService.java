package com.paro.patientservice.service;

import com.paro.patientservice.model.Patient;
import com.paro.patientservice.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PatientService.class);

    //Field Injection - The Ugly
    /*
    @Autowired
    private PatientRepository patientRepository;
    */
    //Setter Injection - The Bad
    /*
    private PatientRepository patientRepository;
    @Autowired
    public void setPatientRepository(PatientRepository patientRepository){
        this.patientRepository=patientRepository;
    }
    */
    //Constructor Injection (1) - The Good
    private PatientRepository patientRepository;
    @Autowired
    public PatientService(PatientRepository patientRepository){
        this.patientRepository=patientRepository;
    }
    //Constructor Injection (2) - The Good
    //(For a single-constructor scenario)
/*    private PatientRepository patientRepository;
    public PatientService(PatientRepository patientRepository){

        this.patientRepository=patientRepository;
    }*/

    public List<Patient> getAll() {
        List<Patient> patientsFound=patientRepository.findAll();
        LOGGER.info("Patients found");
        return patientsFound;
    }

    public Patient getById(Long patientId) {
        Patient patientFound=patientRepository.findById(patientId).orElse(null);
        LOGGER.info("Patient found with id={}", patientId);
        return patientFound;
    }

    public Patient add(Patient patient) {
        Patient patientSaved= patientRepository.save(patient);
        LOGGER.info("Patient added with id={}", patient.getId());
        return patientSaved;
    }

    public List<Patient> getByDepartmentId(Long departmentId) {
        List<Patient> patientsFound=patientRepository.findByDepartmentId(departmentId);
        LOGGER.info("Patients found in department with id={}", departmentId);
        return patientsFound;
    }

    public List<Patient> getByHospitalId(Long hospitalId) {
        List<Patient> patientsFound=patientRepository.findByHospitalId(hospitalId);
        LOGGER.info("Patients found in hospital with id={}", hospitalId);
        return patientsFound;
    }
}
