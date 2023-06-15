package com.example.workflow;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Named
public class SendCustomerVipForm implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception{


        execution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("GetVIPform1")
                .correlate();
    }

}
