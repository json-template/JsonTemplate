package no.ssb.jsontemplate.modelbuild;

import no.ssb.jsontemplate.jsonbuild.JsonBuilder;
import no.ssb.jsontemplate.jsonbuild.JsonNode;
import no.ssb.jsontemplate.modelbuild.handler.DefaultBuildHandler;
import no.ssb.jsontemplate.valueproducer.IValueProducer;

import java.util.Map;

final class ObjectPropertyDeclaration extends BasePropertyDeclaration {

    ObjectPropertyDeclaration(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public void buildJsonTemplate(JsonBuilder builder, Map<String, IValueProducer<JsonNode>> producerMap,
                                  Map<String, JsonNode> typeMap,
                                  Map<String, JsonNode> variableMap,
                                  String defaultTypeName,
                                  DefaultBuildHandler defaultHandler) {
        if (parent == null) {
            builder.createObject();
        } else {
            if (builder.inObject()) {
                builder.putObject(propertyName);
            } else if (builder.inArray()) {
                builder.addObject();
            }
        }
        buildChildrenJsonTemplate(builder, producerMap, typeMap, variableMap, defaultTypeName, defaultHandler);
        builder.end();
    }

    @Override
    public void applyVariablesToParameters(Map<String, Object> variableMap) {
        this.properties.forEach(p -> p.applyVariablesToParameters(variableMap));
    }
}
