package com.accenture.temporalio.quarkus.it;

import com.accenture.temporalio.quarkus.runtime.annotation.Activity;
import io.quarkus.arc.Unremovable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;

@Activity
@ApplicationScoped
@Unremovable //TODO: make this more automatically
public class GreetingActivitiesImpl implements GreetingActivities {

    /*
       This is a sample activity injecting a quarkus bean;
     */
    @Inject
    QuarkusBean quarkusBean;

    @Override
    public String composeGreeting(String greeting, String name) {
        return quarkusBean.sayHello();
    }
}