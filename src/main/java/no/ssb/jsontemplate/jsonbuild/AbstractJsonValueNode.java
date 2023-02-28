package no.ssb.jsontemplate.jsonbuild;

import java.util.function.Supplier;

abstract class AbstractJsonValueNode<T> implements JsonValueNode {

    protected Supplier<T> supplier;

    AbstractJsonValueNode(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public final String prettyString(int indentation) {
        return compactString();
    }
}
