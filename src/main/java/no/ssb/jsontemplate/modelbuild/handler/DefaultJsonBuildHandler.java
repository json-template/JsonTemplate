package no.ssb.jsontemplate.modelbuild.handler;

import no.ssb.jsontemplate.jsonbuild.JsonNode;

public final class DefaultJsonBuildHandler implements DefaultBuildHandler {

    @Override
    public JsonNode handle(String valueTypeName) {
        throw new IllegalArgumentException("Unknown value type name " + valueTypeName);
    }
}
