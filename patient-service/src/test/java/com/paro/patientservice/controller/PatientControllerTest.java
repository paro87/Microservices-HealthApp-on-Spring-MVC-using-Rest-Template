package com.paro.patientservice.controller;

import com.paro.patientservice.model.Patient;
import com.paro.patientservice.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
class PatientControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PatientService patientService;

    private Patient patient1;
    private Patient patient2;
    private List<Patient> patientList;

    @BeforeEach
    void setUp() {
        patient1=new Patient(1L, "John", "Adams", 2L, 23L);
        patient2=new Patient(2L, "Mary", "James", 1L, 7L);
        patientList= new ArrayList<>();
        patientList.add(patient1);
        patientList.add(patient2);
    }

    @Test
    void getAll() throws Exception {
        given(patientService.getAll()).willReturn(patientList);
        ResultMatcher expected = jsonPath("$[0].id").value(patient1.getId());  //To get memmber of an array
        mockMvc.perform(get("/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))   //To count the size of an array
                .andExpect(expected);
        verify(patientService).getAll();                           //Verifying if the mockBean was called

//        when(patientService.getAll()).thenReturn(patientList);
//        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))
//                .andReturn();
//        System.out.println(mvcResult.getResponse().getContentAsString());
//        verify(patientService).getAll();
    }

    @Test
    void getById() throws Exception {
        given(patientService.getById((long) 1)).willReturn(patient1);
        ResultMatcher expected= jsonPath("$.id").value(patient1.getId()); //To get the member of object
        mockMvc.perform(get("/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(5))) //To count members of object
                .andExpect(expected);
        verify(patientService).getById((long)1);
    }

    @Test
    void add() throws Exception {
        given(patientService.add(ArgumentMatchers.any(Patient.class))).willReturn(patient1);
        String patientInJSON="{\"id\": 1,\"firstname\": \"John\",\"surname\": \"Adams\", \"hospitalId\": 2,\"departmentId\": 23}";
        mockMvc.perform(post("/")
                .content(patientInJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(5))) //To count members of object
                .andExpect(jsonPath("$.firstname").value(patient1.getFirstname()));

        verify(patientService).add(ArgumentMatchers.any(Patient.class));
    }

    @Test
    void getByDepartment() throws Exception {
        given(patientService.getByDepartmentId(23L)).willReturn(patientList);
        ResultMatcher expected = jsonPath("$[0].id").value(patient1.getId());

        mockMvc.perform(get("/department/23")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))   //To count the size of an array
                .andExpect(expected);
        verify(patientService).getByDepartmentId(23L);

    }

    @Test
    void getByHospital() throws Exception {
        given(patientService.getByHospitalId(2L)).willReturn(patientList);
        ResultMatcher expected = jsonPath("$[0].id").value(patient1.getId());

        mockMvc.perform(get("/hospital/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))   //To count the size of an array
                .andExpect(expected);
        verify(patientService).getByHospitalId(2L);
    }
}