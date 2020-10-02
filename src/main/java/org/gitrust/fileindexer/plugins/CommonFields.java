package org.gitrust.fileindexer.plugins;

public enum CommonFields {
    TEXT("text");

    private String name;

    private CommonFields(String name) {

        this.name = name;
    }

    public String getName() {
        return name;
    }
}
