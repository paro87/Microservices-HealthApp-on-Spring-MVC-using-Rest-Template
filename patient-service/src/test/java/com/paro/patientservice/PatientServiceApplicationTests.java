package com.paro.patientservice;

import com.paro.patientservice.repository.PatientConfig;
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
@ContextConfiguration(classes = {PatientConfig.class}, loader = AnnotationConfigContextLoader.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PatientunittestdemoApplication.class)
@Transactional
@SpringBootTest(classes = {PatientServiceApplication.class, PatientConfig.class })
@DirtiesContext
//@SpringBootTest
class PatientServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
