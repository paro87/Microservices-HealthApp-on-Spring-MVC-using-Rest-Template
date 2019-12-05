package com.paro.hospitalservice.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.paro.hospitalservice.controller.HospitalController;
import com.paro.hospitalservice.model.Department;
import com.paro.hospitalservice.model.Hospital;
import com.paro.hospitalservice.model.Patient;
import com.paro.hospitalservice.repository.HospitalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Service
public class HospitalService {
    private static final Logger LOGGER= LoggerFactory.getLogger(HospitalService.class);

    private HospitalRepository hospitalRepository;

    @LoadBalanced
    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    public void HospitalService (HospitalRepository hospitalRepository){
        this.hospitalRepository=hospitalRepository;

    }

    public List<Hospital> getAll(){
        LOGGER.info("Hospitals found");
        return hospitalRepository.findAll();
    }

    public Hospital getById( Long hospitalId){
        Hospital hospitalFound=hospitalRepository.findById(hospitalId).orElse(null);
        LOGGER.info("Hospital found with id={}", hospitalId);
        return hospitalFound;
    }

    public Hospital add( Hospital hospital){
        hospitalRepository.save(hospital);
        LOGGER.info("Hospital added with id={}", hospital.getId());
        Hospital hospitalSaved=hospitalRepository.findById(hospital.getId()).orElse(null);
        return hospitalSaved;
    }

    private String departmentClient="http://department-service";
    private String patientClient="http://patient-service";
    private static final String RESOURCE_PATH="/hospital/";
    private String REQUEST_URI_Department=departmentClient+RESOURCE_PATH;
    private String REQUEST_URI_Patient=patientClient+RESOURCE_PATH;

    @HystrixCommand(fallbackMethod = "getHospitalWithDepartments_Fallback")
    public Hospital getHospitalWithDepartments(Long hospitalId){
        Hospital hospital=hospitalRepository.findById(hospitalId).orElse(null);
        ResponseEntity<List<Department>> responseEntity = restTemplate.exchange(REQUEST_URI_Department+hospital.getId(), HttpMethod.GET, null, new ParameterizedTypeReference<List<Department>>() {});
        hospital.setDepartmentList(responseEntity.getBody());
        LOGGER.info("Departments found with hospital id={}", hospitalId);
        return hospital;
    }

    @HystrixCommand(fallbackMethod = "getHospitalWithDepartmentsAndPatients_Fallback")
    public Hospital getHospitalWithDepartmentsAndPatients(Long hospitalId){
        Hospital hospital=hospitalRepository.findById(hospitalId).orElse(null);
        ResponseEntity<List<Department>> responseEntity = restTemplate.exchange(REQUEST_URI_Department+hospital.getId()+"/with-patients", HttpMethod.GET, null, new ParameterizedTypeReference<List<Department>>() {});
        hospital.setDepartmentList(responseEntity.getBody());
        LOGGER.info("Departments and patients found with hospital id={}", hospitalId);
        return hospital;
    }

    @HystrixCommand(fallbackMethod = "getHospitalWithPatients_Fallback")
    public Hospital getHospitalWithPatients(Long hospitalId){
        Hospital hospital=hospitalRepository.findById(hospitalId).orElse(null);
        ResponseEntity<List<Patient>> responseEntity = restTemplate.
                exchange(REQUEST_URI_Patient+hospital.getId(), HttpMethod.GET, null, new ParameterizedTypeReference<List<Patient>>() {});
        hospital.setPatientList(responseEntity.getBody());
        LOGGER.info("Patients found with hospital id={}", hospitalId);
        return hospital;
    }

    @SuppressWarnings("unused")
    private Hospital getHospitalWithDepartments_Fallback(Long hospitalId){
        Hospital hospital=hospitalRepository.findById(hospitalId).orElse(null);
        Department departmentNotFound=new Department(0L, "UNKNOWN", hospitalId,null);
        List<Department> departmentListNotFound=new ArrayList<>();
        departmentListNotFound.add(departmentNotFound);
        hospital.setDepartmentList(departmentListNotFound);
        return hospital;
    }

    @SuppressWarnings("unused")
    private Hospital getHospitalWithDepartmentsAndPatients_Fallback(Long hospitalId){
        Hospital hospital=hospitalRepository.findById(hospitalId).orElse(null);
        ResponseEntity<List<Department>> responseEntity=null;
        try {
            responseEntity = restTemplate.exchange(REQUEST_URI_Department+hospital.getId()+"/with-patients", HttpMethod.GET, null, new ParameterizedTypeReference<List<Department>>() {});
            List<Department> departmentList=responseEntity.getBody();
            List<Patient> patientListNotFound=new ArrayList<>();
            for (Department department: departmentList) {
                Patient patientNotFound=new Patient(0L, "UNKNOWN", "UNKNOWN", hospitalId,department.getId());
                patientListNotFound.add(patientNotFound);
                department.setPatientList(patientListNotFound);
            }
            hospital.setDepartmentList(departmentList);
        } catch (Exception exception){

            System.out.println(exception.getLocalizedMessage());
            Patient patientNotFound=new Patient(0L, "UNKNOWN", "UNKNOWN", hospitalId,0L);
            List<Patient> patientListNotFound=new ArrayList<>();
            patientListNotFound.add(patientNotFound);
            Department departmentNotFound=new Department(0L, "UNKNOWN", hospitalId,null);
            List<Department> departmentListNotFound=new ArrayList<>();
            departmentNotFound.setPatientList(patientListNotFound);
            departmentListNotFound.add(departmentNotFound);
            hospital.setDepartmentList(departmentListNotFound);
        }




//        Patient patientNotFound=new Patient(0L, "UNKNOWN", "UNKNOWN", hospitalId,0L);
//        List<Patient> patientListNotFound=new ArrayList<>();
//        patientListNotFound.add(patientNotFound);
//        Department departmentNotFound=new Department(0L, "UNKNOWN", hospitalId,null);
//        List<Department> departmentListNotFound=new ArrayList<>();
//        departmentNotFound.setPatientList(patientListNotFound);
//        departmentListNotFound.add(departmentNotFound);
//        hospital.setDepartmentList(departmentListNotFound);
        return hospital;
    }

    @SuppressWarnings("unused")
    private Hospital getHospitalWithPatients_Fallback(Long hospitalId){
        Hospital hospital=hospitalRepository.findById(hospitalId).orElse(null);
        Patient patientNotFound=new Patient(0L, "UNKNOWN", "UNKNOWN", hospitalId,0L);
        List<Patient> patientListNotFound=new ArrayList<>();
        patientListNotFound.add(patientNotFound);
        hospital.setPatientList(patientListNotFound);
        return hospital;
    }
}
