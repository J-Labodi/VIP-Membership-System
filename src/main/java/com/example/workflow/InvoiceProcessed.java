package com.example.workflow;

import camundajar.impl.scala.util.control.Exception;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Named
public class InvoiceProcessed implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        // Send invoiceProcessed message
        execution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("invoiceProcessed")
                .correlate();

        System.out.println("Customer invoice processed");
    }

}
