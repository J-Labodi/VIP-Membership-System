package com.example.workflow;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;


@Named
public class test implements JavaDelegate {
    public void execute(DelegateExecution execution) throws Exception {

        System.out.println("hey buddy");

    }

}