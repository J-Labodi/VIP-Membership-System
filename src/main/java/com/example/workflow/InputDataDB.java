package com.example.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.inject.Named;

@Named
public class InputDataDB implements JavaDelegate {


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String item = (String) delegateExecution.getVariable("Item");
        int customerNumber = (int) delegateExecution.getVariable("customerNumber");

        // Create a connection to the H2 database
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./camunda-h2-database", "", "");

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO ORDER_DETAILS (CUSTOMER_NUMBER, ITEM) VALUES (?,?)");
        stmt.setInt(1, customerNumber);
        stmt.setString(2, item);
        stmt.executeUpdate();

        // Close the database connection
        stmt.close();
        conn.close();


    }
}
