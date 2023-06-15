package com.example.workflow;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import javax.inject.Named;

@Named
public class PopulateForm implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        // Access the required input variables from the process instance
        Map<String, Object> variables = delegateTask.getVariables();
        String address = (String) variables.get("address");
        String postcode = (String) variables.get("postcode");

        Object customerNumberObj = variables.get("customerNumber");
        int customerNumber = (customerNumberObj == null) ? 0 : (int) customerNumberObj;

        // Error handling for invalid form submission
        if(customerNumber == 0){
            if (address == null || address.isEmpty() || postcode == null || postcode.isEmpty()) {
                // If either address or postcode is null or empty, then the form submission should be blocked
                throw new RuntimeException("Please enter both address line and postcode.");
            }
        }

        try (
                // Connect to H2 DB and prepare the SQL statement based on the input variables
                Connection conn = DriverManager.getConnection("jdbc:h2:file:./camunda-h2-database", "", "");
                PreparedStatement statement = prepareStatement(conn, customerNumber, address, postcode);
                // Execute the SQL statement and retrieve the results
                ResultSet resultSet = statement.executeQuery();
        ) {
            // If there is a result, populate the form fields
            if (resultSet.next()) {
                delegateTask.setVariable("confirmCustomerName", resultSet.getString("NAME"));
                delegateTask.setVariable("confirmCustomerNumber", resultSet.getString("CUSTOMER_NUMBER"));
                delegateTask.setVariable("confirmCustomerVIP", resultSet.getString("VIP_STATUS"));
                delegateTask.setVariable("confirmCustomerEmail", resultSet.getString("EMAIL"));
                delegateTask.setVariable("confirmCustomerAddress", resultSet.getString("ADDRESS"));
                delegateTask.setVariable("confirmCustomerPostcode", resultSet.getString("POSTCODE"));
                delegateTask.setVariable("ConfirmPhoneNumber", resultSet.getString("PHONE_NUMBER"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

