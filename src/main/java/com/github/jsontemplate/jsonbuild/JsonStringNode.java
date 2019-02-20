package com.github.jsontemplate.jsonbuild;

import java.util.function.Supplier;

public final class JsonStringNode extends AbstractJsonValueNode<String> {

    public JsonStringNode(Supplier<String> supplier) {
        super(supplier);
    }

    public static JsonStringNode of(String value) {
        return new JsonStringNode(() -> value);
    }

    @Override
    public String print() {
        return "\"" + supplier.get() + "\"";
    }

}
