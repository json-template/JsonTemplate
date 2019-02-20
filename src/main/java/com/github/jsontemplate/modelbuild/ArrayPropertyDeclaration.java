package com.github.jsontemplate.modelbuild;

import com.github.jsontemplate.jsonbuild.JsonBuilder;
import com.github.jsontemplate.jsonbuild.JsonNode;
import com.github.jsontemplate.modelbuild.handler.DefaultBuildHandler;
import com.github.jsontemplate.valueproducer.INodeProducer;

import java.util.Map;

final class ArrayPropertyDeclaration extends SimplePropertyDeclaration {

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
        if (this.typeSpec == null) {
            TypeSpec ancestorTypeSpec = findAncestorTypeSpec();
            this.typeSpec.setTypeName(ancestorTypeSpec.getTypeName());
        }

        JsonNode jsonNode = buildNodeFromProducer(producerMap);

        if (jsonNode == null) {
            jsonNode = typeMap.get(this.typeSpec.getTypeName());
        }
        setArrayInfo(builder.peekArrayNode(), jsonNode);
        builder.end();
    }

    @Override
    public void applyVariablesToParameters(Map<String, Object> variableMap) {
        this.properties.forEach(p -> p.applyVariablesToParameters(variableMap));
    }
}
