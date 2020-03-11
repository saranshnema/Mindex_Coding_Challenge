package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import com.mindex.challenge.utility.ReportingStructureUtilityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ReportingStructureUtilityService reportingStructureUtilityService;



    @Override
    public ReportingStructure read(String id) {
        LOG.debug("Creating reporting Structure with id [{}]", id);

        Employee employee = employeeService.read(id);

        ReportingStructure reportingStructure = new ReportingStructure();
        int numberOfReports = reportingStructureUtilityService.getNumberOfReports(employee);

        reportingStructure.setEmployee(employee);
        reportingStructure.setNumberOfReports(numberOfReports);

        return reportingStructure;
    }
}
