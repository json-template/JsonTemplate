package com.github.jsontemplate.modelbuild;

import com.github.jsontemplate.jsonbuild.JsonBuilder;
import com.github.jsontemplate.jsonbuild.JsonNode;
import com.github.jsontemplate.modelbuild.handler.DefaultBuildHandler;
import com.github.jsontemplate.valueproducer.INodeProducer;

import java.util.Map;

public class ArrayPropertyDeclaration extends SimplePropertyDeclaration {

    ArrayPropertyDeclaration(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public void buildJsonTemplate(JsonBuilder builder, Map<String, INodeProducer> producerMap,
                                  Map<String, JsonNode> typeMap,
                                  Map<String, JsonNode> variableMap, DefaultBuildHandler defaultHandler) {
        if (parent == null) {
            builder.createArray();
        } else {
            if (builder.inObject()) {
                builder.putArray(propertyName);
            } else if (builder.inArray()) {
                builder.addArray();
            }
        }
        buildChildrenJsonTemplate(builder, producerMap, typeMap, variableMap, defaultHandler);
        String valueTypeName = findValueType();
        JsonNode jsonNode = buildNodeFromProducer(producerMap, valueTypeName);

        if (jsonNode == null) {
            jsonNode = typeMap.get(valueTypeName);
        }
        setArrayInfo(builder.peekArrayNode(), jsonNode);
        builder.end();
    }

    @Override
    public void applyVariables(Map<String, Object> variableMap) {
        this.properties.forEach(p -> p.applyVariables(variableMap));
    }
}
