package no.ssb.jsontemplate.valueproducer;


import no.ssb.jsontemplate.jsonbuild.JsonStringNode;

import java.time.Instant;

/**
 * This class produces a {@link JsonStringNode} which generates current ISO8601 timestamp.
 */
public class Iso8601ValueProducer extends AbstractValueProducer<JsonStringNode> {

    /**
     * The type name used in the template, e.g. {anIsoField: @iso8601}
     */
    public static final String TYPE_NAME = "iso8601";

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    /**
     * Produces a node which can generate current ISO8601 timestamp
     *
     * @return the produced json string node
     */
    @Override
    public JsonStringNode produce() {
        return new JsonStringNode(this::produceTs);
    }

    /**
     * Produces an ISO8601 timestamp based on current time
     *
     * @return ISO8601 timestamp string
     */
    protected String produceTs() {
        return Instant.now().toString();
    }
}

