package com.example.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.inject.Named;

@Named
public class ApplyDiscount implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        // Get previously assigned delivery charge
        double deliveryCharge = (double) delegateExecution.getVariable("deliveryCharge");

        // if the customer is VIP - Overwrite delivery charge
        if ((boolean) delegateExecution.getVariable("customerVIP") == true) {
            // connect to H2 DB
            Connection conn = DriverManager.getConnection("jdbc:h2:file:./camunda-h2-database", "", "");

            if ((boolean) delegateExecution.getVariable("nextDayDelivery") == false) {
                // Prepare the SQL statement based on customer number
                PreparedStatement stmt = conn.prepareStatement("SELECT AMOUNT FROM DELIVERY_CHARGES_VIP WHERE TYPE = ?");
                stmt.setString(1, "Standard");

                // Access result
                ResultSet result = stmt.executeQuery();

                // Get delivery charge amount
                if (result.next()) {
                    deliveryCharge = result.getDouble("AMOUNT");
                }

                result.close();
                stmt.close();

            } else {
                // Prepare the SQL statement based on customer number
                PreparedStatement stmt = conn.prepareStatement("SELECT AMOUNT FROM DELIVERY_CHARGES_VIP WHERE TYPE = ?");
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

            // Update the amount stored in deliveryCharge process variable
            delegateExecution.setVariable("deliveryCharge", deliveryCharge);

        }

        System.out.println("Apply discount on delivery executed");
    }


}