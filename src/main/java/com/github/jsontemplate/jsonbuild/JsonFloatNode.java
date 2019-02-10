package com.github.jsontemplate.jsonbuild;

import java.util.function.Supplier;

public class JsonFloatNode extends AbstractJsonValueNode<Float> {

    public JsonFloatNode(Supplier<Float> supplier) {
        super(supplier);
    }

    public static JsonFloatNode of(Float value) {
        return new JsonFloatNode(() -> value);
    }

    @Override
    public String print() {
        return Float.toString(supplier.get());
    }
}
