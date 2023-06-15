package com.example.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.inject.Named;

@Named
public class UpdateOrder implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        // Access the relevant process variables
        String item = (String) delegateExecution.getVariable("Item");
        int customerNumber = (int) delegateExecution.getVariable("customerNumber");
        double deliveryCharge = (double) delegateExecution.getVariable("deliveryCharge");

        // Connect to H2 DB
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./camunda-h2-database", "", "");

        // Insert order details to DB
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO ORDER_DETAILS (CUSTOMER_NUMBER, ITEM, DELIVERY_CHARGE) VALUES (?,?,?)");
        stmt.setInt(1, customerNumber);
        stmt.setString(2, item);
        stmt.setDouble(3, deliveryCharge);

        stmt.executeUpdate();

        stmt.close();

        // Prepare SQL statement to access order number
        PreparedStatement stmt2 = conn.prepareStatement("SELECT MAX(ORDER_NUMBER) FROM ORDER_DETAILS WHERE CUSTOMER_NUMBER = ?");
        stmt2.setInt(1, customerNumber);

        // Access result
        ResultSet result = stmt2.executeQuery();

        // Get order number and set it as a process variable
        if (result.next()) {
            int orderNumber = result.getInt("MAX(ORDER_NUMBER)");
            delegateExecution.setVariable("confirmOrderNumber", orderNumber);
        }

        stmt2.close();
        conn.close();

        System.out.println("Update order status executed");
    }
}