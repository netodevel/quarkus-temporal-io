package com.accenture.temporalio.quarkus.it;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
@RegisterForReflection
public interface GreetingActivities {
    @ActivityMethod(name = "greet")
    String composeGreeting(String greeting, String name);
}