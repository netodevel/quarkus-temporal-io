package com.accenture.temporalio.quarkus.deployment;

import com.accenture.temporalio.quarkus.runtime.metadata.TemporalBuildItems;
import io.quarkus.builder.item.SimpleBuildItem;

public final class WorkflowBuildItem extends SimpleBuildItem {

    private TemporalBuildItems temporalBuildItems;

    public WorkflowBuildItem(TemporalBuildItems temporalBuildItems) {
        this.temporalBuildItems = temporalBuildItems;
    }

    public WorkflowBuildItem() {
    }

    public TemporalBuildItems getTemporalBuildItems() {
        return temporalBuildItems;
    }

    public void setTemporalBuildItems(TemporalBuildItems temporalBuildItems) {
        this.temporalBuildItems = temporalBuildItems;
    }
}
