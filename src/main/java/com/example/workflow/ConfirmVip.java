package com.example.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.inject.Named;

@Named
public class ConfirmVip implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        int customerNumber;
        String address;
        String postcode;
        boolean vipStatus = false;

        // Connect to H2 DB
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./camunda-h2-database", "", "");

        // Obtain customer number if provided
        if ((int)delegateExecution.getVariable("customerNumber") != 0) {
            customerNumber = (int) delegateExecution.getVariable("customerNumber");

            // Prepare the SQL statement based on customer number
            PreparedStatement stmt = conn.prepareStatement("SELECT VIP_STATUS FROM CUSTOMER_DETAILS WHERE CUSTOMER_NUMBER = ?");
            stmt.setInt(1, customerNumber);

            // Access result
            ResultSet result = stmt.executeQuery();

            // Get customer's VIP status
            if(result.next()){
                vipStatus = result.getBoolean("VIP_STATUS");
            }

            result.close();
            stmt.close();
        }

        // Obtain address&postcode if customer number is not provided
        else if (delegateExecution.getVariable("address") != null && delegateExecution.getVariable("postcode") != null) {
            address = (String) delegateExecution.getVariable("address");
            postcode = (String) delegateExecution.getVariable("postcode");

            // Prepare the SQL statement based on address&postcode
            PreparedStatement stmt = conn.prepareStatement("SELECT VIP_STATUS, CUSTOMER_NUMBER FROM CUSTOMER_DETAILS WHERE ADDRESS = ? AND POSTCODE = ?");
            stmt.setString(1, address);
            stmt.setString(2, postcode);

            // Access result
            ResultSet result = stmt.executeQuery();

            // Get customer's VIP status and customer number, initiate customer number process variable
            if (result.next()) {
                vipStatus = result.getBoolean("VIP_STATUS");
                customerNumber = result.getInt("CUSTOMER_NUMBER");
                delegateExecution.setVariable("customerNumber", customerNumber);
            }

            result.close();
            stmt.close();
        }

        conn.close();

        // Initiate customer's VIP status as process variable
        delegateExecution.setVariable("customerVIP", vipStatus);

        System.out.println("Confirm VIP status executed");

    }
}
