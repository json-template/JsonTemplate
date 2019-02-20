package com.github.jsontemplate.jsonbuild;

import java.util.function.Supplier;

public final class JsonIntegerNode extends AbstractJsonValueNode<Integer> {

    public JsonIntegerNode(Supplier<Integer> supplier) {
        super(supplier);
    }

    public static JsonIntegerNode of(Integer value) {
        return new JsonIntegerNode(() -> value);
    }

    @Override
    public String print() {
        return Integer.toString(supplier.get());
    }
}
