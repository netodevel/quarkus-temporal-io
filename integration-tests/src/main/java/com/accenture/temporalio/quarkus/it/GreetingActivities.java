package com.accenture.temporalio.quarkus.it;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface GreetingActivities {
    @ActivityMethod(name = "greet")
    String composeGreeting(String greeting, String name);
}