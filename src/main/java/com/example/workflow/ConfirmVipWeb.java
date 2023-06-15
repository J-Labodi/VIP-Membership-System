package com.example.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.inject.Named;

@Named
public class ConfirmVipWeb implements ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) throws Exception {
        // Check whether the current process is a Web order
        boolean isAwebOrder = (Boolean) execution.getVariable("isAwebOrder");

        if (isAwebOrder == true) {
            String address = (String) execution.getVariable("address");
            String postcode = (String) execution.getVariable("postcode");
            int customerNumber = (Integer) execution.getVariable("customerNumber");

            try (
                    // Connect to H2 DB and prepare the SQL statement based on the input variables
                    Connection conn = DriverManager.getConnection("jdbc:h2:file:./camunda-h2-database", "", "");
                    PreparedStatement statement = prepareStatement(conn, customerNumber, address, postcode);
                    // Execute the SQL statement and retrieve the results
                    ResultSet resultSet = statement.executeQuery();
            ) {
                // If there is a result, initiate the appropriate process variables
                if (resultSet.next()) {
                    execution.setVariable("confirmCustomerName", resultSet.getString("NAME"));
                    execution.setVariable("confirmCustomerNumber", resultSet.getString("CUSTOMER_NUMBER"));
                    execution.setVariable("confirmCustomerVIP", resultSet.getString("VIP_STATUS"));
                    execution.setVariable("confirmCustomerEmail", resultSet.getString("EMAIL"));
                    execution.setVariable("confirmCustomerAddress", resultSet.getString("ADDRESS"));
                    execution.setVariable("confirmCustomerPostcode", resultSet.getString("POSTCODE"));
                    execution.setVariable("ConfirmPhoneNumber", resultSet.getString("PHONE_NUMBER"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Prepare SQL statement based on customer number or address&postcode submitted
    private PreparedStatement prepareStatement(Connection conn, int customerNumber, String address, String postcode) throws SQLException {
        PreparedStatement statement;
        if (customerNumber != 0) {
            statement = conn.prepareStatement("SELECT * FROM CUSTOMER_DETAILS WHERE CUSTOMER_NUMBER = ?");
            statement.setInt(1, customerNumber);
        } else {
            statement = conn.prepareStatement("SELECT * FROM CUSTOMER_DETAILS WHERE ADDRESS = ? AND POSTCODE = ?");
            statement.setString(1, address);
            statement.setString(2, postcode);
        }
        return statement;
    }

}

