package com.github.jsontemplate.modelbuild;

import com.github.jsontemplate.jsonbuild.JsonNode;
import com.github.jsontemplate.jsonbuild.JsonWrapperNode;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DefaultJsonBuildHandler implements DefaultBuildHandler {

    @Override
    public void handle(String valueTypeName) {
        throw new IllegalArgumentException("Unknown value type name " + valueTypeName);
    }
}
