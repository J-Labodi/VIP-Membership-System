package com.example.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Named;
@Named
public class CheckCustomerAndConestAndVIP implements JavaDelegate {

    private static final String DB_URL = "jdbc:h2:file:./camunda-h2-database";
    private static final String DB_USERNAME = "";
    private static final String DB_PASSWORD = "";

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        int customerNumber = (int) delegateExecution.getVariable("customerNumber");
        boolean consent = (boolean) delegateExecution.getVariable("consent");

        Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

        String sql = "SELECT VIP_STATUS, EMAIL,PHONE_NUMBER, NAME FROM CUSTOMER_DETAILS WHERE CUSTOMER_NUMBER = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, customerNumber);
        ResultSet rs = stmt.executeQuery();

        boolean vipStatus = false;
        String email = "";
        String name = "";
        String phoneNumber = "";
        if (rs.next()) {
            vipStatus = rs.getBoolean("VIP_STATUS");
            email = rs.getString("EMAIL");
            name = rs.getString("NAME");
            phoneNumber = rs.getString("PHONE_NUMBER");
        }


        String message = "";
        boolean currentVIP = false;

        if (consent == false){
            currentVIP = true;
            message = "Customer did not consent to the terms and conditions";
        } else if (vipStatus) {
            currentVIP = true;
            message = "Customer is already a VIP";
        }


        delegateExecution.setVariable("currentVIP", currentVIP);
        delegateExecution.setVariable("message", message);
        delegateExecution.setVariable("email", email);
        delegateExecution.setVariable("name", name);
        delegateExecution.setVariable("phoneNumber", phoneNumber);

        stmt.close();
        conn.close();
    }
}
