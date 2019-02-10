package com.github.jsontemplate.jsonbuild;

import java.util.function.Supplier;

public class JsonBooleanNode extends AbstractJsonValueNode<Boolean> {

    public JsonBooleanNode(Supplier<Boolean> supplier) {
        super(supplier);
    }

    public static JsonBooleanNode of(Boolean value) {
        return new JsonBooleanNode(() -> value);
    }

    @Override
    public String print() {
        return Boolean.toString(supplier.get());
    }

}
