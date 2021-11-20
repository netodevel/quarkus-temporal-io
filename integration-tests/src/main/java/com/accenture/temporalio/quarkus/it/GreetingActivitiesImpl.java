package com.accenture.temporalio.quarkus.it;

import com.accenture.temporalio.quarkus.runtime.annotation.Activity;

@Activity
public class GreetingActivitiesImpl implements GreetingActivities {

    @Override
    public String composeGreeting(String greeting, String name) {
        return name;
    }

}
