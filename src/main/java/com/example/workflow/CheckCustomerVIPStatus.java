package com.example.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Named;
@Named
public class CheckCustomerVIPStatus implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        // Get the customer number from the process instance
        int customerNumber = (int) delegateExecution.getVariable("customerNumber");

        // Create a connection to the H2 database
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./camunda-h2-database", "", "");

        // Define the query to retrieve the VIP_START_DATE for a given CUSTOMER_NUMBER
        PreparedStatement stmt = conn.prepareStatement("SELECT VIP_START_DATE FROM CUSTOMER_DETAILS WHERE CUSTOMER_NUMBER = ?");
        stmt.setInt(1, customerNumber);

        // Execute the query and retrieve the result set
        ResultSet rs = stmt.executeQuery();

        // Check if the result set contains any rows
        if (rs.next()) {
            // If the result set contains a row, retrieve the VIP_START_DATE value
            String vipStartDateStr = rs.getString("VIP_START_DATE");
            // Set the VIP_START_DATE value as a process variable
            delegateExecution.setVariable("vipStartDate", vipStartDateStr);

            // Check if VIP_START_DATE is null
            if (vipStartDateStr == null) {
                // If VIP_START_DATE is null, set currentOrPastCust to false and return
                delegateExecution.setVariable("currentOrPastCust", false);
                delegateExecution.setVariable("TryingToBuyVIP", false); // set TryingToBuyVIP to false as well
                System.out.println("currentOrPastCust == false");
                return;
            }

            // Convert the VIP_START_DATE string to a Date object
            Date vipStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(vipStartDateStr);

            // Get the current date
            Date currentDate = new Date();

            // Calculate the difference between the current date and VIP_START_DATE in months
            int monthsDifference = (int) ((currentDate.getTime() - vipStartDate.getTime()) / (1000 * 60 * 60 * 24 * 30));

            // Set the currentOrPastCust process variable based on the number of months difference
            boolean currentOrPastCust = monthsDifference <= 6;
            delegateExecution.setVariable("currentOrPastCust", currentOrPastCust);
            delegateExecution.setVariable("TryingToBuyVIP", true); // set TryingToBuyVIP to true since the customer is a current or past VIP customer
            System.out.println("currentOrPastCust == true");
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
