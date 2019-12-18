package com.paro.patientservice.controller;

import com.paro.patientservice.model.Patient;
import com.paro.patientservice.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1")     //For Swagger UI: http://localhost:8092/swagger-ui.html
public class PatientController {

    private PatientService patientService;
    @Autowired
    public PatientController(PatientService patientService){
        this.patientService=patientService;
    }

    @GetMapping(value = "/")
    public List<Patient> getAll(){
        List<Patient> patientsFound=patientService.getAll();
        return patientsFound;
    }

    @GetMapping(value = "/{id}")
    public Patient getById(@PathVariable("id") Long patientId){
        Patient patientFound=patientService.getById(patientId);
        return patientFound;
    }

    @PostMapping(value = "/")
    public Patient add(@RequestBody Patient patient){
        //OLD
        //patientService.add(patient);
        //Patient patientAdded= patientService.getById(patient.getId());

        //NEW
        Patient patientAdded=patientService.add(patient);
        return patientAdded;
    }

    @GetMapping(value = "/department/{departmentId}")
    public List<Patient> getByDepartment(@PathVariable("departmentId") Long departmentId){
        List<Patient> patientsFound=patientService.getByDepartmentId(departmentId);
        return patientsFound;
    }

    @GetMapping(value="/hospital/{hospitalId}")
    public List<Patient> getByHospital(@PathVariable("hospitalId") Long hospitalId) {
        List<Patient> patientsFound=patientService.getByHospitalId(hospitalId);
        return patientsFound;
    }
}
