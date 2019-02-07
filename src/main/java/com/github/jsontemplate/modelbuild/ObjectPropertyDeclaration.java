package com.github.jsontemplate.modelbuild;

import com.github.jsontemplate.jsonbuild.JsonBuilder;
import com.github.jsontemplate.jsonbuild.JsonNode;
import com.github.jsontemplate.valueproducer.INodeProducer;

import java.util.Map;

public class ObjectPropertyDeclaration extends SimplePropertyDeclaration {

    ObjectPropertyDeclaration(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public void buildJsonTemplate(JsonBuilder builder, Map<String, INodeProducer> producerMap,
                                     Map<String, JsonNode> typeMap,
                                     Map<String, JsonNode> variableMap, DefaultBuildHandler defaultHandler) {
        if (parent == null) {
            builder.createObject();
        } else {
            if (builder.inObject()) {
                builder.putObject(propertyName);
            } else if (builder.inArray()) {
                builder.addObject();
            }
        }
        buildChildrenJsonTemplate(builder, producerMap, typeMap, variableMap, defaultHandler);
        builder.end();
    }

    @Override
    public void applyVariables(Map<String, Object> variableMap) {
        this.properties.forEach(p -> p.applyVariables(variableMap));
    }
}
