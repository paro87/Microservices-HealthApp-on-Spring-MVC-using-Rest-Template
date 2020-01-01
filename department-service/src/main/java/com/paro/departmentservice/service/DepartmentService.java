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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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


    public Department getById(Long departmentId) {
        Optional<Department> departmentFound=departmentRepository.findById(departmentId);
        if (departmentFound.isPresent()) {
            LOGGER.info("Department found with id={}", departmentId);
            return departmentFound.get();
        }
        return null;

    }
    // 2.way ResponseEntity- Returning HttpStatus.NOT_FOUND in case if the department with required id does not exist
/*
    public ResponseEntity<Department> getById(Long departmentId) {
        Optional<Department> departmentFound=departmentRepository.findById(departmentId);
        if (departmentFound.isPresent()) {
            LOGGER.info("Department found with id={}", departmentId);
            return new ResponseEntity<>(patientFound.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

    }
*/

    public Department add(Department department){
        Department departmentSaved=departmentRepository.save(department);
        LOGGER.info("Department added with id={}", department.getId());
        return departmentSaved;
    }

    public Department put(Department department) {
        Department departmentSaved= departmentRepository.save(department);
        LOGGER.info("Department put with id={}", department.getId());
        return departmentSaved;
    }

    public Department patch(Long departmentId, Department departmentToPatch) {
        Department departmentFound=departmentRepository.findById(departmentId).get();
        if (departmentToPatch.getId()!=null) {
            departmentFound.setId(departmentToPatch.getId());
        }
        if (departmentToPatch.getName()!=null) {
            departmentFound.setName(departmentToPatch.getName());
        }
        if (departmentToPatch.getHospitalId()!=null) {
            departmentFound.setHospitalId(departmentToPatch.getHospitalId());
        }
        //There’s no way of removing or adding a subset of items from a collection.
        //If the department wants to add or remove an entry from a collection, it must send the complete altered collection.
        if (departmentToPatch.getPatientList()!=null) {
            departmentFound.setPatientList(departmentToPatch.getPatientList());
        }

        Department departmentPatched= departmentRepository.save(departmentFound);
        LOGGER.info("Department patched with id={}", departmentFound.getId());
        return departmentPatched;
    }

    public void deleteById(Long departmentId) {
        try {
            departmentRepository.deleteById(departmentId);
            LOGGER.info("Department deleted with id={}", departmentId);
        } catch (EmptyResultDataAccessException e){}
    }

    public List<Department> getByHospitalId(Long hospitalId){
        List<Department> departmentsFound=departmentRepository.findByHospitalId(hospitalId);
        LOGGER.info("Departments found for the hospital with an id={}", hospitalId);
        return departmentsFound;
    }

    private String patientClient="http://patient-service";          //The host address will be fetched from Eureka
    private static final String RESOURCE_PATH="/service/department/";
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
