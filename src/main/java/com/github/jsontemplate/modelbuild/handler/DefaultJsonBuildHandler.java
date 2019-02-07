package com.github.jsontemplate.modelbuild.handler;

public class DefaultJsonBuildHandler implements DefaultBuildHandler {

    @Override
    public void handle(String valueTypeName) {
        throw new IllegalArgumentException("Unknown value type name " + valueTypeName);
    }
}
