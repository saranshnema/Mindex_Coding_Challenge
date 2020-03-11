package com.mindex.challenge.utility.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.impl.ReportingStructureServiceImpl;
import com.mindex.challenge.utility.ReportingStructureUtilityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ReportingStructureUtilityImpl implements ReportingStructureUtilityService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureUtilityImpl.class);

    @Autowired
    private EmployeeService employeeService;

    @Override
    public int getNumberOfReports(Employee employee) {
        LOG.debug("Getting report count for employee with id [{}]", employee.getEmployeeId());
        List<Employee> directReports = employee.getDirectReports();
        if (directReports == null){
            return 0;
        }
        int numberOfReports = directReports.size();

        for(Employee reportees: directReports){
            Employee currentReportee = employeeService.read(reportees.getEmployeeId());
            numberOfReports += getNumberOfReports(currentReportee);
        }
        return numberOfReports;
    }
}
