package com.paro.hospitalservice;

import com.paro.hospitalservice.repository.HospitalConfig;
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
@ContextConfiguration(classes = {HospitalConfig.class}, loader = AnnotationConfigContextLoader.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PatientunittestdemoApplication.class)
@Transactional
@SpringBootTest(classes = {HospitalServiceApplication.class, HospitalConfig.class })
@DirtiesContext
class HospitalServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
