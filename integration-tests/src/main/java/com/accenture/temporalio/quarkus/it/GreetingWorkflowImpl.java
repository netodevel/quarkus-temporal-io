package com.accenture.temporalio.quarkus.it;

import java.time.Duration;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

/**
 * This class need a bean quarkus too?
 */
@com.accenture.temporalio.quarkus.runtime.annotation.Workflow(queue = "greeting-queue")
public class GreetingWorkflowImpl implements GreetingWorkflow {

    private final GreetingActivities activities = Workflow.newActivityStub(GreetingActivities.class,
            ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(2)).build());

    @Override
    public String getGreeting(String name) {
        return activities.composeGreeting("Hello", name);
    }
}