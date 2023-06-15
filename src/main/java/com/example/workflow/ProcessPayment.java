package com.example.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Named
public class ProcessPayment implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution){

        System.out.println("Process payment executed");
    }
}