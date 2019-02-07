package com.github.jsontemplate.jsonbuild;

import java.util.function.Supplier;

public class JsonIntegerNode extends AbstractJsonValueNode<Integer> {

    public static JsonIntegerNode of(Integer value) {
        return new JsonIntegerNode(() -> value);
    }

    public JsonIntegerNode(Supplier<Integer> supplier) {
        super(supplier);
    }

    @Override
    public String print() {
        return Integer.toString(supplier.get());
    }
}
