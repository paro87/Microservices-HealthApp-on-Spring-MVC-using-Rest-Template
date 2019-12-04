package com.paro.departmentservice.controller;

import com.paro.departmentservice.model.Department;
import com.paro.departmentservice.model.Patient;
import com.paro.departmentservice.service.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepartmentController.class)
class DepartmentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DepartmentService departmentService;

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
    void getAll() throws Exception {
        given(departmentService.getAll()).willReturn(departmentList);
        ResultMatcher expected = jsonPath("$[0].id").value(department1.getId());  //To get memmber of an array

        mockMvc.perform(get("/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))   //To count the size of an array
                .andExpect(expected);
        verify(departmentService).getAll();
    }

    @Test
    void getById() throws Exception {
        given(departmentService.getById((long) 11)).willReturn(department1);
        ResultMatcher expected= jsonPath("$.id").value(department1.getId()); //To get the member of object

        mockMvc.perform(get("/11")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(4))) //To count members of object
                .andExpect(expected);
        verify(departmentService).getById((long)11);
    }

    @Test
    void add() throws Exception {
        given(departmentService.add(any(Department.class))).willReturn(department1);

        String departmentInJSON="{\"id\":11,\"name\":\"Cardiology\",\"hospitalId\":1,\"patientList\":null}";

        mockMvc.perform(post("/")
                .content(departmentInJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(4))) //To count members of object
                .andExpect(jsonPath("$.name").value(department1.getName()));
        verify(departmentService).add(any(Department.class));
    }

    @Test
    void getByHospital() throws Exception {
        given(departmentService.getByHospitalId(1L)).willReturn(departmentList);
        ResultMatcher expected = jsonPath("$[0].id").value(department1.getId());

        mockMvc.perform(get("/hospital/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))   //To count the size of an array
                .andExpect(expected);
        verify(departmentService).getByHospitalId(1L);
    }

    @Test
    void getByHospitalWithPatients() throws Exception {
        given(departmentService.getByHospitalWithPatients(1L)).willReturn(departmentList);
        ResultMatcher expected = jsonPath("$[0].patientList[1].surname").value("James");

        mockMvc.perform(get("/hospital/1/with-patients")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))   //To count the size of an array
                .andExpect(jsonPath("$[0].id").value(department1.getId()))
                .andExpect(jsonPath("$[0].patientList[0].id").value(1L))
                .andExpect(jsonPath("$[0].patientList[0].firstname").value("John"))
                .andExpect(expected);
        verify(departmentService).getByHospitalWithPatients(1L);

    }
}