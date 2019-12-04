package com.paro.hospitalservice.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Hospital {
    @Id
    @Column(name = "hospital_id")
    private Long id;
    @Column(name = "hospital_name")
    private String name;
    @Column(name = "hospital_address")
    private String address;
    @Transient
    private List<Department> departmentList=new ArrayList<>();
    @Transient
    private List<Patient> patientList=new ArrayList<>();
}
