package com.accenture.temporalio.quarkus.it;

import java.time.Duration;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

/**
 * This class need a bean quarkus too?
 *
 * NO INJECT A DI
 * docs:
 *  https://docs.temporal.io/docs/java/workflows/?_ga=2.132625166.1434419763.1650045760-1770726456.1650045760#workflow-implementation-constraints
 *  https://community.temporal.io/t/how-to-register-a-spring-boot-workflow-implementation/71
 *
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