package com.github.jsontemplate.valueproducer;

import com.github.jsontemplate.jsonbuild.JsonStringNode;

import java.util.List;

/**
 * This class produces a {@link JsonStringNode JsonStringNode} which is used to concatenate multiple strings
 */
public class ConcatStringProducer extends AbstractValueProducer<JsonStringNode> {
    /**
     * The type name used in the template, e.g. {aStringField: @cat}
     */
    public static final String TYPE_NAME = "cat";

    @Override
    public String getTypeName() { return TYPE_NAME; }

    /**
     * Produces an empty {@link JsonStringNode JsonStringNode}
     * @return Produced JSON string value
     */
    @Override
    public JsonStringNode produce() { return new JsonStringNode(() -> ""); }

    /**
     * Produces a {@link JsonStringNode JsonStringNode} that contains the input
     * @param value The string value
     * @return Produced JSON string value
     */
    @Override
    public JsonStringNode produce(String value) { return new JsonStringNode(() -> value); }

    /**
     * Produces a {@link JsonStringNode JsonStringNode} that contains the input strings concatenated together
     * @param valueList the enumerated string values
     * @return Produced JSON string value
     */
    @Override
    public JsonStringNode produce(List<String> valueList) { return new JsonStringNode(() -> catString(valueList)); }

    /**
     * Concatenates a list of strings into a single string using the Java 8 Stream API.
     * The strings in the list are combined sequentially from the beginning to the end
     * using the '+' operator, resulting in a final concatenated string.
     *
     * @param valueList  The list of strings to be concatenated.
     * @return A single string obtained by concatenating all the strings in the given list.
     */
    public String catString(List<String> valueList) {
        return valueList.stream().reduce("", (a, b) -> a + b);
    }
}
