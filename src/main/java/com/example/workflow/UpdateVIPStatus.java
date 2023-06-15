package com.example.workflow;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class UpdateVIPStatus implements JavaDelegate {

    public void execute(DelegateExecution delegateExecution) throws Exception {

        // Get the customer number from the process instance
        int customerNumber = (int) delegateExecution.getVariable("customerNumber");

        // Create a connection to the H2 database
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./camunda-h2-database", "", "");
        try {
            // Set auto-commit to false to start a transaction
            conn.setAutoCommit(false);

            // Define the query to update VIP_STATUS and VIP_START_DATE columns in CUSTOMER_DETAILS table
            PreparedStatement stmt = conn.prepareStatement("UPDATE CUSTOMER_DETAILS SET VIP_STATUS = ?, VIP_START_DATE = ?, EXPIRY_DATE = ? WHERE CUSTOMER_NUMBER = ?");

            // Update VIP_STATUS column to true
            stmt.setBoolean(1, true);

            // Update VIP_START_DATE column to today's date
            LocalDate currentDate = LocalDate.now();
            String vipStartDateString = currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
            stmt.setString(2, vipStartDateString);

            // Update EXPIRY_DATE column to a date 365 days in the future
            LocalDate expiryDate = currentDate.plusDays(365);
            String expiryDateString = expiryDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
            stmt.setString(3, expiryDateString);

            // Set the customer number in the WHERE clause of the query
            stmt.setInt(4, customerNumber);

            // Execute the update statement
            int rowsAffected = stmt.executeUpdate();

            // Commit the transaction
            conn.commit();

            // Check if any rows were affected by the update statement
            if (rowsAffected == 0) {
                throw new RuntimeException("Customer number " + customerNumber + " not found in the database.");
            }

            // Close the statement
            stmt.close();

        } catch (SQLException e) {
            // Rollback the transaction if an error occurred
            conn.rollback();
            throw e;
        } finally {
            // Set auto-commit back to true
            conn.setAutoCommit(true);
            // Close the connection
            conn.close();
        }
    }
}