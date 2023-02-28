package no.ssb.jsontemplate.jsonbuild;

import java.util.function.Supplier;

/**
 * This class represents a producer of a json float value.
 */
public final class JsonFloatNode extends AbstractJsonValueNode<Float> {

    public JsonFloatNode(Supplier<Float> supplier) {
        super(supplier);
    }

    /**
     * Creates a JsonFloatNode with a given float value.
     *
     * @param value the float value to be converted
     * @return the converted json float node
     */
    public static JsonFloatNode of(Float value) {
        return new JsonFloatNode(() -> value);
    }

    @Override
    public String compactString() {
        return Float.toString(supplier.get());
    }
}
