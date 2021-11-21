package com.accenture.temporalio.quarkus.it;

import com.accenture.temporalio.quarkus.runtime.annotation.Activity;

import javax.inject.Singleton;

@Activity
@Singleton
public class GreetingActivitiesImpl implements GreetingActivities {

    @Override
    public String composeGreeting(String greeting, String name) {
        return greeting + " " + name + "!";
    }

}
