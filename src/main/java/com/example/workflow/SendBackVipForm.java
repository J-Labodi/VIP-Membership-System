package com.example.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

@Named
public class SendBackVipForm implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception{

        int customerNumber = (int) execution.getVariable("customerNumber");
        String email = (String) execution.getVariable("email");
        Boolean consent = (Boolean) execution.getVariable("consent");

        Map<String, Object> formData = new HashMap<>();
        formData.put("customerNumber", customerNumber);
        formData.put("email", email);
        formData.put("consent", consent);

        execution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("GetFinishedVIPform")
                .setVariables(formData)
                .correlate();
    }
}
