package com.github.jsontemplate.modelbuild.handler;

import com.github.jsontemplate.jsonbuild.JsonNode;
import com.github.jsontemplate.jsonbuild.JsonWrapperNode;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DefaultTypeBuildHandler implements DefaultBuildHandler {

    private Map<String, List<JsonWrapperNode>> missTypeMap;

    public DefaultTypeBuildHandler(Map<String, List<JsonWrapperNode>> missTypeMap) {
        this.missTypeMap = missTypeMap;
    }

    @Override
    public void handle(String valueTypeName) {
        JsonNode jsonNode = new JsonWrapperNode();
        List<JsonWrapperNode> jsonWrapperNodes = missTypeMap.get(valueTypeName);
        if (jsonWrapperNodes == null) {
            missTypeMap.put(valueTypeName, Arrays.asList((JsonWrapperNode) jsonNode));
        } else {
            jsonWrapperNodes.add((JsonWrapperNode) jsonNode);
        }
    }

}
