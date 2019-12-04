package com.paro.hospitalservice.controller;

import com.paro.hospitalservice.model.Department;
import com.paro.hospitalservice.model.Hospital;
import com.paro.hospitalservice.model.Patient;
import com.paro.hospitalservice.service.HospitalService;
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

@WebMvcTest(HospitalController.class)
class HospitalControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private HospitalService hospitalService;

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
        hospital1=new Hospital(1L, "Johns Hopkins", "US", null, null);
        hospital2=new Hospital(2L, "Charité", "Germany", null, null);
        hospitalList=new ArrayList<>();
        hospitalList.add(hospital1);
        hospitalList.add(hospital2);

        department1=new Department(11L, "Cardiology", 1L, null);
        department2=new Department(12L, "Neurology", 1L, null);
        departmentList= new ArrayList<>();
        departmentList.add(department1);
        departmentList.add(department2);

        patient1=new Patient(1L, "John", "Adams", 1L, 11L);
        patient2=new Patient(2L, "Mary", "James", 1L, 11L);
        patientList=new ArrayList<>();
        patientList.add(patient1);
        patientList.add(patient2);

        department1.setPatientList(patientList);
        hospital1.setDepartmentList(departmentList);
        hospital1.setPatientList(patientList);


    }

    @Test
    void getAll() throws Exception {
        given(hospitalService.getAll()).willReturn(hospitalList);
        ResultMatcher expected = jsonPath("$[0].id").value(hospital1.getId());  //To get memmber of an array

        mockMvc.perform(get("/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))   //To count the size of an array
                .andExpect(expected);
        verify(hospitalService).getAll();
    }

    @Test
    void getById() throws Exception {
        given(hospitalService.getById((long) 1)).willReturn(hospital1);
        ResultMatcher expected= jsonPath("$.id").value(hospital1.getId()); //To get the member of object

        mockMvc.perform(get("/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(5))) //To count members of object
                .andExpect(expected);
        verify(hospitalService).getById((long)1);
    }

    @Test
    void add() throws Exception {
        given(hospitalService.add(any(Hospital.class))).willReturn(hospital1);
        String patientInJSON="{\"id\": 1,\"name\": \"Johns Hopkins\",\"address\": \"US\", \"departmentList\":null,\"patientList\":null}";
        mockMvc.perform(post("/")
                .content(patientInJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(5))) //To count members of object
                .andExpect(jsonPath("$.name").value(hospital1.getName()));

        verify(hospitalService).add(any(Hospital.class));
    }

    @Test
    void getHospitalWithDepartments() throws Exception {
        given(hospitalService.getHospitalWithDepartments(1L)).willReturn(hospital1);
        ResultMatcher expected = jsonPath("$.departmentList[1].name").value(department2.getName());
        mockMvc.perform(get("/1/with-departments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(hospital1.getId()))
                .andExpect(jsonPath("$.*", hasSize(5))) //To count members of object
                .andExpect(jsonPath("$.departmentList[0].id").value(department1.getId()))
                .andExpect(expected);
        verify(hospitalService).getHospitalWithDepartments(1L);
    }

    @Test
    void getHospitalWithDepartmentsAndPatients() throws Exception {
        given(hospitalService.getHospitalWithDepartmentsAndPatients(1L)).willReturn(hospital1);
        ResultMatcher expected = jsonPath("$.departmentList[0].patientList[1].id").value(patient2.getId());
        mockMvc.perform(get("/1/with-departments-and-patients")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(hospital1.getId()))
                .andExpect(jsonPath("$.*", hasSize(5))) //To count members of object
                .andExpect(jsonPath("$.departmentList[0].id").value(department1.getId()))
                .andExpect(jsonPath("$.departmentList[1].name").value(department2.getName()))
                .andExpect(jsonPath("$.departmentList[0].patientList[0].firstname").value(patient1.getFirstname()))
                .andExpect(expected);
        verify(hospitalService).getHospitalWithDepartmentsAndPatients(1L);
    }

    @Test
    void getHospitalWithPatients() throws Exception {
        given(hospitalService.getHospitalWithPatients(1L)).willReturn(hospital1);
        ResultMatcher expected = jsonPath("$.patientList[1].firstname").value(patient2.getFirstname());
        mockMvc.perform(get("/1/with-patients")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(hospital1.getId()))
                .andExpect(jsonPath("$.*", hasSize(5))) //To count members of object
                .andExpect(jsonPath("$.patientList[0].id").value(patient1.getId()))
                .andExpect(expected);
        verify(hospitalService).getHospitalWithPatients(1L);
    }
}
