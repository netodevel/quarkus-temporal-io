package com.accenture.temporalio.quarkus.runtime.metadata;

import java.util.HashMap;
import java.util.Map;

public final class TemporalBuildItem {

    private final class TemporalItems {
        public String className;
        public String type;

        public TemporalItems() {
        }

        public TemporalItems(String className, String type) {
            this.className = className;
            this.type = type;
        }
    }

    private Map<Class, TemporalItems> buildItens = new HashMap<>();

    public TemporalBuildItem() {
    }

    public void put(Class clazz, String type) {
        buildItens.put(clazz, new TemporalItems(clazz.getName(), type));
    }

    public Map<Class, TemporalItems> getBuildItens() {
        return buildItens;
    }
}
