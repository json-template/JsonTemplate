package no.ssb.jsontemplate.modelbuild.handler;

import no.ssb.jsontemplate.jsonbuild.JsonNode;
import no.ssb.jsontemplate.jsonbuild.JsonWrapperNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class DefaultTypeBuildHandler implements DefaultBuildHandler {

    private Map<String, List<JsonWrapperNode>> missTypeMap;

    public DefaultTypeBuildHandler(Map<String, List<JsonWrapperNode>> missTypeMap) {
        this.missTypeMap = missTypeMap;
    }

    @Override
    public JsonNode handle(String valueTypeName) {
        JsonWrapperNode jsonNode = new JsonWrapperNode();
        List<JsonWrapperNode> jsonWrapperNodes = missTypeMap.get(valueTypeName);
        if (jsonWrapperNodes == null) {
            List<JsonWrapperNode> wrapperNodes = new ArrayList<>();
            wrapperNodes.add(jsonNode);
            missTypeMap.put(valueTypeName, wrapperNodes);
        } else {
            jsonWrapperNodes.add(jsonNode);
        }
        return jsonNode;
    }

}
