package com.accenture.temporalio.quarkus.it;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface GreetingWorkflow {
    @WorkflowMethod
    String getGreeting(String name);
}
