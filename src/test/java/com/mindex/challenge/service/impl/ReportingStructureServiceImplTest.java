package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.utility.ReportingStructureUtilityService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    private String reportingStructureUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        reportingStructureUrl = "http://localhost:" + port + "/reportingStructure/{id}";
    }

    @Test
    public void testRead() {
        // Read checks
        ReportingStructure readReportingStructure = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, "16a596ae-edd3-4847-99fe-c4518e82c86f").getBody();
        assertEquals(readReportingStructure.getNumberOfReports(), 4);
        assertEmployeeEquivalence(readReportingStructure);

        readReportingStructure = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, "b7839309-3348-463b-a7e3-5de1c168beb3").getBody();
        assertEquals(readReportingStructure.getNumberOfReports(), 0);

        readReportingStructure = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, "03aa1462-ffa9-4978-901b-7c001562cf6f").getBody();
        assertEquals(readReportingStructure.getNumberOfReports(), 2);

        readReportingStructure = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, "62c1084e-6e34-4630-93fd-9153afb65309").getBody();
        assertEquals(readReportingStructure.getNumberOfReports(), 0);

        readReportingStructure = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, "c0c2293d-16bd-4603-8e08-638a9d18b22c").getBody();
        assertEquals(readReportingStructure.getNumberOfReports(), 0);

    }

    private static void assertEmployeeEquivalence(ReportingStructure expected) {
        assertEquals(expected.getEmployee().getFirstName(), "John");
        assertEquals(expected.getEmployee().getLastName(), "Lennon");
        assertEquals(expected.getEmployee().getDepartment(), "Engineering");
        assertEquals(expected.getEmployee().getPosition(), "Development Manager");
    }
}
