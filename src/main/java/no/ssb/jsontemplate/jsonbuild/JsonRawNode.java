package no.ssb.jsontemplate.jsonbuild;

import java.util.function.Supplier;

/**
 * This class represents a producer of a json raw value. Compared to JsonStringNode,
 * this class does not wrap the value as a json string value.
 */
public final class JsonRawNode extends AbstractJsonValueNode<String> {

    public JsonRawNode(Supplier<String> supplier) {
        super(supplier);
    }

    @Override
    public String compactString() {
        return supplier.get();
    }
}
