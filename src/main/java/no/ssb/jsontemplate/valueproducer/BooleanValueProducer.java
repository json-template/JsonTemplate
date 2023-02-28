package no.ssb.jsontemplate.valueproducer;

import no.ssb.jsontemplate.jsonbuild.JsonBooleanNode;
import no.ssb.jsontemplate.jsonbuild.supplier.ListParamSupplier;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * This class produces a {@link JsonBooleanNode JsonBooleanNode} which can generate json boolean value.
 */
public class BooleanValueProducer extends AbstractValueProducer<JsonBooleanNode> {
    private static final Random random = new Random();

    /**
     * The type name used in the template, e.g. {aBooleanField: @b}
     */
    public static final String TYPE_NAME = "b";

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    /**
     * Produces a node which can generate a random boolean value
     *
     * @return the produced json boolean node
     */
    @Override
    public JsonBooleanNode produce() {
        return new JsonBooleanNode(random::nextBoolean);
    }

    /**
     * Produces a node which can generate a fixed boolean value
     *
     * @param value string representation of the boolean value
     * @return the produced json boolean node
     */
    @Override
    public JsonBooleanNode produce(String value) {
        boolean parsedBoolean = Boolean.parseBoolean(value);
        return new JsonBooleanNode(() -> parsedBoolean);
    }

    /**
     * Produces a node which selects a string in a list.
     * The selected string is parsed to a boolean.
     *
     * @param valueList the enumerated string values
     * @return the produced json boolean node
     */
    @Override
    public JsonBooleanNode produce(List<String> valueList) {
        List<Boolean> parsedValueList = valueList.stream().map(Boolean::parseBoolean).collect(Collectors.toList());
        return new JsonBooleanNode(new ListParamSupplier<>(parsedValueList));
    }
}
