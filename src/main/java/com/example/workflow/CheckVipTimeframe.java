package com.example.workflow;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class CheckVipTimeframe implements JavaDelegate {


    public void execute(DelegateExecution execution) throws Exception {

        // Get the customer number from the process instance
        String customerNumber = (String) execution.getVariable("customerNumber");

        // Initialize tryingToBuyVIP with the value of TryingToBuyVIP process variable, defaulting to false if it's null
        Boolean TryingToBuyVIP = (Boolean) execution.getVariable("TryingToBuyVIP");
        boolean tryingToBuyVIP = TryingToBuyVIP != null ? TryingToBuyVIP : false;

        // Create a connection to the H2 database
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./camunda-h2-database", "", "");

        // Define the query to retrieve the VIP_START_DATE for a given CUSTOMER_NUMBER
        PreparedStatement stmt = conn.prepareStatement("SELECT VIP_START_DATE FROM CUSTOMER_DETAILS WHERE CUSTOMER_NUMBER = ?");
        stmt.setString(1, customerNumber);

        // Execute the query and retrieve the result set
        ResultSet rs = stmt.executeQuery();

        // Check if the result set contains any rows
        LocalDate vipStartDate = null;
        if (rs.next()) {
            // If the result set contains a row, retrieve the VIP_START_DATE value
            vipStartDate = rs.getObject("VIP_START_DATE", LocalDate.class);
        } else {
            // If the result set does not contain any rows, the customer number does not exist in the table
            // Handle the error or return a default value as needed
            throw new RuntimeException("Customer number " + customerNumber + " not found in the database.");
        }

        // Close the result set, statement, and connection
        rs.close();
        stmt.close();
        conn.close();

        // Check if vipStartDate is not null and 30 days have passed since it was set
        System.out.println("vipStartDate: " + vipStartDate);
        boolean thirtyDays = false;
        if (vipStartDate != null) {
            LocalDate currentDate = LocalDate.now();
            LocalDate thirtyDaysAfterVipStartDate = vipStartDate.plusDays(30);
            thirtyDays = currentDate.isBefore(thirtyDaysAfterVipStartDate);
        }
        System.out.println(thirtyDays);

        // If TryingToBuyVIP is true, set thirtyDays to true as well
        if (tryingToBuyVIP) {
            thirtyDays = true;
        }

        // Set the thirtyDays variable as a process variable
        execution.setVariable("thirtyDays", thirtyDays);
    }

}
