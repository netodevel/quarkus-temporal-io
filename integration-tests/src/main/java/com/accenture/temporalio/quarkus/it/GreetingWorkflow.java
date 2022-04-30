package com.accenture.temporalio.quarkus.it;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
@RegisterForReflection
public interface GreetingWorkflow {
    @WorkflowMethod
    String getGreeting(String name);
}
