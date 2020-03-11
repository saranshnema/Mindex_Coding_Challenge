package com.mindex.challenge.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Compensation {
    private Employee employee;
    private String employeeId;
    private int salary;
    private String effectiveDate;

    public Compensation() {
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        this.employeeId = this.employee.getEmployeeId();
    }

    @JsonIgnore
    public String getEmployeeId() { return employeeId; }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) throws ParseException {

        this.effectiveDate = formatDate(effectiveDate);
    }

    private String formatDate(String effectiveDate) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(effectiveDate);
        } catch (ParseException e) {
            throw new ParseException("This date cannot be parsed. Please enter a valid date-time string. Example - yyyy-MM-dd HH:mm:ss  (2020-03-10 23:59:59)", e.getErrorOffset());
        }

        return format.format(date);
    }

}
