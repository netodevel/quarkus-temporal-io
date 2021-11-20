package com.accenture.temporalio.quarkus.runtime.metadata;

import java.util.ArrayList;
import java.util.List;

public final class TemporalBuildItem {

    public List<String> listOfActivities = new ArrayList<>();
    public List<String> listOfWorkflows = new ArrayList<>();

    public TemporalBuildItem() {
    }

    public void putActivity(String clazz) {
        listOfActivities.add(clazz);
    }

    public void putWorkflow(String clazz) {
        listOfWorkflows.add(clazz);
    }

    public List<String> getListOfActivities() {
        return listOfActivities;
    }

    public List<String> getListOfWorkflows() {
        return listOfWorkflows;
    }
}
