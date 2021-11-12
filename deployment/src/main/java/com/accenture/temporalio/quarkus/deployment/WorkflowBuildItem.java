package com.accenture.temporalio.quarkus.deployment;

import com.accenture.temporalio.quarkus.runtime.metadata.TemporalBuildItem;
import io.quarkus.builder.item.SimpleBuildItem;

public final class WorkflowBuildItem extends SimpleBuildItem {

    private TemporalBuildItem temporalBuildItem;

    public WorkflowBuildItem(TemporalBuildItem temporalBuildItem) {
        this.temporalBuildItem = temporalBuildItem;
    }

    public WorkflowBuildItem() {
    }

    public TemporalBuildItem getTemporalBuildItems() {
        return temporalBuildItem;
    }

    public void setTemporalBuildItems(TemporalBuildItem temporalBuildItem) {
        this.temporalBuildItem = temporalBuildItem;
    }
}
