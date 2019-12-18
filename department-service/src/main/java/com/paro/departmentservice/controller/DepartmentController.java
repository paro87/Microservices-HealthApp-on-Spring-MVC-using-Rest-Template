package com.paro.departmentservice.controller;
import com.paro.departmentservice.model.Department;
import com.paro.departmentservice.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")     //For Swagger UI: http://localhost:8091/swagger-ui.html
public class DepartmentController {
    private DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService){
        this.departmentService=departmentService;
    }

    @GetMapping(value = "/")
    public List<Department> getAll() {
        List<Department> departmentList=departmentService.getAll();
        return departmentList;
    }

    @GetMapping(value = "/{id}")
    public Department getById(@PathVariable("id") Long departmentId){
        Department department=departmentService.getById(departmentId);
        return department;
    }
    @PostMapping(value = "/")
    public Department add(@RequestBody Department department){
        Department departmentAdd=departmentService.add(department);
        return departmentAdd;
    }
    @GetMapping(value = "/hospital/{hospitalId}")
    public List<Department> getByHospital(@PathVariable("hospitalId") Long hospitalId){
        List<Department> departmentList=departmentService.getByHospitalId(hospitalId);
        return departmentList;
    }
    @GetMapping(value = "/hospital/{hospitalId}/with-patients")
    public List<Department> getByHospitalWithPatients(@PathVariable("hospitalId") Long hospitalId){
        List<Department> departmentList=departmentService.getByHospitalWithPatients(hospitalId);
        return departmentList;
    }
}
