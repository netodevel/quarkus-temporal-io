package com.accenture.temporalio.quarkus.it;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class QuarkusBean {

    public String sayHello() {
        return "hello world temporal.io with quarkus";
    }
}
