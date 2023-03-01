package no.ssb.jsontemplate.jsonbuild;

import java.util.function.Supplier;

public final class JsonIntegerNode extends AbstractJsonValueNode<Integer> {

    public JsonIntegerNode(Supplier<Integer> supplier) {
        super(supplier);
    }

    /**
     * Creates a JsonIntegerNode with a given integer value.
     *
     * @param value the integer value to be converted
     * @return the converted json integer node
     */
    public static JsonIntegerNode of(Integer value) {
        return new JsonIntegerNode(() -> value);
    }

    @Override
    public String compactString() {
        return Integer.toString(supplier.get());
    }
}
