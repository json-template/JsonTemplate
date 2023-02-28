package no.ssb.jsontemplate.modelbuild.handler;

import no.ssb.jsontemplate.jsonbuild.JsonNode;

public interface DefaultBuildHandler {

    /**
     * A fallback handling of a type used in the template
     *
     * @param valueTypeName the name of the value type
     * @return the produced json node
     */
    JsonNode handle(String valueTypeName);
}
