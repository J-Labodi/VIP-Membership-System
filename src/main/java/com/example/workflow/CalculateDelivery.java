package com.example.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.inject.Named;


@Named
public class CalculateDelivery implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        boolean nextDayDelivery = false;
        double deliveryCharge = 0.00;

        // Connect to H2 DB
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./camunda-h2-database", "", "");

        // Check whether next day delivery has been requested
        nextDayDelivery = (boolean) delegateExecution.getVariable("nextDayDelivery");

        // Access standard delivery charges from DELIVERY_CHARGES table based on next day requested
        if (nextDayDelivery == false) {
            // Prepare the SQL statement based on customer number
            PreparedStatement stmt = conn.prepareStatement("SELECT AMOUNT FROM DELIVERY_CHARGES WHERE TYPE = ?");
            stmt.setString(1, "Standard");

            // Access result
            ResultSet result = stmt.executeQuery();

            // Get delivery charge amount
            if (result.next()) {
                deliveryCharge = result.getDouble("AMOUNT");
            }

            result.close();
            stmt.close();

        } else{
            // Prepare the SQL statement based on customer number
            PreparedStatement stmt = conn.prepareStatement("SELECT AMOUNT FROM DELIVERY_CHARGES WHERE TYPE = ?");
            stmt.setString(1, "Next Day");

            // Access result
            ResultSet result = stmt.executeQuery();

            // Get delivery charge amount
            if (result.next()) {
                deliveryCharge = result.getDouble("AMOUNT");
            }

            result.close();
            stmt.close();
        }

        conn.close();

        // Initiate delivery charge as process variable
        delegateExecution.setVariable("deliveryCharge", deliveryCharge);

        System.out.println("Calculate delivery charge executed");

    }

}
