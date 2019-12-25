package com.paro.hospitalservice.controller;

import com.paro.hospitalservice.model.Hospital;
import com.paro.hospitalservice.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1")     //For Swagger UI: http://localhost:8090/swagger-ui.html
public class HospitalController {

    private final HospitalService hospitalService;

    @Autowired
    public HospitalController(HospitalService hospitalService){
        this.hospitalService=hospitalService;
    }

    @GetMapping(value = "/")
    public List<Hospital> getAll(){
        List<Hospital> hospitalsFound=hospitalService.getAll();
        return hospitalsFound;
    }
    @GetMapping(value = "/{id}")
    public Hospital getById(@PathVariable("id") Long hospitalId){
        Hospital hospitalFound=hospitalService.getById(hospitalId);
        return hospitalFound;
    }

    @PostMapping(value = "/")
    public Hospital add(@RequestBody Hospital hospital){
        Hospital hospitalAdded=hospitalService.add(hospital);
        return hospitalAdded;
    }

    @GetMapping(value = "/{id}/with-departments")
    public Hospital getHospitalWithDepartments(@PathVariable("id") Long hospitalId){
        Hospital hospital=hospitalService.getHospitalWithDepartments(hospitalId);
        return hospital;
    }
    @GetMapping(value = "/{id}/with-departments-and-patients")
    public Hospital getHospitalWithDepartmentsAndPatients(@PathVariable("id") Long hospitalId){
        Hospital hospital=hospitalService.getHospitalWithDepartmentsAndPatients(hospitalId);
        return hospital;
    }
    @GetMapping(value = "/{id}/with-patients")
    public Hospital getHospitalWithPatients(@PathVariable("id") Long hospitalId){
        Hospital hospital=hospitalService.getHospitalWithPatients(hospitalId);
        return hospital;
    }

}
