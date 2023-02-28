package no.ssb.jsontemplate.jsonbuild;

import java.util.function.Supplier;

/**
 * This class represents a producer of a json boolean value.
 */
public final class JsonBooleanNode extends AbstractJsonValueNode<Boolean> {

    public JsonBooleanNode(Supplier<Boolean> supplier) {
        super(supplier);
    }

    /**
     * Creates a JsonBooleanNode with a given boolean value.
     *
     * @param value the boolean object to be converted
     * @return the converted json boolean node
     */
    public static JsonBooleanNode of(Boolean value) {
        return new JsonBooleanNode(() -> value);
    }

    @Override
    public String compactString() {
        return Boolean.toString(supplier.get());
    }

}
