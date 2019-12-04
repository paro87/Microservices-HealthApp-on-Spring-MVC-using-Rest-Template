package com.paro.departmentservice;

import com.paro.departmentservice.repository.DepartmentConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

@PropertySource("classpath:application-test.properties")
@ActiveProfiles("test")
@ContextConfiguration(classes = {DepartmentConfig.class}, loader = AnnotationConfigContextLoader.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PatientunittestdemoApplication.class)
@Transactional
@SpringBootTest(classes = {DepartmentServiceApplication.class, DepartmentConfig.class })
@DirtiesContext
class DepartmentServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
