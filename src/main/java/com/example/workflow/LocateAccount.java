package com.example.workflow;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.inject.Named;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Named
public class LocateAccount implements JavaDelegate{

    private JdbcTemplate jdbcTemplate;

    public void execute(DelegateExecution execution) throws Exception {

        // Get the customerNumber variable from the process instance
        int customerNumber = (int) execution.getVariable("customerNumber");

        // Get the process engine object
        ProcessEngine processEngine = execution.getProcessEngine();

        // Get the dataSource from the process engine object
        DataSource dataSource = processEngine.getProcessEngineConfiguration().getDataSource();

        // Create a JdbcTemplate using the dataSource
        jdbcTemplate = new JdbcTemplate(dataSource);

        // Query the database for customer details
        String sql = "SELECT NAME, EMAIL, ADDRESS, POSTCODE, PHONE_NUMBER FROM CUSTOMER_DETAILS WHERE CUSTOMER_NUMBER = ?";
        Map<String, Object> result = jdbcTemplate.queryForMap(sql, customerNumber);

        // Extract the customer details from the query result
        String customerName = (String) result.get("NAME");
        String email = (String) result.get("EMAIL");
        String address = (String) result.get("ADDRESS");
        String postcode = (String) result.get("POSTCODE");
        String phoneNumber = (String) result.get("PHONE_NUMBER");

        // Create a Map to store the customer details as process variables
        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("postcode", postcode);
        variables.put("phoneNumber", phoneNumber);
        variables.put("email", email);
        variables.put("address", address);

        System.out.println(customerName);
        System.out.println(email);
        System.out.println(address);
        System.out.println(postcode);
        System.out.println(phoneNumber);

        // Set the process variables for the next user task
        execution.setVariables(variables);
    }

}
