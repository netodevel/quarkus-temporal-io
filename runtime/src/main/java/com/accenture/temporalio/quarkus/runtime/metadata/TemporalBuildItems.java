package com.accenture.temporalio.quarkus.runtime.metadata;

import java.util.ArrayList;
import java.util.List;

public class TemporalBuildItems {

    private List<TemporalItems> buildItens = new ArrayList<>();

    public TemporalBuildItems() {
    }

    public void insertAll(List<TemporalItems> temporalItems) {
        this.buildItens.addAll(temporalItems);
    }

    public List<TemporalItems> getBuildItens() {
        return buildItens;
    }
}
