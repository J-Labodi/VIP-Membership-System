package com.example.workflow;


import java.sql.*;
import java.time.LocalDate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class CancelVIPmembership implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {

        // Get the customer number from the process instance
        String customerNumber = (String) execution.getVariable("customerNumber");

        // Create a connection to the H2 database
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./camunda-h2-database", "", "");

        // Define the query to retrieve the VIP_STATUS and EXPIRY_DATE for a given CUSTOMER_NUMBER
        PreparedStatement stmt = conn.prepareStatement("SELECT VIP_STATUS, EXPIRY_DATE FROM CUSTOMER_DETAILS WHERE CUSTOMER_NUMBER = ?");
        stmt.setString(1, customerNumber);

        // Execute the query and retrieve the result set
        ResultSet rs = stmt.executeQuery();

        // Check if the result set contains any rows
        if (rs.next()) {
            // If the result set contains a row, retrieve the VIP_STATUS and EXPIRY_DATE values
            boolean vipStatus = rs.getBoolean("VIP_STATUS");
            String expiryDateStr = rs.getString("EXPIRY_DATE");

            // Set the VIP_STATUS value as false and the EXPIRY_DATE value as today's date
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE CUSTOMER_DETAILS SET VIP_STATUS = ?, EXPIRY_DATE = ? WHERE CUSTOMER_NUMBER = ?");
            updateStmt.setBoolean(1, false);
            updateStmt.setString(2, LocalDate.now().toString());
            updateStmt.setString(3, customerNumber);
            updateStmt.executeUpdate();

            // Set the 30Days value as a process variable
            execution.setVariable("cancelledVIPMembership", true);

        } else {
            // If the result set does not contain any rows, the customer number does not exist in the table
            // Handle the error or return a default value as needed
            throw new RuntimeException("Customer number " + customerNumber + " not found in the database.");
        }

        // Close the result set, statement, and connection
        rs.close();
        stmt.close();
        conn.close();
    }

}