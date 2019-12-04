package com.paro.departmentservice.model;

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
public class Department {
    @Id
    @Column(name = "department_id")
    private Long id;
    @Column(name= "depatment_name")
    private String name;
    @Column(name = "hospital_id")
    private Long hospitalId;
    @Transient
    private List<Patient> patientList=new ArrayList<>();


    public Department(Long valueOf, String apple, Long valueOf1) {
    }
}
