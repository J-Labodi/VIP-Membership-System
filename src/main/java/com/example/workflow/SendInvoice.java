package com.example.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Named
public class SendInvoice implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception{
        // Get the relevant process variables
        String customerName = (String) execution.getVariable("confirmCustomerName");
        String customerNumber = (String) execution.getVariable("confirmCustomerNumber");
        String customerAddress = (String) execution.getVariable("confirmCustomerAddress");
        String customerPostcode = (String) execution.getVariable("confirmCustomerPostcode");
        String Item = (String) execution.getVariable("Item");
        double deliveryChargeValue = (double) execution.getVariable("deliveryCharge");
        String deliveryCharge = Double.toString(deliveryChargeValue);
        int orderNumberValue = (int) execution.getVariable("confirmOrderNumber");
        String orderNumber = Integer.toString(orderNumberValue);

        // Add the relevant process variables to the message payload
        execution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("receiveInvoice")
                // Set the list of variables
                .setVariable("confirmCustomerName", customerName)
                .setVariable("confirmCustomerNumber", customerNumber)
                .setVariable("confirmCustomerAddress", customerAddress)
                .setVariable("confirmCustomerPostcode", customerPostcode)
                .setVariable("Item", Item)
                .setVariable("deliveryCharge", deliveryCharge)
                .setVariable("confirmOrderNumber", orderNumber)
                .correlate();
    }

}
