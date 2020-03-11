package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String compensationUrl;
    private String compensationIdUrl;

    @Autowired
    private CompensationService compensationService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationIdUrl = "http://localhost:" + port + "/compensation/employee/{id}";
    }

    @Test
    public void testCreateRead() {
        Employee testEmployee = new Employee();
        testEmployee.setEmployeeId("123456789");
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        Compensation testCompensation = new Compensation();
        testCompensation.setEmployee(testEmployee);
        testCompensation.setSalary(100000);
        try {
            testCompensation.setEffectiveDate("2020-03-10 23:59:59");
        } catch (ParseException e) {
            // This wont be called now, so doing nothing here
            e.printStackTrace();
        }

        // Create checks
        Compensation createdCompensation = restTemplate.postForEntity(compensationUrl, testCompensation, Compensation.class).getBody();
        assertNotNull(createdCompensation.getEmployeeId());
        assertEquals(createdCompensation.getSalary(), testCompensation.getSalary());
        assertEquals(createdCompensation.getEffectiveDate(), testCompensation.getEffectiveDate());
        assertEmployeeEquivalence(testEmployee, createdCompensation.getEmployee());


        // Read checks
        Compensation[] readCompensation = restTemplate.getForEntity(compensationIdUrl, Compensation[].class, createdCompensation.getEmployeeId()).getBody();
        assertEquals(createdCompensation.getEmployeeId(), readCompensation[0].getEmployeeId());
        assertEmployeeEquivalence(testEmployee, readCompensation[0].getEmployee());

        // Check incorrect date format

        try {
            testCompensation.setEffectiveDate("abc");
        } catch (ParseException e) {
            // This wont be called now, so doing nothing here
            assertEquals(e.getMessage(), "This date cannot be parsed. Please enter a valid date-time string. Example - yyyy-MM-dd HH:mm:ss  (2020-03-10 23:59:59)");
        }

        // Check for list of compensations
        testCompensation.setSalary(100001);
        restTemplate.postForEntity(compensationUrl, testCompensation, Compensation.class).getBody();
        readCompensation = restTemplate.getForEntity(compensationIdUrl, Compensation[].class, createdCompensation.getEmployeeId()).getBody();
        assertEquals(readCompensation.length, 2);
        for (Compensation c : readCompensation){
            assertEmployeeEquivalence(testEmployee, c.getEmployee());
        }
        assertEquals(readCompensation[0].getSalary(), 100000);
        assertEquals(readCompensation[1].getSalary(), 100001);

    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
