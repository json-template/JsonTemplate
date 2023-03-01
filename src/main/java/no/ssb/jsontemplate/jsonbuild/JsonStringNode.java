package no.ssb.jsontemplate.jsonbuild;

import java.util.function.Supplier;

/**
 * This class represents a producer of a json string value.
 */
public final class JsonStringNode extends AbstractJsonValueNode<String> {

    public JsonStringNode(Supplier<String> supplier) {
        super(supplier);
    }

    /**
     * Creates a JsonStringNode with a given string value.
     *
     * @param value the string value to be converted
     * @return the converted json string node
     */
    public static JsonStringNode of(String value) {
        return new JsonStringNode(() -> value);
    }

    @Override
    public String compactString() {
        return "\"" + supplier.get() + "\"";
    }

}
