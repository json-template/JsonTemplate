package no.ssb.jsontemplate.modelbuild;

import no.ssb.jsontemplate.jsonbuild.JsonBuilder;
import no.ssb.jsontemplate.jsonbuild.JsonNode;
import no.ssb.jsontemplate.modelbuild.handler.DefaultBuildHandler;
import no.ssb.jsontemplate.valueproducer.IValueProducer;

import java.util.Map;

final class ArrayPropertyDeclaration extends BasePropertyDeclaration {

    ArrayPropertyDeclaration(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public void buildJsonTemplate(JsonBuilder builder, Map<String, IValueProducer<JsonNode>> producerMap,
                                  Map<String, JsonNode> typeMap,
                                  Map<String, JsonNode> variableMap, String defaultTypeName,
                                  DefaultBuildHandler defaultHandler) {
        if (parent == null) {
            builder.createArray();
        } else {
            if (builder.inObject()) {
                builder.putArray(propertyName);
            } else if (builder.inArray()) {
                builder.addArray();
            }
        }
        buildChildrenJsonTemplate(builder, producerMap, typeMap, variableMap, defaultTypeName, defaultHandler);
        if (this.typeSpec.getTypeName() == null) {
            TypeSpec ancestorTypeSpec = findAncestorTypeSpec(defaultTypeName);
            this.typeSpec.setTypeName(ancestorTypeSpec.getTypeName());
        }

        JsonNode jsonNode = buildNodeFromProducer(producerMap);

        if (jsonNode == null) {
            jsonNode = typeMap.get(this.typeSpec.getTypeName());
        }
        if (jsonNode == null) {
            jsonNode = defaultHandler.handle(this.typeSpec.getTypeName());
        }
        setArrayInfo(builder.peekArrayNode(), jsonNode);
        builder.end();
    }

    @Override
    public void applyVariablesToParameters(Map<String, Object> variableMap) {
        this.properties.forEach(p -> p.applyVariablesToParameters(variableMap));
    }
}
