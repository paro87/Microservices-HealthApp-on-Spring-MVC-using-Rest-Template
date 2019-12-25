package com.paro.departmentservice.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.paro.departmentservice.model.Department;
import com.paro.departmentservice.model.Patient;
import com.paro.departmentservice.repository.DepartmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentService.class);
    private final DepartmentRepository departmentRepository;

    @LoadBalanced
    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository=departmentRepository;

    }

    public List<Department> getAll() {
        List<Department> departmentsFound=departmentRepository.findAll();
        LOGGER.info("Departments found");
        return departmentsFound;
    }


    public Department getById(Long departmentId){
        Department departmentFound=departmentRepository.findById(departmentId).orElse(null);
        LOGGER.info("Department found with id={}: ", departmentId);
        return departmentFound;
    }

    public Department add(Department department){
        Department departmentSaved=departmentRepository.save(department);
        LOGGER.info("Department added with id={}", department.getId());
        return departmentSaved;
    }

    public List<Department> getByHospitalId(Long hospitalId){
        List<Department> departmentsFound=departmentRepository.findByHospitalId(hospitalId);
        LOGGER.info("Departments found for the hospital with an id={}", hospitalId);
        return departmentsFound;
    }

    private String patientClient="http://patient-service";          //The host address will be fetched from Eureka
    private static final String RESOURCE_PATH="/department/";
    private String REQUEST_URI=patientClient+RESOURCE_PATH;


    @HystrixCommand(fallbackMethod = "getByHospitalWithPatients_Fallback")
    public List<Department> getByHospitalWithPatients(Long hospitalId){
        List<Department> departmentList=departmentRepository.findByHospitalId(hospitalId);
        List<Patient> patientList;
        for (Department department:departmentList) {
            ResponseEntity<List<Patient>> responseEntity = restTemplate.
                    exchange(REQUEST_URI+department.getId(), HttpMethod.GET, null, new ParameterizedTypeReference<List<Patient>>() {});
            patientList = responseEntity.getBody();
            department.setPatientList(patientList);
        }

        return departmentList;
    }

    @SuppressWarnings("unused")
    private List<Department> getByHospitalWithPatients_Fallback(Long hospitalId){

        List<Department> departmentList=departmentRepository.findByHospitalId(hospitalId);
        List<Patient> patientListNotFound=new ArrayList<>();
        for (Department department:departmentList) {
            Patient patientNotFound=new Patient(0L, "UNKNOWN", "UNKNOWN", hospitalId, department.getId());
            patientListNotFound.add(patientNotFound);
            department.setPatientList(patientListNotFound);
        }

       return departmentList;
    }

}
