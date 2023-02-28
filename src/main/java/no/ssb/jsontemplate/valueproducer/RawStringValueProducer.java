package no.ssb.jsontemplate.valueproducer;

import no.ssb.jsontemplate.jsonbuild.JsonRawNode;

/**
 * This class produces a {@link JsonRawNode} which prints the content
 * of the given string without quotes. This is suitable for embedding
 * another json string.
 */
public class RawStringValueProducer extends AbstractValueProducer<JsonRawNode> {

    /**
     * The type name used in the template, e.g. {aStringField: @raw}
     */
    public static final String TYPE_NAME = "raw";

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    @Override
    public JsonRawNode produce(String value) {
        return new JsonRawNode(() -> value);
    }

}
