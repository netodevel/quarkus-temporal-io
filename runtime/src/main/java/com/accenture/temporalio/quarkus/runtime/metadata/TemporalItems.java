package com.accenture.temporalio.quarkus.runtime.metadata;

public class TemporalItems {

    private String className;
    private Class clazz;
    private String type;

    public TemporalItems() {
    }

    public TemporalItems(String className, Class clazz, String type) {
        this.className = className;
        this.clazz = clazz;
        this.type = type;
    }

    public Class getClazz() {
        return clazz;
    }

    public String getClassName() {
        return className;
    }

    public String getType() {
        return type;
    }
}
