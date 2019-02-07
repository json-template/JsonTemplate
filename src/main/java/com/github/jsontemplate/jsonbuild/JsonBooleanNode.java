package com.github.jsontemplate.jsonbuild;

import java.util.function.Supplier;

public class JsonBooleanNode extends AbstractJsonValueNode<Boolean> {

    public static JsonBooleanNode of(Boolean value) {
        return new JsonBooleanNode(() -> value);
    }

    public JsonBooleanNode(Supplier<Boolean> supplier) {
        super(supplier);
    }

    @Override
    public String print() {
        return Boolean.toString(supplier.get());
    }

}
